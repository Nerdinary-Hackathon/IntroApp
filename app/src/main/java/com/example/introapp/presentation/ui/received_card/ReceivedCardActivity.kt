package com.example.introapp.presentation.ui.received_card

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.introapp.R
import com.example.introapp.databinding.ActivityReceivedCardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReceivedCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReceivedCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReceivedCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.receivedCardMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.run {
            ivArrowLeft.setOnClickListener { finish() }

            categoryFilter.apply {
                setSingleSelectionMode(true) // 단일 선택 모드 활성화
                setCategory(
                    title = "",
                    items = listOf("전체", "PM", "디자인", "웹", "백엔드", "안드로이드", "iOS")
                )
                // 기본값으로 "전체" 선택
                setSelectedItems(setOf("전체"))
            }
        }
    }
}