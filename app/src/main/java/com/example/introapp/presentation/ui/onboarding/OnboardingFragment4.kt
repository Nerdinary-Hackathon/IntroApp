package com.example.introapp.presentation.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.introapp.R
import com.example.introapp.databinding.FragmentOnboarding4Binding
import com.example.introapp.presentation.viewmodel.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class OnboardingFragment4 : Fragment() {

    private var _binding: FragmentOnboarding4Binding? = null
    private val binding
        get() = _binding!!

    private val viewModel: OnBoardingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboarding4Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        binding.run {
            layoutTop.bind(4, "경력", "현재 나의 경력 레벨을 선택해주세요")

            // 경력 리스트 설정
            val careers = listOf("취업준비생 | 대학생", "신입 (1년 미만)", "주니어 (1~3년)", "시니어 (3년 이상)")
            careerSelector.setItems(careers)

            careerSelector.setOnItemSelectedListener { selectedCareer ->
                viewModel.updateCareer(selectedCareer)
            }

            // 이전에 선택한 값이 있다면 복원
            val currentCareer = viewModel.onboardingData.value.page4.career
            if (currentCareer.isNotEmpty()) {
                careerSelector.selectItemByValue(currentCareer)
            }

            btnGoToCompleteScreen.setOnClickListener {
                val selectedCareer = careerSelector.getSelectedItem()
                if (selectedCareer != null) {
                    // 프로필 설정 완료 프래그먼트로 이동
                    Toast.makeText(requireActivity(), "완료 화면 이동", Toast.LENGTH_SHORT).show()
                    // TODO: Navigate to completion screen
                    // findNavController().navigate(R.id.action_page4_to_page5)
                } else {
                    // 선택하지 않았을 때 에러 표시
                    Toast.makeText(requireActivity(), "경력을 선택해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = OnboardingFragment4()
    }
}