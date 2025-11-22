package com.example.introapp.presentation.ui.onboarding

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.introapp.R
import com.example.introapp.databinding.FragmentOnboarding1Binding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class OnboardingFragment1 : Fragment() {
    private var _binding: FragmentOnboarding1Binding? = null
    private val binding
        get() = _binding!!

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
            layoutName.bind(
                titleShow = true,
                title = getString(R.string.name),
                placeholder = getString(R.string.name_input_alert)
            )
            layoutNickName.bind(
                titleShow = true,
                title = getString(R.string.nickname),
                placeholder = getString(R.string.nickname_input_alert)
            )
            layoutPhone.bind(
                titleShow = true,
                title = getString(R.string.phone),
                placeholder = getString(R.string.phone_input_alert),
                inputType = InputType.TYPE_CLASS_PHONE,
                enablePhoneFormatter = true
            )
            layoutEmail.bind(
                titleShow = true,
                title = getString(R.string.email),
                placeholder = getString(R.string.email_input_alert),
                inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS or InputType.TYPE_CLASS_TEXT
            )
            layoutLink.bind(
                titleShow = true,
                title = getString(R.string.link),
                placeholder = getString(R.string.link_input_alert),
                inputType = InputType.TYPE_TEXT_VARIATION_URI or InputType.TYPE_CLASS_TEXT
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