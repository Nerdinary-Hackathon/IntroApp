package com.example.introapp.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.introapp.R
import com.example.introapp.databinding.FragmentCardBinding
import com.example.introapp.domain.entity.Card
import com.example.introapp.domain.entity.JobGroup
import com.example.introapp.presentation.viewmodel.OnBoardingViewModel
import com.example.introapp.presentation.viewmodel.UiState
import com.example.introapp.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.getValue

// 내 명함
@AndroidEntryPoint
class CardFragment : Fragment() {
    private lateinit var binding: FragmentCardBinding
    private val onBoardingViewModel: OnBoardingViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    onBoardingViewModel.getSavedUserId().collect { userId ->
                        if (userId != null) {
                            Timber.d("## [userId] 조회됨 - $userId")
                            userViewModel.getCard(userId.toString())
                        }
                    }
                }

                launch {
                    userViewModel.cardState.collect { cardState ->
                        when (cardState) {
                            is UiState.Error -> {
                                Timber.e("## [명함 조회] Error - ${cardState.message}")
                                Toast.makeText(requireContext(), cardState.message, Toast.LENGTH_SHORT).show()
                            }
                            UiState.Idle -> {
                                Timber.d("## [명함 조회] Idle")
                            }
                            UiState.Loading -> {
                                Timber.d("## [명함 조회] Loading")
                            }
                            is UiState.Success -> {
                                val card = cardState.data
                                Timber.d("## [명함 조회] 성공 - card : $card")
                                updateCardUI(card)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateCardUI(card: Card) {
        binding.apply {
            tvName.text = card.nickname
            tvJob.text = card.jobGroup.displayName

            // JobGroup에 따라 색상 변경
            updateCardColors(card.jobGroup)

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

        val strokeColor = resources.getColor(strokeColorRes, null)
        val backgroundColor = resources.getColor(backgroundColorRes, null)

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

        return TextView(requireContext()).apply {
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

}