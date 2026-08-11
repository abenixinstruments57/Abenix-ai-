package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = GeometricBlue,
    onPrimary = Color.White,
    primaryContainer = GeometricBlueDark,
    onPrimaryContainer = GeometricBlueLight,
    secondary = GeometricTextSecondary,
    tertiary = GoldenBadge,
    background = DarkNavyBg,
    surface = DarkNavySurface,
    surfaceVariant = DarkNavyCard,
    outline = Color(0xFF4B5563),
    onBackground = Color.White,
    onSurface = Color.White
  )

private val LightColorScheme =
  lightColorScheme(
    primary = GeometricBlue,
    onPrimary = Color.White,
    primaryContainer = GeometricBlueLight,
    onPrimaryContainer = GeometricBlue,
    secondary = GeometricTextSecondary,
    tertiary = GoldenBadge,
    background = GeometricBg,
    surface = GeometricSurface,
    surfaceVariant = GeometricBg,
    outline = GeometricBorder,
    onBackground = GeometricTextPrimary,
    onSurface = GeometricTextPrimary,
    onSurfaceVariant = GeometricTextSecondary
  )

@Composable
fun AbenixTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) = AbenixTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
