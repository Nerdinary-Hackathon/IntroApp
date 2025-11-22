package com.example.introapp.domain.entity

/**
 * 프로필 도메인 엔티티
 */
data class Profile(
    val name: String,
    val nickname: String,
    val phone: String,
    val email: String,
    val link: String,
    val jobGroup: JobGroup,
    val level: Level,
    val techStacks: List<TechStack>
)

/**
 * 직군
 */
enum class JobGroup(val value: String, val displayName: String) {
    PM("PM", "기획자"),
    DESIGNER("DESIGNER", "디자이너"),
    WEB("WEB", "웹 개발자"),
    BACKEND("BACKEND", "백엔드 개발자"),
    ANDROID("ANDROID", "안드로이드 개발자"),
    IOS("IOS", "iOS 개발자");

    companion object {
        fun from(value: String): JobGroup {
            return entries.find { it.value == value }
                ?: throw IllegalArgumentException("Unknown JobGroup: $value")
        }
    }
}

/**
 * 경력 레벨
 */
enum class Level(val value: String, val displayName: String) {
    JOB_SEEKING("JOB_SEEKING", "취업 준비"),
    JUNIOR("JUNIOR", "주니어"),
    SENIOR("SENIOR", "시니어");

    companion object {
        fun from(value: String): Level {
            return entries.find { it.value == value }
                ?: throw IllegalArgumentException("Unknown Level: $value")
        }
    }
}

/**
 * 기술 스택
 */
enum class TechStack(val value: String) {
    REACT("REACT"),
    VUE("VUE"),
    ANGULAR("ANGULAR"),
    SPRING("SPRING"),
    DJANGO("DJANGO"),
    KOTLIN("KOTLIN"),
    JAVA("JAVA"),
    SWIFT("SWIFT"),
    FLUTTER("FLUTTER");

    companion object {
        fun from(value: String): TechStack {
            return entries.find { it.value == value }
                ?: throw IllegalArgumentException("Unknown TechStack: $value")
        }
    }
}