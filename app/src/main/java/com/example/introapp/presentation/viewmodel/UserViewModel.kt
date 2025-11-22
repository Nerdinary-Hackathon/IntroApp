package com.example.introapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.introapp.domain.entity.Card
import com.example.introapp.domain.entity.CardList
import com.example.introapp.domain.entity.CardSummary
import com.example.introapp.domain.entity.JobGroup
import com.example.introapp.domain.usecase.ExchangeCardUseCase
import com.example.introapp.domain.usecase.GetCardListUseCase
import com.example.introapp.domain.usecase.GetCardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val exchangeCardUseCase: ExchangeCardUseCase,
    private val getCardUseCase: GetCardUseCase,
    private val getCardListUseCase: GetCardListUseCase,
): ViewModel() {

    // 명함 상세 조회 상태
    private val _cardState = MutableStateFlow<UiState<Card>>(UiState.Idle)
    val cardState: StateFlow<UiState<Card>> = _cardState.asStateFlow()

    // 명함 목록 조회 상태
    private val _cardListState = MutableStateFlow<UiState<CardList>>(UiState.Idle)
    val cardListState: StateFlow<UiState<CardList>> = _cardListState.asStateFlow()

    // 명함 교환 상태
    private val _exchangeState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val exchangeState: StateFlow<UiState<Unit>> = _exchangeState.asStateFlow()

    /**
     * 명함 교환
     */
    fun exchangeCard(userId: String, cardCode: String) {
        viewModelScope.launch {
            _exchangeState.value = UiState.Loading

            exchangeCardUseCase(userId, cardCode)
                .catch { error ->
                    _exchangeState.value = UiState.Error(
                        error.message ?: "알 수 없는 오류가 발생했습니다"
                    )
                }
                .collect { result ->
                    result.onSuccess {
                        _exchangeState.value = UiState.Success(Unit)
                    }.onFailure { error ->
                        _exchangeState.value = UiState.Error(
                            error.message ?: "명함 교환에 실패했습니다"
                        )
                    }
                }
        }
    }

    /**
     * 명함 조회
     */
    fun getCard(userId: String) {
        viewModelScope.launch {
            _cardState.value = UiState.Loading

            getCardUseCase(userId)
                .catch { error ->
                    _cardState.value = UiState.Error(
                        error.message ?: "알 수 없는 오류가 발생했습니다"
                    )
                }
                .collect { result ->
                    result.onSuccess { card ->
                        _cardState.value = UiState.Success(card)
                    }.onFailure { error ->
                        _cardState.value = UiState.Error(
                            error.message ?: "명함 조회에 실패했습니다"
                        )
                    }
                }
        }
    }

    /**
     * 명함 목록 조회
     */
    fun getCardList(
        userId: String,
        jobGroup: JobGroup?, // nullable로 변경
        cursor: String? = null,
        size: Int = 10
    ) {
        viewModelScope.launch {
            _cardListState.value = UiState.Loading

            getCardListUseCase(
                userId = userId,
                cursor = cursor,
                size = size,
                jobGroup = jobGroup // null 허용
            )
                .catch { error ->
                    _cardListState.value = UiState.Error(
                        error.message ?: "알 수 없는 오류가 발생했습니다"
                    )
                }
                .collect { result ->
                    result.onSuccess { cardList ->
                        _cardListState.value = UiState.Success(cardList)
                    }.onFailure { error ->
                        _cardListState.value = UiState.Error(
                            error.message ?: "목록 조회에 실패했습니다"
                        )
                    }
                }
        }
    }

    /**
     * 모든 직군의 명함 목록 조회 ("전체" 필터용)
     * - 이제 사용하지 않음, getCardList(jobGroup = null) 사용
     */
    @Deprecated("Use getCardList with jobGroup = null instead")
    fun getAllCardList(
        userId: String,
        cursor: String? = null,
        size: Int = 10
    ) {
        viewModelScope.launch {
            _cardListState.value = UiState.Loading

            try {
                // 모든 JobGroup에 대해 병렬로 API 호출
                val allJobGroups = JobGroup.entries
                val deferredResults = allJobGroups.map { jobGroup ->
                    async {
                        getCardListUseCase(
                            userId = userId,
                            cursor = cursor,
                            size = size,
                            jobGroup = jobGroup
                        ).first()
                    }
                }

                // 모든 결과를 기다림
                val results = deferredResults.awaitAll()

                // 성공한 결과들을 병합
                val allCards = mutableListOf<CardSummary>()
                var hasError = false
                var errorMessage = ""

                results.forEach { result ->
                    result.onSuccess { cardList ->
                        allCards.addAll(cardList.cards)
                    }.onFailure { error ->
                        hasError = true
                        errorMessage = error.message ?: "알 수 없는 오류가 발생했습니다"
                    }
                }

                // 결과 처리
                if (hasError && allCards.isEmpty()) {
                    // 모든 요청이 실패한 경우
                    _cardListState.value = UiState.Error(errorMessage)
                } else {
                    // 하나 이상 성공한 경우 병합된 리스트 반환
                    _cardListState.value = UiState.Success(
                        CardList(
                            cards = allCards,
                            pageSize = allCards.size,
                            nextCursor = null,
                            hasNext = false
                        )
                    )
                }
            } catch (e: Exception) {
                _cardListState.value = UiState.Error(
                    e.message ?: "알 수 없는 오류가 발생했습니다"
                )
            }
        }
    }

    /**
     * 명함 목록 더 불러오기 (페이징)
     */
    fun loadMoreCards(userId: String, jobGroup: JobGroup) {
        val currentState = _cardListState.value
        if (currentState is UiState.Success) {
            val currentData = currentState.data
            if (currentData.hasNext && currentData.nextCursor != null) {
                getCardList(
                    userId = userId,
                    jobGroup = jobGroup,
                    cursor = currentData.nextCursor
                )
            }
        }
    }

    /**
     * 상태 초기화
     */
    fun resetCardState() {
        _cardState.value = UiState.Idle
    }

    fun resetCardListState() {
        _cardListState.value = UiState.Idle
    }

    fun resetExchangeState() {
        _exchangeState.value = UiState.Idle
    }

}

sealed class UiState<out T> {
    data object Idle : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}