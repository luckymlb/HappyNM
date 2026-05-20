# HappyNM - Android 工作日薪小组件

## Context

复刻 iOS "Island Widgets" 中的工作日薪小组件 Android 版本。使用 Kotlin + Jetpack Glance 技术栈，实现全部 7 个功能模块。

## 技术选型

- **语言**: Kotlin
- **Widget 框架**: Jetpack Glance 1.1+（Compose 风格）
- **设置页面**: Jetpack Compose + Material3
- **数据存储**: DataStore Preferences
- **网络**: Ktor Client + kotlinx.serialization
- **后台任务**: WorkManager + AlarmManager
- **最低版本**: Android 8.0 (API 26)
- **构建**: Gradle Kotlin DSL + Version Catalog

## 功能模块

| 模块 | 说明 | 数据来源 |
|------|------|----------|
| 时钟 | 时间 + 日期 + 农历 | 本地计算 |
| 今日已赚 | 实时薪资（核心） | DataStore 设置 + 本地计算 |
| 天气 | 温度范围 | 和风天气 API |
| 倒计时 | 距目标日 N 天 | DataStore 设置 |
| 专注时刻 | 番茄钟入口 | 前台 Service |
| 工作状态 | 进度条 + 状态文字 | 本地计算 |
| 日程 | 今日事件数 | 系统日历 ContentProvider |

## 项目结构

```
HappyNM/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle/libs.versions.toml
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── res/ (xml/, values/, drawable/, mipmap-*)
│       └── kotlin/com/happynm/widget/
│           ├── HappyNMApp.kt
│           ├── MainActivity.kt
│           ├── ui/ (设置页面 Compose UI)
│           ├── widget/ (Glance Widget)
│           │   ├── MainWidget.kt + Receiver
│           │   └── components/ (7个子模块)
│           ├── data/ (model, repository, datastore)
│           ├── domain/ (SalaryCalculator, LunarCalendar, WorkProgressCalculator)
│           ├── network/ (WeatherApi)
│           └── worker/ (WidgetUpdateWorker, WeatherSyncWorker)
```

## 核心实现细节

### 薪资计算逻辑
```
日薪 = 月薪 / 当月工作日数
已赚 = 日薪 × (已工作有效分钟 / 每日总工作分钟)
```
边界：非工作日显示"休息中"，未到上班时间显示 ¥0.00，超过下班时间显示日满额。

### Widget 刷新策略
- 系统级 updatePeriodMillis = 30分钟（最小值）
- WorkManager 每15分钟触发
- AlarmManager 工作时间内每分钟精确触发
- 非工作时间降频至30分钟

### 农历计算
自包含查表法（1900-2100年数据表），零网络依赖，约2KB数据。

## 实现顺序

### Phase 1: 项目骨架 + 核心（先做能跑起来的最小版本）
1. 创建完整项目结构（Gradle、Manifest、依赖）
2. DataStore + UserSettings 数据模型
3. SalaryCalculator 核心计算逻辑
4. MainWidget (Glance) 显示薪资
5. 基础设置页面（月薪、工作时间）

### Phase 2: 时钟 + 刷新
6. LunarCalendar 农历算法
7. ClockModule 时间/日期/农历
8. WidgetUpdateWorker 定时刷新

### Phase 3: 完整组合
9. WorkStatusModule 工作进度条
10. CountdownModule 倒计时
11. 组合成完整 4x4 主小组件

### Phase 4: 网络 + 外部数据
12. WeatherApi + 天气模块
13. ScheduleModule 日历读取
14. FocusModule 专注入口

### Phase 5: 打磨
15. UI 深色主题、圆角卡片、字体层次
16. 多尺寸 Widget (2x2, 4x2, 4x4)
17. 错误处理与权限引导

## 权限
- INTERNET, ACCESS_NETWORK_STATE（天气）
- READ_CALENDAR（日程）
- SCHEDULE_EXACT_ALARM（分钟刷新）
- FOREGROUND_SERVICE, POST_NOTIFICATIONS（专注计时）
- ACCESS_COARSE_LOCATION（可选，天气定位）

## 验证方式
1. `./gradlew assembleDebug` 构建成功
2. 模拟器上添加小组件，确认显示正确的薪资数据
3. 修改设置后，小组件数据实时更新
4. 等待1分钟，验证薪资金额增长
