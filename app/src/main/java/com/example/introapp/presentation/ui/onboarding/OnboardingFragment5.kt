package com.example.introapp.presentation.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.introapp.R
import com.example.introapp.databinding.FragmentOnboarding5Binding
import com.example.introapp.presentation.ui.MainActivity

class OnboardingFragment5 : Fragment() {

    private var _binding: FragmentOnboarding5Binding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboarding5Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            btnGoToMain.setOnClickListener {
                startActivity(
                    Intent(requireActivity(), MainActivity::class.java)
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = OnboardingFragment5()
    }
}