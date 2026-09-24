package com.smogunov.showcase.core.model

data class UserData(
    val themeConfig: ThemeConfig,
    val useDynamicColor: Boolean,
)

enum class ThemeConfig {
    FOLLOW_SYSTEM,
    LIGHT,
    DARK,
}
