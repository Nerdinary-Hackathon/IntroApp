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
            tvJob.text = card.jobGroup.toString()
            llTech.removeAllViews()
            card.techStacks.forEachIndexed { index, techStack ->
                val textView = createTechStackTextView(techStack.toString(), index)
                llTech.addView(textView)
            }
            tvPhone.text = card.phoneNumber
            tvEmail.text = card.email
            tvLink.text = card.link
        }
    }

    /**
     * 기술스택 TextView 동적 생성 함수
     *
     * @param text 표시할 기술스택 텍스트
     * @param index TextView의 인덱스 (첫 번째 항목은 marginStart 없음)
     */
    private fun createTechStackTextView(text: String, index: Int): android.widget.TextView {
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