package com.example.introapp.presentation.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.introapp.R
import com.example.introapp.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 뷰바인딩 초기화
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //첫화면을 명함으로 설정
        supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrm, CardFragment())
            .commit()

        //상단 두 버튼에 클릭 리스너 적용
        binding.tabs.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.tab_card -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.homeFrm, CardFragment())
                            .commit()
                    }
                    R.id.tab_code -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.homeFrm, CodeFragment())
                            .commit()
                    }
                }
            }
        }


        // WindowInsets 적용
        ViewCompat.setOnApplyWindowInsetsListener(binding.homeMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
