package com.example.introapp.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.introapp.R
import com.example.introapp.databinding.ActivityHomeBinding
import com.example.introapp.presentation.ui.received_card.ReceivedCardActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private var backPressedTime: Long = 0
    private val backPressInterval: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true  // 상태 바 아이콘/텍스트를 어둡게
        }
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackPressHandler()

        // 첫 화면을 명함으로 설정
        supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrm, CardFragment())
            .commit()

        binding.tvTabCard.setOnClickListener {
            selectTab(true)  // 내 명함 탭 선택
            supportFragmentManager.beginTransaction()
                .replace(R.id.homeFrm, CardFragment())
                .commit()
        }

        binding.tvTabCode.setOnClickListener {
            selectTab(false)  // 나의 코드 탭 선택
            supportFragmentManager.beginTransaction()
                .replace(R.id.homeFrm, CodeFragment())
                .commit()
        }

        binding.ivConvertCard.setOnClickListener {
            showCardMenuPopup(it)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.homeMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /**
     * 뒤로가기 버튼을 두 번 눌러야 종료되도록 설정
     */
    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 현재 시간과 이전에 뒤로가기를 누른 시간의 차이 계산
                if (System.currentTimeMillis() - backPressedTime <= backPressInterval) {
                    // 2초 이내에 다시 뒤로가기를 누르면 앱 종료
                    finish()
                } else {
                    // 첫 번째 뒤로가기: 토스트 메시지 표시
                    backPressedTime = System.currentTimeMillis()
                    Toast.makeText(
                        this@HomeActivity,
                        "종료하려면 한 번 더 뒤로가기를 누르세요",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    /**
     * 탭 선택 상태를 변경하는 메서드
     * @param isCardTab true면 명함 탭, false면 코드 탭을 선택
     */
    private fun selectTab(isCardTab: Boolean) {
        if (isCardTab) {
            // 명함 탭 선택: 배경 적용, 글자색 진하게
            binding.tvTabCard.setBackgroundResource(R.drawable.bg_tab_selected)
            binding.tvTabCard.setTextColor(getColor(R.color.color_0f0f10))
            // 코드 탭 비선택: 배경 제거, 글자색 연하게
            binding.tvTabCode.background = null
            binding.tvTabCode.setTextColor(getColor(R.color.color_0f0f10_40))
        } else {
            // 코드 탭 선택: 배경 적용, 글자색 진하게
            binding.tvTabCode.setBackgroundResource(R.drawable.bg_tab_selected)
            binding.tvTabCode.setTextColor(getColor(R.color.color_0f0f10))
            // 명함 탭 비선택: 배경 제거, 글자색 연하게
            binding.tvTabCard.background = null
            binding.tvTabCard.setTextColor(getColor(R.color.color_0f0f10_40))
        }
    }

    private fun showCardMenuPopup(anchorView: View) {
        // 팝업 레이아웃 인플레이트
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_card_menu, null)

        // PopupWindow 생성
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true // 외부 터치 시 닫히도록 설정
        )

        // 팝업 외부 터치 시 닫히게 설정
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true

        // 메뉴 아이템 클릭 리스너 설정
        val menuMyCard = popupView.findViewById<TextView>(R.id.menu_my_card)
        val menuReceivedCard = popupView.findViewById<TextView>(R.id.menu_received_card)

        // "내 명함" 클릭
        menuMyCard.setOnClickListener {
            popupWindow.dismiss()
        }

        // "받은명함" 클릭
        menuReceivedCard.setOnClickListener {
            popupWindow.dismiss()
            startActivity(
                Intent(this, ReceivedCardActivity::class.java)
            )
        }

        // 팝업 표시 위치 계산 (앵커뷰 아래 오른쪽에 정렬)
        popupWindow.showAsDropDown(
            anchorView,
            -100, // x 오프셋 (왼쪽으로 이동)
            10    // y 오프셋 (아래로 약간 이동)
        )
    }

    /**
     * 로딩 오버레이 표시 (Fragment에서 호출 가능하도록 public)
     */
    fun showLoading() {
        binding.loadingOverlay.visibility = View.VISIBLE
    }

    /**
     * 로딩 오버레이 숨김 (Fragment에서 호출 가능하도록 public)
     */
    fun hideLoading() {
        binding.loadingOverlay.visibility = View.GONE
    }
}
