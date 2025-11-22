package com.example.introapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor() : ViewModel() {
    private val _onboardingData = MutableStateFlow(OnboardingData())
    val onboardingData: StateFlow<OnboardingData> = _onboardingData.asStateFlow()

    // 1페이지
    fun updateName(value: String) {
        _onboardingData.update { it.copy(page1 = it.page1.copy(name = value)) }
    }

    fun updateNickname(value: String) {
        _onboardingData.update { it.copy(page1 = it.page1.copy(nickname = value)) }
    }

    fun updatePhone(value: String) {
        _onboardingData.update { it.copy(page1 = it.page1.copy(phone = value)) }
    }

    fun updateEmail(value: String) {
        _onboardingData.update { it.copy(page1 = it.page1.copy(email = value)) }
    }

    fun updateLink(value: String) {
        _onboardingData.update { it.copy(page1 = it.page1.copy(link = value)) }
    }

    // 2페이지
    fun updateJob(value: String) {
        _onboardingData.update { it.copy(page2 = it.page2.copy(job = value)) }
    }

    // 3페이지 - 기술 스택 추가/제거
    fun updateTechStacks(techStacks: Set<String>) {
        _onboardingData.update { it.copy(page3 = it.page3.copy(selectedTechStacks = techStacks)) }
    }

    // 4페이지 - 경력
    fun updateCareer(value: String) {
        _onboardingData.update { it.copy(page4 = it.page4.copy(career = value)) }
    }
}

data class OnboardingData(
    val page1: Page1Data = Page1Data(),
    val page2: Page2Data = Page2Data(),
    val page3: Page3Data = Page3Data(),
    val page4: Page4Data = Page4Data(),
)

data class Page1Data(
    val name: String = "",
    val nickname: String = "",
    val phone: String = "",
    val email: String = "",
    val link: String = "",
)

data class Page2Data(
    val job: String = ""
)

data class Page3Data(
    val selectedTechStacks: Set<String> = emptySet()
)

data class Page4Data(
    val career: String = ""
)

//data class Page3Data(
//    val frontend: List<String> = emptyList(),
//    val backend: List<String> = emptyList(),
//    val design: List<String> = emptyList(),
//    val infra: List<String> = emptyList()
//)