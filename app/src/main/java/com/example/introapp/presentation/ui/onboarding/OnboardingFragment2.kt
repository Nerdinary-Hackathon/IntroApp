package com.example.introapp.presentation.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.introapp.R
import com.example.introapp.databinding.FragmentOnboarding2Binding
import com.example.introapp.presentation.viewmodel.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment2 : Fragment() {

    private var _binding: FragmentOnboarding2Binding? = null
    private val binding get() = _binding!!

    private val viewModel: OnBoardingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboarding2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        binding.run {
            layoutTop.bind(2, "직무", "나를 가장 잘 나타내는 직무를 하나 선택해주세요")

            // 직무 목록 설정
            val jobs = listOf("PM(기획)", "웹", "디자인", "백엔드", "안드로이드", "iOS")
            jobSelector.setItems(jobs)

            // 선택 리스너
            jobSelector.setOnItemSelectedListener { selectedJob ->
                viewModel.updateJob(selectedJob)
            }

            // 이전에 선택한 값이 있다면 복원
            val currentJob = viewModel.onboardingData.value.page2.job
            if (currentJob.isNotEmpty()) {
                jobSelector.selectItemByValue(currentJob)
            }

            // 다음 버튼
            btnGoTo3.setOnClickListener {
                findNavController().navigate(R.id.action_page2_to_page3)
//                val selectedJob = jobSelector.getSelectedItem()
//                if (selectedJob != null) {
//                    findNavController().navigate(R.id.action_page2_to_page3)
//                } else {
//                    // TODO: 선택하지 않았을 때 에러 표시
//                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = OnboardingFragment2()
    }
}