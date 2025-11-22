package com.example.introapp.presentation.ui.onboarding.component

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
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

    fun bind(
        currentIdx: Int,
        title: String,
        description: String
    ) {
        binding.apply {
            val indexText = "$currentIdx / 4"
            val spannableString = SpannableString(indexText)
            val currentIdxString = currentIdx.toString()

            // currentIdx만 볼드
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                currentIdxString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            tvIndex.text = spannableString
            tvTitle.text = title
            tvDescription.text = description
        }
    }
}