package com.example.introapp.presentation.ui.detail

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.introapp.R
import com.example.introapp.databinding.ActivityCardDetailBinding
import com.example.introapp.domain.entity.Card
import com.example.introapp.presentation.viewmodel.UiState
import com.example.introapp.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class CardDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardDetailBinding
    private val userViewModel: UserViewModel by viewModels()

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCardDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cardDetailMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userId = intent.getIntExtra("userId", -1)
        Timber.e("## [상세화면] userId : $userId")

        userViewModel.getCard(userId.toString())

        lifecycleScope.launch {
            userViewModel.cardState.collect { state ->
                when (state) {
                    is UiState.Error -> {
                        Timber.e("## [명함 조회] Error - ${state.message}")
                        Toast.makeText(this@CardDetailActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    UiState.Idle -> {
                        Timber.d("## [명함 조회] Idle")
                    }
                    UiState.Loading -> {
                        Timber.d("## [명함 조회] Loading")
                    }
                    is UiState.Success -> {
                        val card = state.data
                        Timber.d("## [명함 조회] 성공 - card : $card")
                        updateCardUI(card)
                    }
                }
            }
        }
    }

    private fun updateCardUI(card: Card) {
        binding.run {
            tvName.text = card.nickname
            tvJob.text = card.jobGroup.toString()
            llTech.removeAllViews()
            card.techStacks.forEachIndexed { index, techStack ->
                val textView = createTechStackTextView(techStack.toString(), index)
                llTech.addView(textView)
            }
            tvPhone.text = card.phoneNumber
            tvEmail.text = card.email
            tvLink.text = card.link
        }
    }

    /**
     * 기술스택 TextView 동적 생성 함수
     *
     * @param text 표시할 기술스택 텍스트
     * @param index TextView의 인덱스 (첫 번째 항목은 marginStart 없음)
     */
    private fun createTechStackTextView(text: String, index: Int): TextView {
        return TextView(this).apply {
            // 텍스트 설정
            this.text = text

            // 레이아웃 파라미터 설정
            layoutParams = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT
            ).apply {
                // 첫 번째 항목이 아니면 왼쪽 여백 추가
                if (index > 0) {
                    marginStart = resources.getDimensionPixelSize(
                        R.dimen.tech_stack_margin
                    )
                }
            }

            // 스타일 설정
            setBackgroundResource(R.drawable.rounded_bg)
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 16f
            gravity = android.view.Gravity.CENTER
            maxLines = 1
            minWidth = resources.getDimensionPixelSize(R.dimen.tech_stack_min_width)
            minHeight = resources.getDimensionPixelSize(R.dimen.tech_stack_min_height)

            // 패딩 설정 (horizontal: 10dp, vertical: 6dp)
            val paddingHorizontal = resources.getDimensionPixelSize(
                R.dimen.tech_stack_padding_horizontal
            )
            val paddingVertical = resources.getDimensionPixelSize(
                R.dimen.tech_stack_padding_vertical
            )
            setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)

            // 폰트 설정
            typeface = resources.getFont(R.font.pretendard_medium)
        }
    }
}