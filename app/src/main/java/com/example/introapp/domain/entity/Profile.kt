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
    DESIGNER("DESIGNER", "디자인"),
    WEB("WEB", "웹"),
    BACKEND("BACKEND", "백엔드"),
    ANDROID("ANDROID", "안드로이드"),
    IOS("IOS", "iOS");

    companion object {
        /**
         * API value로 JobGroup 찾기 (예: "PM", "DESIGNER")
         */
        fun from(value: String): JobGroup {
            return entries.find { it.value.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown JobGroup: $value")
        }

        /**
         * 한글 displayName으로 JobGroup 찾기 (예: "기획자", "웹")
         */
        fun fromDisplayName(displayName: String): JobGroup {
            return entries.find { it.displayName == displayName }
                ?: throw IllegalArgumentException("Unknown JobGroup displayName: $displayName")
        }
    }
}

/**
 * 경력 레벨
 */
enum class Level(val value: String, val displayName: String) {
    JOB_SEEKING("JOB_SEEKING", "취준생"),
    NEWCOMER("NEWCOMER", "신입"),
    JUNIOR("JUNIOR", "주니어"),
    SENIOR("SENIOR", "시니어");

    companion object {
        /**
         * API value로 Level 찾기 (예: "JOB_SEEKING", "JUNIOR")
         */
        fun from(value: String): Level {
            return entries.find { it.value.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown Level: $value")
        }

        /**
         * 한글 displayName으로 Level 찾기 (예: "취준생", "주니어 (1~3년)")
         * 괄호 안의 내용은 무시하고 매칭
         */
        fun fromDisplayName(displayName: String): Level {
            // "주니어 (1~3년)" -> "주니어"로 정규화
            val normalized = displayName.split("(").firstOrNull()?.trim() ?: displayName
            return entries.find { it.displayName == normalized }
                ?: throw IllegalArgumentException("Unknown Level displayName: $displayName")
        }
    }
}

/**
 * 기술 스택
 *
 * displayName는 UI에 표시되는 텍스트와 다름
 */
enum class TechStack(val value: String, val displayName: String) {
    // Frontend
    REACT("REACT", "React"),
    VUE("VUE", "Vue"),
    NEXTJS("NEXTJS", "Next.js"),
    TYPESCRIPT("TYPESCRIPT", "TypeScript"),
    FLUTTER("FLUTTER", "Flutter"),

    // Backend
    JAVA("JAVA", "Java"),
    SPRING("SPRING", "Spring"),
    PYTHON("PYTHON", "Python"),
    DJANGO("DJANGO", "Django"),
    NODEJS("NODEJS", "Node.js"),

    // Design
    FIGMA("FIGMA", "Figma"),
    PHOTOSHOP("PHOTOSHOP", "Photoshop"),
    SKETCH("SKETCH", "Sketch"),
    THREED("3D", "3D"),
    ILLUSTRATOR("ILLUSTRATOR", "Illustrator"),

    // 인프라 / 기타
    AWS("AWS", "AWS"),
    DOCKER("DOCKER", "Docker"),
    KUBERNETES("KUBERNETES", "Kubernetes"),
    GIT("GIT", "Git");

    companion object {
        /**
         * API value로 TechStack 찾기 (예: "REACT", "DJANGO")
         */
        fun from(value: String): TechStack {
            return entries.find { it.value.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown TechStack: $value")
        }

        /**
         * displayName으로 TechStack 찾기 (예: "React", "Django", "Node.js")
         */
        fun fromDisplayName(displayName: String): TechStack {
            return entries.find { it.displayName.equals(displayName, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown TechStack displayName: $displayName")
        }
    }
}