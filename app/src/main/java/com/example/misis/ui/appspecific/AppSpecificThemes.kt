package com.example.misis.ui.appspecific

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

internal val LightColorScheme = lightColorScheme(
    background = GrayLight,
    primary = White,
    onPrimary = Black,
    onSecondary = BlackA60,
    onTertiary = BlackA15,
    surfaceContainerHigh = Red60,
    surfaceContainerLow = GrayMedium,
    outline = BlackA20,
    outlineVariant = GrayDark,
    onSurface = White,
    surface = Green50,
    surfaceVariant = Blue50,
    surfaceBright = Light,
    surfaceDim = Dark,
    surfaceTint = CalendarColor,
    onBackground = GrayMedium,
)

internal val DarkColorScheme = darkColorScheme(
    background = Black,
    primary = GrayCharcoal,
    onPrimary = White,
    onSecondary = WhiteA60,
    onTertiary = WhiteA15,
    surfaceContainerHigh = Red60,
    surfaceContainerLow = GrayDarker,
    outline = WhiteA20,
    outlineVariant = GrayDark,
    onSurface = White,
    surface = Green50,
    surfaceVariant = Blue50,
    surfaceBright = Light,
    surfaceDim = Dark,
    surfaceTint = CalendarColorDark,
    onBackground = GrayDarker,
)
