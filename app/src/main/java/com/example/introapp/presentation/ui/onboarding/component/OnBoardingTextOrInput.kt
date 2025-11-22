package com.example.introapp.presentation.ui.onboarding.component

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
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

    // 무한 루프 방지 플래그
    private var isFormatting = false

    fun bind(
        titleShow: Boolean,
        title: String,
        placeholder: String,
        inputType: Int = InputType.TYPE_CLASS_TEXT, // 기본값: 일반 텍스트
        enablePhoneFormatter: Boolean = false // 전화번호 포맷팅 활성화 여부
    ) {
        binding.apply {
            tvName.visibility = if (titleShow) VISIBLE else GONE
            tvName.text = title
            tvOnboardingInput.hint = placeholder
            tvOnboardingInput.inputType = inputType

            // 기존 TextWatcher 제거 (중복 방지)
            tvOnboardingInput.removeTextChangedListener(phoneTextWatcher)

            // 전화번호 포맷팅이 필요한 경우 TextWatcher 추가
            if (enablePhoneFormatter) {
                tvOnboardingInput.addTextChangedListener(phoneTextWatcher)
            }
        }
    }

    // 전화번호 포맷팅을 위한 TextWatcher
    private val phoneTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (isFormatting) return // 포맷팅 중이면 중단 (무한 루프 방지)

            isFormatting = true

            val text = s.toString()
            val digitsOnly = text.replace("-", "") // 숫자만 추출

            val formatted = formatPhoneNumber(digitsOnly)

            // 포맷팅된 텍스트가 현재 텍스트와 다르면 업데이트
            if (formatted != text) {
                binding.tvOnboardingInput.setText(formatted)
                binding.tvOnboardingInput.setSelection(formatted.length) // 커서를 끝으로 이동
            }

            isFormatting = false
        }
    }

    // 전화번호 포맷팅 로직: 010-1234-5678 형태로 변환
    private fun formatPhoneNumber(digits: String): String {
        return when {
            digits.length <= 3 -> digits
            digits.length <= 7 -> "${digits.substring(0, 3)}-${digits.substring(3)}"
            digits.length <= 11 -> "${digits.substring(0, 3)}-${digits.substring(3, 7)}-${digits.substring(7)}"
            else -> "${digits.substring(0, 3)}-${digits.substring(3, 7)}-${digits.substring(7, 11)}" // 최대 11자리
        }
    }
}