package com.example.introapp.presentation.ui.received_card

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.introapp.R
import com.example.introapp.databinding.ActivityReceivedCardBinding
import com.example.introapp.domain.entity.CardSummary
import com.example.introapp.domain.entity.JobGroup
import com.example.introapp.presentation.ui.detail.CardDetailActivity
import com.example.introapp.presentation.viewmodel.OnBoardingViewModel
import com.example.introapp.presentation.viewmodel.UiState
import com.example.introapp.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

// 받은 명함 리스트 화면
@AndroidEntryPoint
class ReceivedCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReceivedCardBinding
    private val receivedCardAdapter: ReceivedCardAdapter = ReceivedCardAdapter()

    private val onBoardingViewModel: OnBoardingViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    // 전체 카드 리스트 저장 (로컬 필터링용)
    private var allCardList: List<CardSummary> = emptyList()

    // 현재 사용자 ID
    private var currentUserId: String? = null

    // 현재 선택된 카테고리 (기본값: "전체")
    private var selectedCategory: String = "전체"

    // 카테고리 한글-JobGroup enum 매핑
    private val categoryMap = mapOf(
        "전체" to null,
        "PM" to JobGroup.PM,
        "디자인" to JobGroup.DESIGNER,
        "웹" to JobGroup.WEB,
        "백엔드" to JobGroup.BACKEND,
        "안드로이드" to JobGroup.ANDROID,
        "iOS" to JobGroup.IOS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReceivedCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true  // 상태 바 아이콘/텍스트를 어둡게
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.receivedCardMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupObservers()
        setupViews()
    }

    /**
     * ViewModel 상태 관찰 설정
     */
    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // userId 관찰 및 API 호출
                launch {
                    onBoardingViewModel.getSavedUserId().collect { userId ->
                        if (userId != null) {
                            currentUserId = userId.toString()
                            Timber.d("## [userId] 조회됨 - $currentUserId")

                            // userId를 받으면 카드 목록 조회
                            loadCardList()
                        } else {
                            Timber.e("## [userId] null - 사용자 ID를 가져올 수 없습니다")
                            Toast.makeText(
                                this@ReceivedCardActivity,
                                "사용자 정보를 가져올 수 없습니다",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                // 카드 목록 상태 관찰
                launch {
                    userViewModel.cardListState.collect { state ->
                        handleCardListState(state)
                    }
                }
            }
        }
    }

    /**
     * 뷰 설정
     */
    private fun setupViews() {
        binding.run {
            ivArrowLeft.setOnClickListener { finish() }

            // 카테고리 필터 설정
            categoryFilter.apply {
                setSingleSelectionMode(true)
                setCategory(
                    title = "",
                    items = listOf("전체", "PM", "디자인", "웹", "백엔드", "안드로이드", "iOS")
                )
                setSelectedItems(setOf(selectedCategory))

                setOnSelectionChangedListener { selectedItems ->
                    val newCategory = selectedItems.firstOrNull() ?: "전체"
                    if (newCategory != selectedCategory) {
                        selectedCategory = newCategory
                        Timber.d("## [카테고리] 선택됨 - $selectedCategory")
                        filterCardsByCategory(selectedCategory)
                    }
                }
            }

            // RecyclerView 설정
            rvReceivedCard.adapter = receivedCardAdapter
            receivedCardAdapter.setOnItemClickListener { cardSummary ->
                Timber.d("## [리사이클러뷰] 아이템 클릭 - jobGroup: ${cardSummary.jobGroup}, nickname: ${cardSummary.nickname}")
                // TODO: 명함 상세 화면으로 이동
                startActivity(
                    Intent(this@ReceivedCardActivity, CardDetailActivity::class.java)
                        .putExtra("userId", cardSummary.userId)
                )
            }
        }
    }

    /**
     * 카드 목록을 로드하는 함수
     * 현재 선택된 필터에 따라 API 호출
     */
    private fun loadCardList() {
        val userId = currentUserId
        if (userId == null) {
            Timber.w("## [loadCardList] userId가 null입니다")
            Toast.makeText(this, "사용자 정보가 없습니다", Toast.LENGTH_SHORT).show()
            return
        }

        // 선택된 카테고리에 해당하는 JobGroup 가져오기
        val selectedJobGroup = categoryMap[selectedCategory]

        if (selectedJobGroup != null) {
            // 특정 직군 선택 시: 해당 직군만 조회
            Timber.d("## [loadCardList] 특정 직군 조회 - $selectedJobGroup")
            userViewModel.getCardList(
                userId = userId,
                jobGroup = selectedJobGroup,
                cursor = null
            )
        } else {
            // "전체" 선택 시: 모든 직군의 카드를 조회
            Timber.d("## [loadCardList] 전체 직군 조회")
            userViewModel.getAllCardList(
                userId = userId,
                cursor = null
            )
        }
    }

    /**
     * 카드 목록 상태 처리
     */
    private fun handleCardListState(state: UiState<com.example.introapp.domain.entity.CardList>) {
        when (state) {
            is UiState.Error -> {
                Timber.e("## [명함 목록 조회] Error - ${state.message}")
                Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
            }
            UiState.Idle -> {
                Timber.d("## [명함 목록 조회] Idle")
            }
            UiState.Loading -> {
                Timber.d("## [명함 목록 조회] Loading")
                // TODO: 로딩 인디케이터 표시
            }
            is UiState.Success -> {
                Timber.d("## [명함 목록 조회] Success - ${state.data.cards.size}개")

                // 전체 리스트 저장
                allCardList = state.data.cards

                // 현재 선택된 필터에 따라 표시
                filterCardsByCategory(selectedCategory)
            }
        }
    }

    /**
     * 카테고리에 따라 카드 목록을 필터링하는 함수 (로컬 필터링)
     */
    private fun filterCardsByCategory(category: String) {
        val jobGroup = categoryMap[category]

        val filteredList = if (jobGroup == null) {
            // "전체" 선택 시 모든 카드 표시
            allCardList
        } else {
            // 선택된 jobGroup에 맞는 카드만 필터링
            allCardList.filter { it.jobGroup == jobGroup }
        }

        receivedCardAdapter.submitList(filteredList)
        updateEmptyState(filteredList.isEmpty())
        Timber.d("## [필터링] 카테고리: $category, jobGroup: $jobGroup, 결과: ${filteredList.size}개")
    }

    /**
     * 빈 상태 UI 업데이트
     *
     * @param isEmpty true면 빈 상태 텍스트 표시, false면 리사이클러뷰 표시
     */
    private fun updateEmptyState(isEmpty: Boolean) {
        binding.apply {
            if (isEmpty) {
                // 데이터가 없을 때: 빈 상태 텍스트 표시, 리사이클러뷰 숨김
                tvEmptyState.visibility = android.view.View.VISIBLE
                rvReceivedCard.visibility = android.view.View.GONE
            } else {
                // 데이터가 있을 때: 리사이클러뷰 표시, 빈 상태 텍스트 숨김
                tvEmptyState.visibility = android.view.View.GONE
                rvReceivedCard.visibility = android.view.View.VISIBLE
            }
        }
    }
}