package com.example.introapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.introapp.databinding.FragmentCodeBinding
import com.example.introapp.presentation.viewmodel.OnBoardingViewModel
import com.example.introapp.presentation.viewmodel.UiState
import com.example.introapp.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

// 나의 코드
@AndroidEntryPoint
class CodeFragment : Fragment() {
    private lateinit var binding: FragmentCodeBinding
    private val onBoardingViewModel: OnBoardingViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    private var currentUserId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            btnExchangeCard.setOnClickListener {
                currentUserId?.let { userId ->
                    val cardCode = etFriendCode.text.toString()
                    userViewModel.exchangeCard(userId, cardCode)
                } ?: run {
                    Toast.makeText(requireContext(), "사용자 ID를 불러오는 중입니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    onBoardingViewModel.getSavedUserId().collect { userId ->
                        if (userId != null) {
                            currentUserId = userId.toString()
                            binding.tvCode.text = currentUserId
                        }
                    }
                }

                launch {
                    userViewModel.exchangeState.collect { state ->
                        when (state) {
                            is UiState.Error -> {
                                Timber.e("## [명함 교환] 에러 : ${state.message}")
                                Toast.makeText(requireContext(), "이미 추가한 명함입니다", Toast.LENGTH_SHORT).show()
                            }
                            UiState.Idle -> {
                                // 초기 상태
                            }
                            UiState.Loading -> {
                                // 로딩 중 - 필요시 프로그레스바 표시
                            }
                            is UiState.Success -> {
                                Toast.makeText(requireActivity(), "명함 교환에 성공했습니다!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}