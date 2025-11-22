package com.example.introapp.presentation.ui.received_card

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.introapp.R
import com.example.introapp.data.model.response.CardItem
import com.example.introapp.databinding.ActivityReceivedCardBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ReceivedCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReceivedCardBinding
    private val receivedCardAdapter: ReceivedCardAdapter = ReceivedCardAdapter()

    // 전체 카드 리스트 (필터링 전)
    private var allCardList: List<CardItem> = emptyList()

    // 카테고리 한글-영어 매핑
    private val categoryMap = mapOf(
        "전체" to null,  // null은 필터링 없음을 의미
        "PM" to "PM",
        "디자인" to "Designer",
        "웹" to "Web",
        "백엔드" to "Backend",
        "안드로이드" to "Android",
        "iOS" to "iOS"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReceivedCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.receivedCardMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.run {
            ivArrowLeft.setOnClickListener { finish() }

            categoryFilter.apply {
                setSingleSelectionMode(true)
                setCategory(
                    title = "",
                    items = listOf("전체", "PM", "디자인", "웹", "백엔드", "안드로이드", "iOS")
                )
                setSelectedItems(setOf("전체"))

                setOnSelectionChangedListener { selectedItems ->
                    val selectedCategory = selectedItems.firstOrNull() ?: "전체"
                    Timber.d("## [카테고리] 선택됨 - $selectedCategory")
                    filterCardsByCategory(selectedCategory)
                }
            }

            rvReceivedCard.adapter = receivedCardAdapter
            receivedCardAdapter.setOnItemClickListener { cardItem ->
                Timber.d("## [리사이클러뷰] 아이템 클릭 - userId : ${cardItem.userId}, jobGroup : ${cardItem.jobGroup}, nickname : ${cardItem.nickName}")
            }

            setupMockData()
        }
    }

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
        Timber.d("## [필터링] 카테고리: $category, jobGroup: $jobGroup, 결과: ${filteredList.size}개")
    }

    // TODO : api 응답으로 변경
    private fun setupMockData() {
        val mockCardList = listOf(
            CardItem(
                userId = 1,
                profileImg = "backend_onboarding_profile",
                nickName = "디노",
                jobGroup = "Backend"
            ),
            CardItem(
                userId = 1,
                profileImg = "android_onboarding_profile",
                nickName = "민규",
                jobGroup = "Android"
            ),
            CardItem(
                userId = 1,
                profileImg = "ios_onboarding_profile",
                nickName = "승관",
                jobGroup = "iOS"
            ),
            CardItem(
                userId = 1,
                profileImg = "web_onboarding_profile",
                nickName = "우지",
                jobGroup = "Web"
            ),
            CardItem(
                userId = 1,
                profileImg = "design_onboarding_profile",
                nickName = "준휘",
                jobGroup = "Designer"
            ),
            CardItem(
                userId = 1,
                profileImg = "pm_onboarding_profile",
                nickName = "호시",
                jobGroup = "PM"
            ),
            CardItem(
                userId = 1,
                profileImg = "backend_onboarding_profile",
                nickName = "정한",
                jobGroup = "Backend"
            ),
            CardItem(
                userId = 1,
                profileImg = "android_onboarding_profile",
                nickName = "조슈아",
                jobGroup = "Android"
            )
        )

        allCardList = mockCardList
        receivedCardAdapter.submitList(mockCardList)
    }
}