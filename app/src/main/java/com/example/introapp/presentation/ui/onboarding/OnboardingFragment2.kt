package com.example.introapp.presentation.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            layoutTop.bind(
                2,
                getString(R.string.onboarding2_title),
                getString(R.string.onboarding2_desc)
            )

            // 직무 목록 설정
            val jobs = listOf(
                getString(R.string.pm),
                getString(R.string.web),
                getString(R.string.backend),
                getString(R.string.design),
                getString(R.string.android),
                getString(R.string.ios)
            )
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
                val selectedJob = jobSelector.getSelectedItem()
                if (selectedJob != null) {
                    findNavController().navigate(R.id.action_page2_to_page3)
                } else {
                    Toast.makeText(requireActivity(), "직무를 선택해 주세요", Toast.LENGTH_SHORT).show()
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
        fun newInstance() = OnboardingFragment2()
    }
}