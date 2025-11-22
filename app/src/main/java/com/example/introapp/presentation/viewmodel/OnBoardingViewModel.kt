package com.example.introapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.introapp.data.local.UserPreferencesDataStore
import com.example.introapp.domain.entity.JobGroup
import com.example.introapp.domain.entity.Level
import com.example.introapp.domain.entity.Profile
import com.example.introapp.domain.entity.TechStack
import com.example.introapp.domain.entity.User
import com.example.introapp.domain.repository.UserRepository
import com.example.introapp.domain.usecase.SubmitProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val submitProfileUseCase: SubmitProfileUseCase,
    private val userPreferencesDataStore: UserPreferencesDataStore,
) : ViewModel() {

    // 온보딩 데이터
    private val _onboardingData = MutableStateFlow(OnboardingData())
    val onboardingData: StateFlow<OnboardingData> = _onboardingData.asStateFlow()

    // 프로필 제출 상태
    private val _submitState = MutableStateFlow<SubmitState>(SubmitState.Idle)
    val submitState: StateFlow<SubmitState> = _submitState.asStateFlow()

    // 1페이지
    fun updateName(value: String) {
        _onboardingData.update { it.copy(page1 = it.page1.copy(name = value)) }
    }

    fun updateNickname(value: String) {
        _onboardingData.update { it.copy(page1 = it.page1.copy(nickname = value)) }
    }

    fun updatePhone(value: String) {
        _onboardingData.update { it.copy(page1 = it.page1.copy(phone = value)) }
    }

    fun updateEmail(value: String) {
        _onboardingData.update { it.copy(page1 = it.page1.copy(email = value)) }
    }

    fun updateLink(value: String) {
        _onboardingData.update { it.copy(page1 = it.page1.copy(link = value)) }
    }

    // 2페이지
    fun updateJob(value: String) {
        _onboardingData.update { it.copy(page2 = it.page2.copy(job = value)) }
    }

    // 3페이지 - 기술 스택 추가/제거
    fun updateTechStacks(techStacks: Set<String>) {
        _onboardingData.update { it.copy(page3 = it.page3.copy(selectedTechStacks = techStacks)) }
    }

    // 4페이지 - 경력
    fun updateCareer(value: String) {
        _onboardingData.update { it.copy(page4 = it.page4.copy(career = value)) }
    }

    // 프로필 작성
    fun submitProfile() {
        val data = _onboardingData.value

        // 입력 검증
        if (!validateData(data)) {
            _submitState.value = SubmitState.Error("입력값을 확인해주세요")
            return
        }

        viewModelScope.launch {
            _submitState.value = SubmitState.Loading

            // OnboardingData -> Profile entity
            val profile = data.toProfile()
            Timber.d("## [프로필 작성 api] profile : $profile")

            submitProfileUseCase(profile)
                .catch { error ->
                    Timber.e("## [프로필 작성 api] error : $error")
                    _submitState.value = SubmitState.Error(
                        error.message ?: "알 수 없는 오류가 발생했습니다"
                    )
                }
                .collect { result ->
                    result.onSuccess { user ->
                        _submitState.value = SubmitState.Success(user)
                        userPreferencesDataStore.saveUserId(user.userId)
                        Timber.e("## [프로필 작성 api] 성공 : $user")
                    }.onFailure { error ->
                        Timber.e("## [프로필 작성 api] 실패 : $error")
                        _submitState.value = SubmitState.Error(
                            error.message ?: "프로필 제출에 실패했습니다"
                        )
                    }
                }
        }
    }

    // api 호출 후 저장된 userId 리턴
    fun getSavedUserId(): Flow<Int?> = userPreferencesDataStore.getUserId()

    /**
     * 제출 상태 초기화
     */
    fun resetSubmitState() {
        _submitState.value = SubmitState.Idle
    }

    /**
     * 데이터 유효성 검증
     */
    private fun validateData(data: OnboardingData): Boolean {
        return data.page1.name.isNotBlank() &&
                data.page1.nickname.isNotBlank() &&
                data.page1.phone.isNotBlank() &&
                data.page1.email.isNotBlank() &&
                data.page2.job.isNotBlank() &&
                data.page3.selectedTechStacks.isNotEmpty() &&
                data.page4.career.isNotBlank()
    }
}

data class OnboardingData(
    val page1: Page1Data = Page1Data(),
    val page2: Page2Data = Page2Data(),
    val page3: Page3Data = Page3Data(),
    val page4: Page4Data = Page4Data(),
) {
    /**
     * OnboardingData를 Profile entity로 변환
     * 한글 displayName을 API value로 변환
     */
    fun toProfile(): Profile {
        return Profile(
            name = page1.name,
            nickname = page1.nickname,
            phone = page1.phone,
            email = page1.email,
            link = page1.link,
            jobGroup = JobGroup.from(page2.job),           // "BACKEND" -> JobGroup.BACKEND
            level = Level.from(page4.career),              // "JUNIOR" -> Level.JUNIOR
            techStacks = page3.selectedTechStacks.map {
                TechStack.from(it)                          // "JAVA" -> TechStack.JAVA
            }
        )
    }
}

data class Page1Data(
    val name: String = "",
    val nickname: String = "",
    val phone: String = "",
    val email: String = "",
    val link: String = "",
)

data class Page2Data(
    val job: String = ""
)

data class Page3Data(
    val selectedTechStacks: Set<String> = emptySet()
)

data class Page4Data(
    val career: String = ""
)

sealed class SubmitState {
    data object Idle : SubmitState()
    data object Loading : SubmitState()
    data class Success(val user: User) : SubmitState()
    data class Error(val message: String) : SubmitState()
}