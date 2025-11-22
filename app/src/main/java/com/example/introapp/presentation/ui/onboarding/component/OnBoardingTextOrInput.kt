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

    // 입력값 변경 리스너
    private var onTextChangedListener: ((String) -> Unit)? = null

    fun bind(
        titleShow: Boolean,
        title: String,
        placeholder: String,
        inputType: Int = InputType.TYPE_CLASS_TEXT,
        enablePhoneFormatter: Boolean = false,
        onTextChanged: ((String) -> Unit)? = null
    ) {
        binding.apply {
            tvName.visibility = if (titleShow) VISIBLE else GONE
            tvName.text = title
            tvOnboardingInput.hint = placeholder
            tvOnboardingInput.inputType = inputType

            // 리스너 저장
            onTextChangedListener = onTextChanged

            // 기존 TextWatcher 제거 (중복 방지)
            tvOnboardingInput.removeTextChangedListener(phoneTextWatcher)
            tvOnboardingInput.removeTextChangedListener(generalTextWatcher)

            // 전화번호 포맷팅이 필요한 경우
            if (enablePhoneFormatter) {
                tvOnboardingInput.addTextChangedListener(phoneTextWatcher)
            } else {
                // 일반 TextWatcher
                tvOnboardingInput.addTextChangedListener(generalTextWatcher)
            }
        }
    }

    // 일반 TextWatcher
    private val generalTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            onTextChangedListener?.invoke(s.toString())
        }
    }

    // 전화번호 포맷팅을 위한 TextWatcher
    private val phoneTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (isFormatting) return

            isFormatting = true

            val text = s.toString()
            val digitsOnly = text.replace("-", "")
            val formatted = formatPhoneNumber(digitsOnly)

            if (formatted != text) {
                binding.tvOnboardingInput.setText(formatted)
                binding.tvOnboardingInput.setSelection(formatted.length)
            }

            // 포맷팅 후 리스너 호출 (하이픈 포함된 포맷된 값 전달)
            onTextChangedListener?.invoke(formatted)

            isFormatting = false
        }
    }

    // 전화번호 포맷팅 로직: 010-1234-5678 형태로 변환
    private fun formatPhoneNumber(digits: String): String {
        return when {
            digits.length <= 3 -> digits
            digits.length <= 7 -> "${digits.substring(0, 3)}-${digits.substring(3)}"
            digits.length <= 11 -> "${digits.substring(0, 3)}-${digits.substring(3, 7)}-${digits.substring(7)}"
            else -> "${digits.substring(0, 3)}-${digits.substring(3, 7)}-${digits.substring(7, 11)}"
        }
    }

    /**
     * 현재 입력된 텍스트 반환
     */
    fun getText(): String {
        return binding.tvOnboardingInput.text.toString()
    }
}