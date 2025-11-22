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
import com.example.introapp.databinding.FragmentOnboarding3Binding
import com.example.introapp.presentation.ui.onboarding.component.OnBoardingTechStackCategoryLayout
import com.example.introapp.presentation.viewmodel.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment3 : Fragment() {

    private var _binding: FragmentOnboarding3Binding? = null
    private val binding get() = _binding!!

    private val viewModel: OnBoardingViewModel by activityViewModels()

    // 전체 선택된 기술 스택
    private val allSelectedTechStacks = mutableSetOf<String>()

    // 최대 선택 가능 개수
    private val maxSelectionCount = 3

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
            layoutTop.bind(
                3,
                getString(R.string.onboarding3_title),
                getString(R.string.onboarding3_desc)
            )

            // 프론트엔드
            categoryFrontend.setCategory(
                getString(R.string.frontend),
                listOf(
                    getString(R.string.react),
                    getString(R.string.vue),
                    getString(R.string.nextjs),
                    getString(R.string.typescript),
                    getString(R.string.flutter)
                )
            )
            categoryFrontend.setOnSelectionChangedListener { selected ->
                updateTechStacks(getString(R.string.frontend), selected, categoryFrontend)
            }

            // 백엔드
            categoryBackend.setCategory(
                getString(R.string.backend),
                listOf(
                    getString(R.string.java),
                    getString(R.string.spring),
                    getString(R.string.python),
                    getString(R.string.django),
                    getString(R.string.nodejs)
                )
            )
            categoryBackend.setOnSelectionChangedListener { selected ->
                updateTechStacks(getString(R.string.backend), selected, categoryBackend)
            }

            // 디자인
            categoryDesign.setCategory(
                getString(R.string.design),
                listOf(
                    getString(R.string.figma),
                    getString(R.string.photoshop),
                    getString(R.string.sketch),
                    getString(R.string.text_3d),
                    getString(R.string.illustrator)
                )
            )
            categoryDesign.setOnSelectionChangedListener { selected ->
                updateTechStacks(getString(R.string.design), selected, categoryDesign)
            }

            // 인프라/기타
            categoryInfra.setCategory(
                getString(R.string.infra),
                listOf(
                    getString(R.string.aws),
                    getString(R.string.docker),
                    getString(R.string.kubernetes),
                    getString(R.string.git)
                )
            )
            categoryInfra.setOnSelectionChangedListener { selected ->
                updateTechStacks(getString(R.string.infra), selected, categoryInfra)
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

    /**
     * 기술 스택 선택을 업데이트하고 최대 선택 개수 체크
     * @param category 카테고리 이름
     * @param selected 선택된 항목들
     * @param categoryView 해당 카테고리 뷰 (선택 복원용)
     */
    private fun updateTechStacks(
        category: String,
        selected: Set<String>,
        categoryView: OnBoardingTechStackCategoryLayout
    ) {
        // 해당 카테고리의 기존 선택 항목들
        val currentCategoryItems = allSelectedTechStacks.filter {
            getTechStacksByCategory(category).contains(it)
        }.toSet()

        // 기존 선택에서 해당 카테고리 항목을 제외한 개수
        val otherCategoriesCount = allSelectedTechStacks.size - currentCategoryItems.size

        // 새로운 전체 선택 개수
        val newTotalCount = otherCategoriesCount + selected.size

        // 최대 선택 개수 초과 체크
        if (newTotalCount > maxSelectionCount) {
            Toast.makeText(requireContext(), "분야는 3개만 선택할 수 있어요", Toast.LENGTH_SHORT).show()
            // 이전 선택 상태로 복원 (리스너 호출 없이)
            categoryView.setSelectedItemsSilently(currentCategoryItems)
            return
        }

        // 해당 카테고리의 기존 선택 항목 제거
        allSelectedTechStacks.removeAll(getTechStacksByCategory(category))

        // 새로운 선택 항목 추가
        allSelectedTechStacks.addAll(selected)

        // ViewModel 업데이트
        viewModel.updateTechStacks(allSelectedTechStacks)
    }

    /**
     * 카테고리에 해당하는 모든 기술 스택 리스트 리턴
     */
    private fun getTechStacksByCategory(category: String): List<String> {
        return when (category) {
            getString(R.string.frontend) -> listOf(
                getString(R.string.react),
                getString(R.string.vue),
                getString(R.string.nextjs),
                getString(R.string.typescript),
                getString(R.string.flutter)
            )

            getString(R.string.backend) -> listOf(
                getString(R.string.java),
                getString(R.string.spring),
                getString(R.string.python),
                getString(R.string.django),
                getString(R.string.nodejs)
            )

            getString(R.string.design) -> listOf(
                getString(R.string.figma),
                getString(R.string.photoshop),
                getString(R.string.sketch),
                getString(R.string.text_3d),
                getString(R.string.illustrator)
            )

            getString(R.string.infra) -> listOf(
                getString(R.string.aws),
                getString(R.string.docker),
                getString(R.string.kubernetes),
                getString(R.string.git)
            )

            else -> emptyList()
        }
    }

    /**
     * 이전에 선택한 기술 스택들을 각 카테고리 뷰에 복원
     */
    private fun restoreSelections(techStacks: Set<String>) {
        binding.run {
            val frontend = techStacks.filter {
                getTechStacksByCategory(getString(R.string.frontend)).contains(it)
            }.toSet()
            val backend = techStacks.filter {
                getTechStacksByCategory(getString(R.string.backend)).contains(it)
            }.toSet()
            val design = techStacks.filter {
                getTechStacksByCategory(getString(R.string.design)).contains(it)
            }.toSet()
            val infra = techStacks.filter {
                getTechStacksByCategory(getString(R.string.infra)).contains(it)
            }.toSet()

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