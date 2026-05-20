# 薪动时刻

实时显示今日已赚薪资的 Android 桌面小组件。

## 功能

- 秒级精度计算当日已赚金额
- 桌面小组件实时刷新（前台 Service + AlarmManager）
- 工作进度条 + 状态显示
- 自定义月薪、上下班时间、午休时长
- 非工作日自动显示休息状态

## 截图

| 小组件 | 设置页面 |
|--------|----------|
| 实时薪资展示 | 月薪/工时配置 |

## 技术栈

- Kotlin + Jetpack Compose
- Jetpack Glance 1.1（小组件框架）
- DataStore Preferences（数据持久化）
- Foreground Service + AlarmManager（实时刷新）
- Material3 Design

## 构建

```bash
# Debug
./gradlew assembleDebug

# Release（需要 keystore.properties）
./gradlew assembleRelease
```

Release 签名配置：复制 `keystore.properties.template` 为 `keystore.properties`，填入密钥信息。

## 项目结构

```
app/src/main/kotlin/com/happynm/widget/
├── MainActivity.kt          # 设置页面
├── HappyNMApp.kt            # Application
├── domain/                  # 薪资计算逻辑
├── data/                    # DataStore 数据层
├── widget/                  # Glance 小组件
├── service/                 # 前台刷新服务
└── worker/                  # AlarmManager + WorkManager
```

## 版本

- v1.0.0 — 首个发布版本

## License

MIT
