package com.example.introapp.presentation.ui.detail

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.example.introapp.R
import com.example.introapp.databinding.ActivityCardDetailBinding
import com.example.introapp.domain.entity.Card
import com.example.introapp.domain.entity.JobGroup
import com.example.introapp.presentation.viewmodel.UiState
import com.example.introapp.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class CardDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardDetailBinding
    private val userViewModel: UserViewModel by viewModels()

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCardDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true  // 상태 바 아이콘/텍스트를 어둡게
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cardDetailMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userId = intent.getIntExtra("userId", -1)

        userViewModel.getCard(userId.toString())

        lifecycleScope.launch {
            userViewModel.cardState.collect { state ->
                when (state) {
                    is UiState.Error -> {
                        Timber.e("## [명함 조회] Error - ${state.message}")
                        // 로딩 오버레이 숨김
                        hideLoading()
                        Toast.makeText(this@CardDetailActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    UiState.Idle -> {
                        Timber.d("## [명함 조회] Idle")
                        // 로딩 오버레이 숨김
                        hideLoading()
                    }
                    UiState.Loading -> {
                        Timber.d("## [명함 조회] Loading")
                        // 로딩 오버레이 표시
                        showLoading()
                    }
                    is UiState.Success -> {
                        val card = state.data
                        Timber.d("## [명함 조회] 성공 - card : $card")
                        // 로딩 오버레이 숨김
                        hideLoading()
                        updateCardUI(card)
                    }
                }
            }
        }

        binding.ivArrowLeft.setOnClickListener { finish() }
    }

    private fun updateCardUI(card: Card) {
        binding.run {
            tvName.text = card.nickname
            tvJob.text = card.jobGroup.displayName

            // JobGroup에 따라 색상 변경
            updateCardColors(card.jobGroup)

            // JobGroup에 따라 프로필 이미지 변경
            updateProfileImage(card.jobGroup)

            llTech.removeAllViews()
            card.techStacks.forEachIndexed { index, techStack ->
                val textView = createTechStackTextView(techStack.toString(), index, card.jobGroup)
                llTech.addView(textView)
            }
            tvPhone.text = card.phoneNumber
            tvEmail.text = card.email
            tvLink.text = card.link
        }
    }

    /**
     * JobGroup에 따라 카드의 배경색과 테두리 색을 변경
     *
     * @param jobGroup 직무 그룹
     */
    private fun updateCardColors(jobGroup: JobGroup) {
        val (strokeColorRes, backgroundColorRes) = when (jobGroup) {
            JobGroup.PM -> R.color.pm_deep to R.color.pm_soft
            JobGroup.DESIGNER -> R.color.design_deep to R.color.design_soft
            JobGroup.WEB -> R.color.web_deep to R.color.web_soft
            JobGroup.BACKEND -> R.color.backend_deep to R.color.backend_soft
            JobGroup.ANDROID -> R.color.android_deep to R.color.android_soft
            JobGroup.IOS -> R.color.ios_deep to R.color.ios_soft
        }

        val strokeColor = getColor(strokeColorRes)
        val backgroundColor = getColor(backgroundColorRes)

        binding.apply {
            // 카드 테두리 및 배경색 변경
            mcvCard.strokeColor = strokeColor
            mcvCard.setCardBackgroundColor(backgroundColor)

            // 구분선 색상 변경
            divider1.dividerColor = strokeColor
            divider2.dividerColor = strokeColor
        }
    }

    /**
     * JobGroup에 따라 프로필 이미지를 변경
     *
     * @param jobGroup 직무 그룹
     */
    private fun updateProfileImage(jobGroup: JobGroup) {
        val profileImageRes = when (jobGroup) {
            JobGroup.PM -> R.drawable.pm_onboarding_profile
            JobGroup.DESIGNER -> R.drawable.design_onboarding_profile
            JobGroup.WEB -> R.drawable.web_onboarding_profile
            JobGroup.BACKEND -> R.drawable.backend_onboarding_profile
            JobGroup.ANDROID -> R.drawable.android_onboarding_profile
            JobGroup.IOS -> R.drawable.ios_onboarding_profile
        }

        binding.ivProfile.setImageResource(profileImageRes)
    }

    /**
     * 기술스택 TextView 동적 생성 함수
     *
     * @param text 표시할 기술스택 텍스트
     * @param index TextView의 인덱스 (첫 번째 항목은 marginStart 없음)
     * @param jobGroup 직무 그룹 (배경색 결정에 사용)
     */
    private fun createTechStackTextView(text: String, index: Int, jobGroup: JobGroup): TextView {
        // JobGroup에 따라 배경색 결정
        val backgroundColorRes = when (jobGroup) {
            JobGroup.PM -> R.color.pm_deep
            JobGroup.DESIGNER -> R.color.design_deep
            JobGroup.WEB -> R.color.web_deep
            JobGroup.BACKEND -> R.color.backend_deep
            JobGroup.ANDROID -> R.color.android_deep
            JobGroup.IOS -> R.color.ios_deep
        }

        return TextView(this).apply {
            // 텍스트 설정
            this.text = text

            // 레이아웃 파라미터 설정
            layoutParams = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT
            ).apply {
                // 첫 번째 항목이 아니면 왼쪽 여백 추가
                if (index > 0) {
                    marginStart = resources.getDimensionPixelSize(
                        R.dimen.tech_stack_margin
                    )
                }
            }

            // 스타일 설정
            setBackgroundResource(R.drawable.rounded_bg)
            // JobGroup에 따른 배경색 적용
            backgroundTintList = resources.getColorStateList(backgroundColorRes, null)
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 16f
            gravity = android.view.Gravity.CENTER
            maxLines = 1
            minWidth = resources.getDimensionPixelSize(R.dimen.tech_stack_min_width)
            minHeight = resources.getDimensionPixelSize(R.dimen.tech_stack_min_height)

            // 패딩 설정 (horizontal: 10dp, vertical: 6dp)
            val paddingHorizontal = resources.getDimensionPixelSize(
                R.dimen.tech_stack_padding_horizontal
            )
            val paddingVertical = resources.getDimensionPixelSize(
                R.dimen.tech_stack_padding_vertical
            )
            setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)

            // 폰트 설정
            typeface = resources.getFont(R.font.pretendard_medium)
        }
    }

    /**
     * 로딩 오버레이 표시
     */
    private fun showLoading() {
        binding.loadingOverlay.visibility = android.view.View.VISIBLE
    }

    /**
     * 로딩 오버레이 숨김
     */
    private fun hideLoading() {
        binding.loadingOverlay.visibility = android.view.View.GONE
    }
}