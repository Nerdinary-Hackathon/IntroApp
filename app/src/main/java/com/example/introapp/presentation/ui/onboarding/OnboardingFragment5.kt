package com.example.introapp.presentation.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.introapp.databinding.FragmentOnboarding5Binding
import com.example.introapp.presentation.ui.HomeActivity
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

        Timber.d("## [온보딩 뷰모델] 뷰모델 값 확인 : ${viewModel.onboardingData.value.toProfile()}")

        binding.run {
            btnGoToMain.setOnClickListener {
                viewModel.submitProfile()
            }
        }
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