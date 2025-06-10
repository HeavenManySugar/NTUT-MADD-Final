package com.ntut.madd.finalproject.data.utils

import com.ntut.madd.finalproject.data.model.User
import com.ntut.madd.finalproject.data.model.UserProfile

/**
 * 測試資料生成器
 * 用於生成測試用戶資料來驗證推薦系統
 */
object TestDataGenerator {

    /**
     * 生成測試用戶列表
     */
    fun generateTestUsers(): List<User> {
        return listOf(
            User(
                id = "test_user_1",
                ownerId = "test_owner_1",
                displayName = "Alice Chen",
                email = "alice@test.com",
                isAnonymous = false,
                profile = UserProfile(
                    city = "Taipei",
                    district = "Xinyi",
                    position = "Software Engineer",
                    company = "Tech Corp",
                    degree = "Bachelor",
                    school = "National Taiwan University",
                    major = "Computer Science",
                    interests = listOf("Programming", "Music", "Travel", "Reading"),
                    personalityTraits = listOf("Creative", "Analytical", "Outgoing"),
                    aboutMe = "I'm a passionate software engineer who loves creating innovative solutions and exploring new technologies. In my free time, I enjoy playing music and traveling to discover new cultures.",
                    lookingFor = "Looking for someone who shares similar interests in technology and has a curious mind about the world.",
                    isProfileComplete = true,
                    profileCompletedAt = System.currentTimeMillis()
                )
            ),
            User(
                id = "test_user_2",
                ownerId = "test_owner_2",
                displayName = "Bob Wang",
                email = "bob@test.com",
                isAnonymous = false,
                profile = UserProfile(
                    city = "Taipei",
                    district = "Daan",
                    position = "Product Manager",
                    company = "Innovation Inc",
                    degree = "Master",
                    school = "National Taiwan University",
                    major = "Business Administration",
                    interests = listOf("Business", "Sports", "Cooking", "Photography"),
                    personalityTraits = listOf("Leadership", "Strategic", "Energetic"),
                    aboutMe = "Product manager with a passion for user experience and business strategy. I love staying active through sports and experimenting with new recipes in the kitchen.",
                    lookingFor = "Seeking someone who is ambitious, enjoys good food, and likes to stay active.",
                    isProfileComplete = true,
                    profileCompletedAt = System.currentTimeMillis()
                )
            ),
            User(
                id = "test_user_3",
                ownerId = "test_owner_3",
                displayName = "Carol Liu",
                email = "carol@test.com",
                isAnonymous = false,
                profile = UserProfile(
                    city = "Kaohsiung",
                    district = "Zuoying",
                    position = "Designer",
                    company = "Creative Studio",
                    degree = "Bachelor",
                    school = "Shih Chien University",
                    major = "Visual Design",
                    interests = listOf("Art", "Design", "Movies", "Coffee"),
                    personalityTraits = listOf("Creative", "Artistic", "Thoughtful"),
                    aboutMe = "Visual designer who finds inspiration in everyday beauty. I love watching indie films and discovering cozy coffee shops around the city.",
                    lookingFor = "Looking for someone who appreciates art and enjoys deep conversations over coffee.",
                    isProfileComplete = true,
                    profileCompletedAt = System.currentTimeMillis()
                )
            ),
            User(
                id = "test_user_4",
                ownerId = "test_owner_4",
                displayName = "David Zhang",
                email = "david@test.com",
                isAnonymous = false,
                profile = UserProfile(
                    city = "Taichung",
                    district = "Xitun",
                    position = "Data Scientist",
                    company = "AI Solutions",
                    degree = "PhD",
                    school = "National Tsing Hua University",
                    major = "Data Science",
                    interests = listOf("Machine Learning", "Hiking", "Gaming", "Reading"),
                    personalityTraits = listOf("Analytical", "Curious", "Patient"),
                    aboutMe = "Data scientist passionate about using AI to solve real-world problems. When not coding, I enjoy hiking in the mountains and playing strategy games.",
                    lookingFor = "Seeking someone who enjoys intellectual discussions and outdoor adventures.",
                    isProfileComplete = true,
                    profileCompletedAt = System.currentTimeMillis()
                )
            ),
            User(
                id = "test_user_5",
                ownerId = "test_owner_5",
                displayName = "Emma Wu",
                email = "emma@test.com",
                isAnonymous = false,
                profile = UserProfile(
                    city = "Taipei",
                    district = "Zhongshan",
                    position = "Marketing Manager",
                    company = "Brand Agency",
                    degree = "Master",
                    school = "National Chengchi University",
                    major = "Marketing",
                    interests = listOf("Marketing", "Yoga", "Travel", "Food"),
                    personalityTraits = listOf("Communicative", "Organized", "Adventurous"),
                    aboutMe = "Marketing professional who loves creating compelling brand stories. I practice yoga daily and love exploring new cuisines from different cultures.",
                    lookingFor = "Looking for someone who is open-minded, loves to travel, and enjoys trying new experiences.",
                    isProfileComplete = true,
                    profileCompletedAt = System.currentTimeMillis()
                )
            )
        )
    }

    /**
     * 打印推薦系統的統計資訊
     */
    fun printRecommendationStats(currentUser: User, candidates: List<User>, recommendations: List<User>) {
        println("=== Recommendation System Stats ===")
        println("Current User: ${currentUser.displayName}")
        println("Current User Interests: ${currentUser.profile?.interests}")
        println("Current User Location: ${currentUser.profile?.city}, ${currentUser.profile?.district}")
        println("Current User Education: ${currentUser.profile?.school}, ${currentUser.profile?.major}")
        println()
        println("Total Candidates: ${candidates.size}")
        println("Recommendations Returned: ${recommendations.size}")
        println()
        
        recommendations.forEachIndexed { index, user ->
            println("Recommendation ${index + 1}: ${user.displayName}")
            println("  - Interests: ${user.profile?.interests}")
            println("  - Location: ${user.profile?.city}, ${user.profile?.district}")
            println("  - Education: ${user.profile?.school}, ${user.profile?.major}")
            println()
        }
    }
}
