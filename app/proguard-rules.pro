# HappyNM ProGuard Rules

-keep class com.happynm.widget.data.model.** { *; }
-keep class com.happynm.widget.network.** { *; }

-dontwarn io.ktor.**
-keep class io.ktor.** { *; }

-dontwarn org.slf4j.**
-dontwarn kotlinx.atomicfu.**
