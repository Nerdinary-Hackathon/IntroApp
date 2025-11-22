package com.example.introapp.presentation.ui.onboarding.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.introapp.R
import com.example.introapp.databinding.LayoutOnboardingJobSelectorBinding

class OnBoardingJobSelector @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val binding: LayoutOnboardingJobSelectorBinding =
        LayoutOnboardingJobSelectorBinding.inflate(
            LayoutInflater.from(context), this, true
        )

    private var onItemSelectedListener: ((String) -> Unit)? = null
    private var selectedView: View? = null
    private val itemViews = mutableListOf<Pair<View, String>>()

    // 선택 가능한 아이템들 설정
    fun setItems(items: List<String>) {
        binding.containerLayout.removeAllViews()
        itemViews.clear()
        selectedView = null

        items.forEachIndexed { index, item ->
            val itemView = createItemView(item, isLast = index == items.size - 1)
            binding.containerLayout.addView(itemView)
            itemViews.add(itemView to item)
        }
    }

    private fun createItemView(text: String, isLast: Boolean): View {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.item_onboarding_selector, binding.containerLayout, false)

        val textView = itemView.findViewById<TextView>(R.id.tvItem)
        textView.text = text

        // selector drawable 적용
        itemView.background = ContextCompat.getDrawable(
            context,
            R.drawable.bg_onboarding_selector
        )

        // 초기 상태: 선택 안됨
        itemView.isSelected = false

        // 마지막 아이템은 하단 마진 제거
        if (isLast) {
            val layoutParams = itemView.layoutParams as MarginLayoutParams
            layoutParams.bottomMargin = 0
            itemView.layoutParams = layoutParams
        }

        // 클릭 리스너
        itemView.setOnClickListener {
            selectItem(itemView, text)
        }

        return itemView
    }

    private fun selectItem(view: View, value: String) {
        // 이전 선택 해제
        selectedView?.isSelected = false

        // 새로운 선택 설정
        view.isSelected = true
        selectedView = view

        // 리스너 호출
        onItemSelectedListener?.invoke(value)
    }

    // 선택 리스너 설정
    fun setOnItemSelectedListener(listener: (String) -> Unit) {
        onItemSelectedListener = listener
    }

    // 현재 선택된 값 가져오기
    fun getSelectedItem(): String? {
        return itemViews.find { it.first == selectedView }?.second
    }

    // 프로그래밍 방식으로 선택
    fun selectItemByValue(value: String) {
        itemViews.find { it.second == value }?.let { (view, selectedValue) ->
            selectItem(view, selectedValue)
        }
    }

    // 선택 초기화
    fun clearSelection() {
        selectedView?.isSelected = false
        selectedView = null
    }
}