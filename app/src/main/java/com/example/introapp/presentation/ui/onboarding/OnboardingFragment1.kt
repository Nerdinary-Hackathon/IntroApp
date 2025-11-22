package com.example.introapp.presentation.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.introapp.R
import com.example.introapp.databinding.FragmentOnboarding1Binding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class OnboardingFragment1 : Fragment() {
    private var _binding: FragmentOnboarding1Binding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboarding1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            layoutTop.bind(1, "기본 정보", "기본 정보를 입력해주세요")
            layoutName.bind(titleShow = true, title = "이름", placeholder = "실명을 기재해주세요.")
            layoutNickName.bind(titleShow = true, title = "닉네임", placeholder = "닉네임을 기재해주세요.")
            layoutPhone.bind(titleShow = true, title = "전화번호", placeholder = "전화번호를 기재해주세요.")
            layoutEmail.bind(titleShow = true, title = "이메일", placeholder = "이메일을 기재해주세요.")
            layoutLink.bind(titleShow = true, title = "링크", placeholder = "본인을 표현할 수 있는 링크를 기재해주세요.")

            btnGoTo2.setOnClickListener {
                // 화면 이동
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = OnboardingFragment1()
    }
}