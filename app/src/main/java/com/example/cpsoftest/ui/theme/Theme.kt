package com.example.cpsoftest.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ColorPrimary    = Color(0xFF1A3C6E)
val ColorSecondary  = Color(0xFF2563EB)
val ColorAccent     = Color(0xFFF59E0B)
val ColorSurface    = Color(0xFFF8FAFF)
val ColorBackground = Color(0xFFF0F4FF)
val ColorCard       = Color(0xFFFFFFFF)
val ColorOnPrimary  = Color(0xFFFFFFFF)
val ColorError      = Color(0xFFDC2626)
val ColorSuccess    = Color(0xFF16A34A)
val ColorGrayLight  = Color(0xFFE2E8F0)
val ColorGrayMed    = Color(0xFF94A3B8)
val ColorGrayDark   = Color(0xFF475569)
val ColorText       = Color(0xFF0F172A)

private val LightColors = lightColorScheme(
    primary          = ColorPrimary,
    onPrimary        = ColorOnPrimary,
    secondary        = ColorSecondary,
    onSecondary      = ColorOnPrimary,
    tertiary         = ColorAccent,
    background       = ColorBackground,
    surface          = ColorSurface,
    onBackground     = ColorText,
    onSurface        = ColorText,
    error            = ColorError
)

@Composable
fun CPSOFTESTTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}