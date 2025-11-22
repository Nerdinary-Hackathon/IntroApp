package com.example.introapp.presentation.ui.onboarding

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.introapp.R
import com.example.introapp.databinding.FragmentOnboarding1Binding
import com.example.introapp.presentation.viewmodel.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class OnboardingFragment1 : Fragment() {
    private var _binding: FragmentOnboarding1Binding? = null
    private val binding
        get() = _binding!!

    private val viewModel: OnBoardingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboarding1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            layoutTop.bind(
                1,
                getString(R.string.onboarding1_title),
                getString(R.string.onboarding1_desc)
            )

            // 이름
            layoutName.bind(
                titleShow = true,
                title = getString(R.string.name),
                placeholder = getString(R.string.name_input_alert),
                onTextChanged = { text ->
                    viewModel.updateName(text)
                }
            )

            // 닉네임
            layoutNickName.bind(
                titleShow = true,
                title = getString(R.string.nickname),
                placeholder = getString(R.string.nickname_input_alert),
                onTextChanged = { text ->
                    viewModel.updateNickname(text)
                }
            )

            // 전화번호
            layoutPhone.bind(
                titleShow = true,
                title = getString(R.string.phone),
                placeholder = getString(R.string.phone_input_alert),
                inputType = InputType.TYPE_CLASS_PHONE,
                enablePhoneFormatter = true,
                onTextChanged = { text ->
                    viewModel.updatePhone(text)
                }
            )

            // 이메일
            layoutEmail.bind(
                titleShow = true,
                title = getString(R.string.email),
                placeholder = getString(R.string.email_input_alert),
                inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS or InputType.TYPE_CLASS_TEXT,
                onTextChanged = { text ->
                    viewModel.updateEmail(text)
                }
            )

            // 링크
            layoutLink.bind(
                titleShow = true,
                title = getString(R.string.link),
                placeholder = getString(R.string.link_input_alert),
                inputType = InputType.TYPE_TEXT_VARIATION_URI or InputType.TYPE_CLASS_TEXT,
                onTextChanged = { text ->
                    viewModel.updateLink(text)
                }
            )

            btnGoTo2.setOnClickListener {
                findNavController().navigate(R.id.action_page1_to_page2)
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