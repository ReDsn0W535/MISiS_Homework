package com.todotodo.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    background = Gray96, //background color
    primary = White, //background color for components
    secondary = Gray95, //background color for inner components

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

    surfaceBright = Green50, // "on" color
    surfaceDim = BlackA6, // "off" color
)

private val DarkColorScheme = darkColorScheme(
    background = Black, //background color
    primary = Gray15, //background color for components
    secondary = Gray20, //background color for inner components

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

    surfaceBright = Green50, // "on" color
    surfaceDim = WhiteA6, // "off" color
)

@Composable
fun TODOTODOTheme(
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


@Composable
fun ThemeUpdater(isDarkTheme : Boolean, content : @Composable (()-> Unit)){
    val reloadScreenVisibility = remember { MutableTransitionState(false) }
    val isDarkThemeLocal = remember{ mutableStateOf(isDarkTheme) }

    LaunchedEffect(isDarkTheme) {
        if(isDarkTheme!=isDarkThemeLocal.value)
            reloadScreenVisibility.targetState=true
    }
    if(reloadScreenVisibility.currentState){
        isDarkThemeLocal.value=isDarkTheme
        reloadScreenVisibility.targetState=false
    }
    TODOTODOTheme(darkTheme = isDarkThemeLocal.value){
        content()
    }
    AnimatedVisibility(reloadScreenVisibility, enter = fadeIn(), exit = fadeOut()) {
        Box(modifier = Modifier.fillMaxSize().background(color = Color.LightGray))
    }
}