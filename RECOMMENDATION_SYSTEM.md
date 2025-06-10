# 智能推薦系統 (Smart Recommendation System)

## 概述

這個智能推薦系統取代了原本隨機推薦用戶的簡單系統，實現了基於用戶興趣、個性特質、地理位置、教育背景等多維度的智能推薦算法。

## 系統架構

### 核心組件

1. **RecommendationService** - 推薦算法核心服務
2. **UserInteractionRepository** - 用戶互動記錄管理  
3. **UserInteractionRemoteDataSource** - 互動資料的 Firebase 存取
4. **UserInteraction** - 用戶互動資料模型

## 主要功能

### 1. 多維度相似度計算
- **興趣相似度** (權重: 30%): 使用 Jaccard 相似度計算共同興趣比例
- **個性特質相似度** (權重: 25%): 分析共同的個性特質
- **地理位置相似度** (權重: 20%): 同城市或同區域的用戶獲得更高分數
- **教育背景相似度** (權重: 15%): 學歷、學校、專業的匹配程度
- **職業相似度** (權重: 10%): 職位和公司的相關性

### 2. 用戶互動記錄
- 記錄用戶的「喜歡」和「不喜歡」行為
- 避免重複推薦已經互動過的用戶
- 為未來的機器學習改進提供數據基礎

### 3. 智能過濾
- 自動排除當前用戶自己
- 過濾掉已經互動過的用戶
- 只推薦完整個人資料的用戶

### 4. 隨機性控制
- 在保持推薦質量的同時加入適度隨機性
- 避免推薦結果過於固定和可預測

## 系統架構

### 核心組件

1. **RecommendationService** - 推薦算法核心
   - 計算用戶相似度
   - 排序推薦結果
   - 控制隨機性

2. **UserInteractionRepository** - 用戶互動管理
   - 記錄用戶行為
   - 管理互動歷史
   - 提供過濾數據

3. **UserProfileRepository** - 用戶資料管理
   - 整合推薦邏輯
   - 管理用戶資料
   - 協調各服務

### 數據模型

- **UserInteraction** - 用戶互動記錄
- **InteractionType** - 互動類型 (APPROVE, REJECT, VIEW)

## 推薦算法詳細說明

### 相似度計算公式

總相似度 = Σ(各維度相似度 × 權重)

### 各維度計算方法

#### 興趣相似度 (Jaccard Similarity)
```
similarity = |interests1 ∩ interests2| / |interests1 ∪ interests2|
```

#### 地理位置相似度
- 同城市同區域: 1.0
- 同城市不同區域: 0.8
- 不同城市: 0.0

#### 教育/職業相似度
```
similarity = (相同項目數) / (總項目數)
```

## 使用方法

### 1. 基本推薦
```kotlin
val recommendedUsers = userProfileRepository.getRecommendedUsers(limit = 10)
```

### 2. 記錄用戶互動
```kotlin
// 用戶喜歡
userInteractionRepository.recordApproval(targetUserId)

// 用戶不喜歡
userInteractionRepository.recordRejection(targetUserId)
```

### 3. 刷新推薦
```kotlin
viewModel.refreshRecommendations()
```

## 用戶界面改進

### 新增功能
- 刷新推薦按鈕 (左上角)
- 改進的空狀態提示
- 自動載入更多推薦
- 更友好的反饋信息

### 用戶體驗
- 當用戶查看完所有推薦時，自動嘗試載入更多
- 提供刷新推薦的選項
- 顯示推薦狀態和載入進度

## 技術特點

1. **可擴展性**: 易於添加新的相似度維度
2. **性能優化**: 使用批量查詢和緩存
3. **數據完整性**: 完善的錯誤處理機制
4. **用戶隱私**: 本地計算，不暴露敏感信息

## 未來改進方向

1. **機器學習整合**: 基於用戶行為數據訓練模型
2. **實時反饋**: 根據用戶即時反應調整推薦
3. **協同過濾**: 基於相似用戶的喜好進行推薦
4. **A/B測試**: 測試不同推薦策略的效果

## 調試信息

系統會在控制台輸出詳細的推薦過程日誌，包括：
- 候選用戶數量
- 過濾後的用戶數量
- 每個用戶的相似度分數
- 推薦結果統計

這些信息有助於開發者理解和優化推薦算法的表現。
