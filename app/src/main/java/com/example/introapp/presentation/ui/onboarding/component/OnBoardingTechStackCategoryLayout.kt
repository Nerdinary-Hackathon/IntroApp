package com.example.introapp.presentation.ui.onboarding.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.introapp.R
import com.example.introapp.databinding.LayoutTechStackCategoryBinding

class OnBoardingTechStackCategoryLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val binding: LayoutTechStackCategoryBinding =
        LayoutTechStackCategoryBinding.inflate(
            LayoutInflater.from(context), this, true
        )

    private val selectedItems = mutableSetOf<String>()
    private var onSelectionChangedListener: ((Set<String>) -> Unit)? = null

    // 단일 선택 모드 플래그 추가
    private var isSingleSelectionMode = false

    // 단일 선택 모드 설정 메서드 추가
    fun setSingleSelectionMode(enabled: Boolean) {
        isSingleSelectionMode = enabled
    }

    // 카테고리 설정
    fun setCategory(title: String, items: List<String>) {
        if (title.isEmpty()) {
            binding.tvCategoryTitle.visibility = GONE
        } else {
            binding.tvCategoryTitle.visibility = VISIBLE
        }
        binding.tvCategoryTitle.text = title
        binding.chipGroup.removeAllViews()
        selectedItems.clear()

        items.forEach { item ->
            val chip = createChip(item)
            binding.chipGroup.addView(chip)
        }
    }

    private fun createChip(text: String): View {
        val chipView = LayoutInflater.from(context)
            .inflate(R.layout.item_onboarding_chip, binding.chipGroup, false)

        val textView = chipView.findViewById<TextView>(R.id.tvChip)
        textView.text = text

        // 초기 상태 : 선택 안됨
        chipView.isSelected = false

        // 클릭 리스너
        chipView.setOnClickListener {
            toggleChip(chipView, text)
        }

        return chipView
    }

    private fun toggleChip(view: View, value: String) {
        if (isSingleSelectionMode) {
            // 단일 선택 모드: 이전 선택 해제 후 새로운 항목 선택
            val previouslySelected = selectedItems.firstOrNull()

            // 같은 항목을 다시 클릭한 경우 (선택 해제 방지)
            if (selectedItems.contains(value)) {
                return
            }

            // 모든 칩 선택 해제
            for (i in 0 until binding.chipGroup.childCount) {
                binding.chipGroup.getChildAt(i).isSelected = false
            }
            selectedItems.clear()

            // 새로운 칩 선택
            view.isSelected = true
            selectedItems.add(value)
        } else {
            // 다중 선택 모드: 기존 로직
            val isSelected = !view.isSelected
            view.isSelected = isSelected

            if (isSelected) {
                selectedItems.add(value)
            } else {
                selectedItems.remove(value)
            }
        }

        // 리스너 호출
        onSelectionChangedListener?.invoke(selectedItems.toSet())
    }

    // 선택 리스너 설정
    fun setOnSelectionChangedListener(listener: (Set<String>) -> Unit) {
        onSelectionChangedListener = listener
    }

    // 현재 선택된 항목들 가져오기
    fun getSelectedItems(): Set<String> = selectedItems.toSet()

    // 프로그래밍 방식으로 선택 설정 (리스너 호출 없음)
    fun setSelectedItems(items: Set<String>) {
        selectedItems.clear()
        selectedItems.addAll(items)

        // UI 업데이트
        for (i in 0 until binding.chipGroup.childCount) {
            val chip = binding.chipGroup.getChildAt(i)
            val textView = chip.findViewById<TextView>(R.id.tvChip)
            val text = textView.text.toString()
            chip.isSelected = selectedItems.contains(text)
        }
    }

    /**
     * 리스너 호출 없이 UI만 업데이트
     * 3개 제한에 걸렸을 때 마지막 클릭을 취소하기 위해 사용
     */
    fun setSelectedItemsSilently(items: Set<String>) {
        selectedItems.clear()
        selectedItems.addAll(items)

        // UI 업데이트
        for (i in 0 until binding.chipGroup.childCount) {
            val chip = binding.chipGroup.getChildAt(i)
            val textView = chip.findViewById<TextView>(R.id.tvChip)
            val text = textView.text.toString()
            chip.isSelected = selectedItems.contains(text)
        }
    }

    // 선택 초기화
    fun clearSelection() {
        selectedItems.clear()
        for (i in 0 until binding.chipGroup.childCount) {
            binding.chipGroup.getChildAt(i).isSelected = false
        }
    }
}