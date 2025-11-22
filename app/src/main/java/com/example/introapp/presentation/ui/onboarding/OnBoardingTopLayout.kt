package com.example.introapp.presentation.ui.onboarding

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.introapp.databinding.LayoutOnboardingTopBinding

class OnBoardingTopLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val binding: LayoutOnboardingTopBinding = LayoutOnboardingTopBinding.inflate(
        LayoutInflater.from(context), this,  true
    )

    fun bind(currentIdx: Int, title: String, description: String) {
        binding.apply {
            tvIndex.text = "{$currentIdx}/4"
            tvTitle.text = title
            tvDescription.text = description
        }
    }
}