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
    fun toggleTechStack(tech: String) {
        _onboardingData.update { current ->
            val currentList = current.page3.selectedTechStacks.toMutableList()
            if (currentList.contains(tech)) {
                currentList.remove(tech)
            } else {
                currentList.add(tech)
            }

            current.copy(
                page3 = current.page3.copy(
                    selectedTechStacks = currentList
                )
            )
        }
    }

    // 특정 기술이 선택되어 있는지 확인
    fun isTechStackSelected(tech: String): Boolean {
        return _onboardingData.value.page3.selectedTechStacks.contains(tech)
    }

    // 페이지 3 전체 업데이트
    fun updateTechStacks(techStacks: List<String>) {
        _onboardingData.update { it.copy(page3 = it.page3.copy(selectedTechStacks = techStacks)) }
    }
}

data class OnboardingData(
    val page1: Page1Data = Page1Data(),
    val page2: Page2Data = Page2Data(),
    val page3: Page3Data = Page3Data(),
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
    val selectedTechStacks: List<String> = emptyList()
)

//data class Page3Data(
//    val frontend: List<String> = emptyList(),
//    val backend: List<String> = emptyList(),
//    val design: List<String> = emptyList(),
//    val infra: List<String> = emptyList()
//)