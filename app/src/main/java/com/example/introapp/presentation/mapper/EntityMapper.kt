package com.example.introapp.presentation.mapper

import com.example.introapp.domain.entity.JobGroup
import com.example.introapp.domain.entity.Level
import com.example.introapp.domain.entity.TechStack

/**
 * UI displayName을 Domain entity value로 변환하는 Mapper
 */
object EntityMapper {

    /**
     * 직군 displayName을 value로 변환
     * @param displayName UI에 표시되는 한글 이름 (예: "백엔드")
     * @return API value (예: "BACKEND")
     */
    fun mapJobGroupDisplayNameToValue(displayName: String): String {
        return try {
            JobGroup.fromDisplayName(displayName).value
        } catch (e: IllegalArgumentException) {
            // fallback: 그대로 반환
            displayName
        }
    }

    /**
     * 경력 displayName을 value로 변환
     * @param displayName UI에 표시되는 한글 이름 (예: "주니어 (1~3년)")
     * @return API value (예: "JUNIOR")
     */
    fun mapLevelDisplayNameToValue(displayName: String): String {
        return try {
            Level.fromDisplayName(displayName).value
        } catch (e: IllegalArgumentException) {
            // fallback: 그대로 반환
            displayName
        }
    }

    /**
     * 기술 스택 displayName을 value로 변환
     * @param displayName UI에 표시되는 이름 (예: "Java")
     * @return API value (예: "JAVA")
     */
    fun mapTechStackDisplayNameToValue(displayName: String): String {
        return try {
            TechStack.fromDisplayName(displayName).value
        } catch (e: IllegalArgumentException) {
            // fallback: 그대로 반환
            displayName
        }
    }

    /**
     * 기술 스택 displayName 리스트를 value 리스트로 변환
     */
    fun mapTechStackDisplayNamesToValues(displayNames: Set<String>): Set<String> {
        return displayNames.map { mapTechStackDisplayNameToValue(it) }.toSet()
    }
}