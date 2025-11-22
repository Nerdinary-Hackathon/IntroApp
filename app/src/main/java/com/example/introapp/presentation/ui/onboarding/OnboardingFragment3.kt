package com.example.introapp.presentation.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.introapp.R
import com.example.introapp.databinding.FragmentOnboarding3Binding
import com.example.introapp.presentation.viewmodel.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment3 : Fragment() {

    private var _binding: FragmentOnboarding3Binding? = null
    private val binding get() = _binding!!

    private val viewModel: OnBoardingViewModel by activityViewModels()

    // 전체 선택된 기술 스택
    private val allSelectedTechStacks = mutableSetOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboarding3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        binding.run {
            layoutTop.bind(3, "기술 스택", "사용 가능한 기술을 모두 선택해주세요")

            // 프론트엔드
            categoryFrontend.setCategory(
                "프론트엔드",
                listOf("React", "Vue", "Next.js", "TypeScript", "Flutter")
            )
            categoryFrontend.setOnSelectionChangedListener { selected ->
                updateTechStacks("프론트엔드", selected)
            }

            // 백엔드
            categoryBackend.setCategory(
                "백엔드",
                listOf("Java", "Spring", "Python", "Django", "Node.js")
            )
            categoryBackend.setOnSelectionChangedListener { selected ->
                updateTechStacks("백엔드", selected)
            }

            // 디자인
            categoryDesign.setCategory(
                "디자인",
                listOf("Figma", "Photoshop", "Sketch", "3D", "Illustrator")
            )
            categoryDesign.setOnSelectionChangedListener { selected ->
                updateTechStacks("디자인", selected)
            }

            // 인프라/기타
            categoryInfra.setCategory(
                "인프라/기타",
                listOf("AWS", "Docker", "Kubernetes", "Git")
            )
            categoryInfra.setOnSelectionChangedListener { selected ->
                updateTechStacks("인프라/기타", selected)
            }

            // 이전에 선택한 값이 있다면 복원
            val currentTechStacks = viewModel.onboardingData.value.page3.selectedTechStacks
            if (currentTechStacks.isNotEmpty()) {
                allSelectedTechStacks.addAll(currentTechStacks)
                restoreSelections(currentTechStacks)
            }

            // 다음 버튼
            btnGoTo4.setOnClickListener {
                viewModel.updateTechStacks(allSelectedTechStacks)
                findNavController().navigate(R.id.action_page3_to_page4)
            }
        }
    }

    private fun updateTechStacks(category: String, selected: Set<String>) {
        // 해당 카테고리의 기존 선택 항목 제거
        allSelectedTechStacks.removeAll(getTechStacksByCategory(category))

        // 새로운 선택 항목 추가
        allSelectedTechStacks.addAll(selected)

        // ViewModel 업데이트
        viewModel.updateTechStacks(allSelectedTechStacks)
    }

    private fun getTechStacksByCategory(category: String): List<String> {
        return when (category) {
            "프론트엔드" -> listOf("React", "Vue", "Next.js", "TypeScript", "Flutter")
            "백엔드" -> listOf("Java", "Spring", "Python", "Django", "Node.js")
            "디자인" -> listOf("Figma", "Photoshop", "Sketch", "3D", "Illustrator")
            "인프라/기타" -> listOf("AWS", "Docker", "Kubernetes", "Git")
            else -> emptyList()
        }
    }

    private fun restoreSelections(techStacks: Set<String>) {
        binding.run {
            val frontend = techStacks.filter { getTechStacksByCategory("프론트엔드").contains(it) }.toSet()
            val backend = techStacks.filter { getTechStacksByCategory("백엔드").contains(it) }.toSet()
            val design = techStacks.filter { getTechStacksByCategory("디자인").contains(it) }.toSet()
            val infra = techStacks.filter { getTechStacksByCategory("인프라/기타").contains(it) }.toSet()

            if (frontend.isNotEmpty()) categoryFrontend.setSelectedItems(frontend)
            if (backend.isNotEmpty()) categoryBackend.setSelectedItems(backend)
            if (design.isNotEmpty()) categoryDesign.setSelectedItems(design)
            if (infra.isNotEmpty()) categoryInfra.setSelectedItems(infra)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = OnboardingFragment3()
    }
}