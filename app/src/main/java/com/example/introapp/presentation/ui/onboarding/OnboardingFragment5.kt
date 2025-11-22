package com.example.introapp.presentation.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.introapp.R
import com.example.introapp.databinding.FragmentOnboarding5Binding
import com.example.introapp.presentation.ui.HomeActivity
import com.example.introapp.presentation.ui.onboarding.component.CustomTypefaceSpan
import com.example.introapp.presentation.viewmodel.OnBoardingViewModel
import com.example.introapp.presentation.viewmodel.SubmitState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class OnboardingFragment5 : Fragment() {

    private var _binding: FragmentOnboarding5Binding? = null
    private val binding
        get() = _binding!!

    private val viewModel: OnBoardingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboarding5Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupProfileImage()

        binding.run {
            val nickname = viewModel.onboardingData.value.page1.nickname
            val descText = getString(R.string.profile_setting_complete_desc)
            val fullText = "$nickname$descText"
            val spannable = SpannableString(fullText)

            val color = ContextCompat.getColor(requireContext(), R.color.backend_deep)
            spannable.setSpan(
                ForegroundColorSpan(color),
                0,
                nickname.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // 닉네임 부분에 pretendard_bold 폰트 적용
            val typeface = ResourcesCompat.getFont(requireContext(), R.font.pretendard_bold)
            spannable.setSpan(
                CustomTypefaceSpan(typeface),
                0,
                nickname.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            binding.tvOnboardingCompleteDesc.text = spannable

            btnGoToMain.setOnClickListener {
                viewModel.submitProfile()
            }
        }
    }

    /**
     * 직무에 따라 프로필 이미지 다르게 설정
     */
    private fun setupProfileImage() {
        val job = viewModel.onboardingData.value.page2.job

        val imageRes = when (job) {
            "PM(기획)" -> R.drawable.pm_onboarding_profile
            "DESIGNER" -> R.drawable.design_onboarding_profile
            "WEB" -> R.drawable.web_onboarding_profile
            "BACKEND" -> R.drawable.backend_onboarding_profile
            "ANDROID" -> R.drawable.android_onboarding_profile
            "IOS" -> R.drawable.ios_onboarding_profile
            else -> R.drawable.android_onboarding_profile // 기본값
        }

        binding.ivOnboardingProfile.setImageResource(imageRes)
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.submitState.collect { state ->
                when (state) {
                    is SubmitState.Idle -> {
                        // 초기 상태
                        hideLoading()
                    }
                    is SubmitState.Loading -> {
                        // 로딩 표시
                        showLoading()
                        Timber.d("## [프로필 제출] 로딩 중...")
                    }
                    is SubmitState.Success -> {
                        // 성공 - 홈으로 이동
                        hideLoading()
                        val userId = state.user.userId
                        Timber.d("## [프로필 제출] 성공! userId: $userId")
                        Toast.makeText(
                            requireContext(),
                            "프로필이 성공적으로 등록되었습니다!",
                            Toast.LENGTH_SHORT
                        ).show()

                        navigateToHome()
                    }
                    is SubmitState.Error -> {
                        // 에러 표시
                        hideLoading()
                        Timber.e("## [프로필 제출] 실패: ${state.message}")
                        Toast.makeText(
                            requireContext(),
                            "오류: ${state.message}",
                            Toast.LENGTH_LONG
                        ).show()

                        // 상태 초기화
                        viewModel.resetSubmitState()
                    }
                }
            }
        }
    }

    /**
     * 로딩 표시
     */
    private fun showLoading() {
        binding.btnGoToMain.isEnabled = false
        binding.btnGoToMain.text = "등록 중..."
    }

    /**
     * 로딩 숨김
     */
    private fun hideLoading() {
        binding.btnGoToMain.isEnabled = true
        binding.btnGoToMain.text = "내 프로필 확인하기"
    }

    /**
     * 홈 화면으로 이동
     */
    private fun navigateToHome() {
        startActivity(Intent(requireActivity(), HomeActivity::class.java))
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = OnboardingFragment5()
    }
}