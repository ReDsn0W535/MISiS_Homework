package com.example.misis.ui.common.themepicker

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.misis.ui.appspecific.DarkColorScheme
import com.example.misis.ui.appspecific.LightColorScheme
import com.example.misis.ui.appspecific.AppTypography

@Composable
fun ThemePicker(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
