# 推薦系統使用說明

## 如何使用新的推薦系統

### 對用戶的變化
1. **智能推薦**: 系統現在會根據你的興趣、位置、教育背景等推薦最相符的用戶
2. **個人化體驗**: 你的每次點擊（喜歡/不喜歡）都會被記錄，避免重複推薦
3. **刷新功能**: 點擊左上角的刷新按鈕可以獲取新的推薦
4. **更好的空狀態**: 當沒有更多推薦時，會顯示友好的提示訊息

### 對開發者的變化

#### 新增的類別和文件:
```
data/service/RecommendationService.kt        # 推薦算法核心
data/model/UserInteraction.kt                # 用戶互動資料模型
data/datasource/UserInteractionRemoteDataSource.kt  # Firebase 互動資料存取
data/repository/UserInteractionRepository.kt # 互動資料倉庫
data/utils/TestDataGenerator.kt              # 測試資料生成器
```

#### 修改的文件:
```
ui/discover/DiscoverPageViewModel.kt          # 加入推薦邏輯和互動記錄
ui/discover/DiscoverPageScreen.kt             # 加入刷新按鈕和改善 UI
data/repository/UserProfileRepository.kt     # 加入智能推薦方法
data/datasource/UserProfileRemoteDataSource.kt # 加入詳細日誌
```

#### 主要 API 變化:

1. **DiscoverPageViewModel** 新增方法:
   ```kotlin
   fun refreshRecommendations()  // 刷新推薦
   ```

2. **UserProfileRepository** 新增方法:
   ```kotlin
   suspend fun getRecommendedUsers(limit: Int = 10): Result<List<User>>
   ```

3. **UserInteractionRepository** 新增功能:
   ```kotlin
   suspend fun recordApproval(targetUserId: String): Result<Unit>
   suspend fun recordRejection(targetUserId: String): Result<Unit>
   suspend fun getRejectedUserIds(): Result<Set<String>>
   suspend fun getApprovedUserIds(): Result<Set<String>>
   ```

### 如何測試

1. **編譯應用**:
   ```bash
   ./gradlew assembleDebug
   ```

2. **運行單元測試**:
   ```bash
   ./gradlew :app:testDebugUnitTest
   ```

3. **安裝到裝置**:
   ```bash
   ./gradlew installDebug
   ```

### 調試和監控

查看推薦系統日誌:
```bash
adb logcat | grep -E "RecommendationService|UserInteraction|DiscoverPageViewModel"
```

### 常見問題

**Q: 為什麼還是看到自己的資料？**
A: 這可能是因為資料庫中沒有其他完整的用戶資料。系統會按以下順序嘗試：
1. 智能推薦其他用戶
2. 基本推薦其他用戶  
3. 顯示"沒有更多用戶"的友好訊息

**Q: 推薦結果為什麼沒有變化？**
A: 點擊左上角的刷新按鈕，或者等待滑完所有推薦後系統會自動載入更多。

**Q: 如何重置互動記錄？**
A: 目前需要手動清除 Firebase 中的 `user_interactions` 集合，未來可以加入設定頁面的重置功能。

### 下一步改進

1. **機器學習**: 加入 ML 模型來學習用戶偏好
2. **實時更新**: 當有新用戶註冊時即時更新推薦
3. **地理搜尋**: 加入距離計算和地圖整合
4. **社交功能**: 考慮共同朋友和社交關係
5. **內容過濾**: 加入年齡、職業等篩選條件
