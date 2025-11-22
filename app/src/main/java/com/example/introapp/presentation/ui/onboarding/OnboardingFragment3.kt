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
            layoutTop.bind(3, getString(R.string.onboarding3_title), getString(R.string.onboarding3_desc))

            // 프론트엔드
            categoryFrontend.setCategory(
                getString(R.string.frontend),
                listOf(getString(R.string.react), getString(R.string.vue), getString(R.string.nextjs), getString(R.string.typescript), getString(R.string.flutter))
            )
            categoryFrontend.setOnSelectionChangedListener { selected ->
                updateTechStacks(getString(R.string.frontend), selected)
            }

            // 백엔드
            categoryBackend.setCategory(
                getString(R.string.backend),
                listOf(getString(R.string.java), getString(R.string.spring), getString(R.string.python), getString(R.string.django), getString(R.string.nodejs))
            )
            categoryBackend.setOnSelectionChangedListener { selected ->
                updateTechStacks(getString(R.string.backend), selected)
            }

            // 디자인
            categoryDesign.setCategory(
                getString(R.string.design),
                listOf(getString(R.string.figma), getString(R.string.photoshop), getString(R.string.sketch), getString(R.string.text_3d), getString(R.string.illustrator))
            )
            categoryDesign.setOnSelectionChangedListener { selected ->
                updateTechStacks(getString(R.string.design), selected)
            }

            // 인프라/기타
            categoryInfra.setCategory(
                getString(R.string.infra),
                listOf(getString(R.string.aws), getString(R.string.docker), getString(R.string.kubernetes), getString(R.string.git))
            )
            categoryInfra.setOnSelectionChangedListener { selected ->
                updateTechStacks(getString(R.string.infra), selected)
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