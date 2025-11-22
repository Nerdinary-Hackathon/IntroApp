package com.example.introapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.introapp.R
import com.example.introapp.databinding.FragmentCodeBinding
import com.example.introapp.domain.entity.Card
import com.example.introapp.domain.entity.JobGroup
import com.example.introapp.presentation.viewmodel.OnBoardingViewModel
import com.example.introapp.presentation.viewmodel.UiState
import com.example.introapp.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

// 나의 코드
@AndroidEntryPoint
class CodeFragment : Fragment() {
    private lateinit var binding: FragmentCodeBinding
    private val onBoardingViewModel: OnBoardingViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    private var currentUserId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            btnExchangeCard.setOnClickListener {
                currentUserId?.let { userId ->
                    val cardCode = etFriendCode.text.toString()
                    userViewModel.exchangeCard(userId, cardCode)
                } ?: run {
                    Toast.makeText(requireContext(), "사용자 ID를 불러오는 중입니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    onBoardingViewModel.getSavedUserId().collect { userId ->
                        if (userId != null) {
                            currentUserId = userId.toString()
                            binding.tvCode.text = currentUserId
                            // 사용자 ID를 받으면 카드 정보 조회
                            userViewModel.getCard(userId.toString())
                        }
                    }
                }

                launch {
                    userViewModel.exchangeState.collect { state ->
                        when (state) {
                            is UiState.Error -> {
                                Timber.e("## [명함 교환] 에러 : ${state.message}")
                                // 로딩 오버레이 숨김
                                hideLoading()
                                Toast.makeText(requireContext(), "이미 추가한 명함입니다", Toast.LENGTH_SHORT).show()
                                // 에러 후 상태 초기화
                                userViewModel.resetExchangeState()
                            }
                            UiState.Idle -> {
                                // 초기 상태
                                hideLoading()
                            }
                            UiState.Loading -> {
                                // 로딩 중 - 프로그레스바 표시
                                showLoading()
                            }
                            is UiState.Success -> {
                                // 로딩 오버레이 숨김
                                hideLoading()
                                Toast.makeText(requireActivity(), "명함 교환에 성공했습니다!", Toast.LENGTH_SHORT).show()
                                // 입력 필드 초기화
                                binding.etFriendCode.text?.clear()
                                // 성공 후 상태 초기화
                                userViewModel.resetExchangeState()
                            }
                        }
                    }
                }

                // 카드 정보 조회 (JobGroup 정보 획득)
                launch {
                    userViewModel.cardState.collect { cardState ->
                        when (cardState) {
                            is UiState.Success -> {
                                val card = cardState.data
                                Timber.d("## [CodeFragment] 카드 조회 성공 - JobGroup: ${card.jobGroup}")
                                // JobGroup에 따라 색상 변경
                                updateCardColors(card.jobGroup)
                            }
                            is UiState.Error -> {
                                Timber.e("## [CodeFragment] 카드 조회 실패 - ${cardState.message}")
                            }
                            else -> { /* Loading, Idle 상태는 무시 */ }
                        }
                    }
                }
            }
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
            // 외부 카드의 테두리 및 배경색 변경
            mcvCodeCard.strokeColor = strokeColor
            mcvCodeCard.setCardBackgroundColor(backgroundColor)

            // 내부 코드 카드의 테두리색 변경
            cdvCode.strokeColor = strokeColor

            // 코드 텍스트 색상 변경 (JobGroup에 따라)
            tvCode.setTextColor(strokeColor)

            // 안내 텍스트 색상 변경
            tvGuideline.setTextColor(strokeColor)
        }
    }

    /**
     * 로딩 오버레이 표시
     */
    private fun showLoading() {
        binding.loadingOverlay.visibility = View.VISIBLE
    }

    /**
     * 로딩 오버레이 숨김
     */
    private fun hideLoading() {
        binding.loadingOverlay.visibility = View.GONE
    }
}