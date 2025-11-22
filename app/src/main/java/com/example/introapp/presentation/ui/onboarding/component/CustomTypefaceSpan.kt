package com.example.introapp.presentation.ui.onboarding.component

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

/**
 * 커스텀 폰트를 적용하기 위한 Span 클래스
 *
 * 기본 TypefaceSpan은 폰트 리소스를 직접 쓸 수 없어서 커스텀 구현 필요
 */
class CustomTypefaceSpan(private val typeface: Typeface?) : MetricAffectingSpan() {

    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeFace(ds, typeface)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, typeface)
    }

    private fun applyCustomTypeFace(paint: Paint, tf: Typeface?) {
        tf?.let {
            paint.typeface = it
        }
    }
}