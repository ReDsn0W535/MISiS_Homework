package com.todo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val LightColorScheme = lightColorScheme(
    background = Gray96, //background color
    primary = White, //background color for components

    onPrimary = Black, // text color for components
    onSecondary = BlackA60, //additional text color for components
    onTertiary = BlackA15, //disabled text color for components

    surfaceContainerHigh = Red60, //color for high priority and delete
    surfaceContainerLow = Gray80, //color for low priority and info

    outline = BlackA20, // separator color
    outlineVariant = Gray55, // border color

    onSurface = White, // icon color
    surface = Green50, // color for done component
    surfaceVariant = Blue50, //color for add component

    surfaceBright = Light, // "on" color
    surfaceDim = Dark, // "off" color

    surfaceTint = CalendarColor,
    onBackground = Gray80,
)

private val DarkColorScheme = darkColorScheme(
    background = Black, //background color
    primary = Gray15, //background color for components

    onPrimary = White, // text color for components
    onSecondary = WhiteA60, //additional text color for components
    onTertiary = WhiteA15, //disabled text color for components

    surfaceContainerHigh = Red60, //color for high priority and delete
    surfaceContainerLow = Gray28, //color for low priority and info

    outline = WhiteA20, // separator color
    outlineVariant = Gray55, // border color

    onSurface = White, // icon color
    surface = Green50, // color for done component
    surfaceVariant = Blue50, //color for add component

    surfaceBright = Light, // "on" color
    surfaceDim = Dark, // "off" color

    surfaceTint = CalendarColorDark,
    onBackground = Gray28,
)

@Composable
fun RomadovaTODOTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
