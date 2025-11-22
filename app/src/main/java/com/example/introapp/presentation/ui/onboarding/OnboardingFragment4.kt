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
            layoutTop.bind(
                4,
                getString(R.string.onboarding4_title),
                getString(R.string.onboarding4_desc)
            )

            // 경력 리스트 설정
            val careers = listOf(
                getString(R.string.student),
                getString(R.string.newcomer),
                getString(R.string.junior),
                getString(R.string.senior)
            )
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
                     findNavController().navigate(R.id.action_page4_to_page5)
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