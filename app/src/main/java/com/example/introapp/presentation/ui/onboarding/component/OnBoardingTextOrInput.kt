package com.example.introapp.presentation.ui.onboarding.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.introapp.databinding.LayoutOnboardingTextOrInputBinding

class OnBoardingTextOrInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val binding: LayoutOnboardingTextOrInputBinding =
        LayoutOnboardingTextOrInputBinding.inflate(
            LayoutInflater.from(context), this, true
        )

    fun bind(
        titleShow: Boolean,
        title: String,
        placeholder: String,
    ) {
        binding.apply {
            tvName.visibility = if (titleShow) VISIBLE else GONE
            tvName.text = title
            tvOnboardingInput.hint = placeholder
        }
    }

}