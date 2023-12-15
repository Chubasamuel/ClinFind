package com.chubasamuel.clinfind.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val _darkColorScheme = ExtendedColors(
    material= darkColorScheme(primary = green_dark,
    secondary = PurpleGrey80,
    tertiary = Pink80,
),
    statusBarColor = statusBarColor_dark,
    cardBg = cardBg_dark,
    swipeDefault = swipeDismissDefaultBg_dark,
    labelColor = label_dark,
    textColor= textColor_dark,
    iconTint = tintIcon_dark,
    red = red_dark,
    green = green_dark,
    blue = blue_dark,
    background = background_dark,
    searchIconTint = search_icon_dark,
    optionsSel = optionsSel_dark
    )

private val _lightColorScheme = ExtendedColors(
    material = lightColorScheme( primary = green,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    ),
    statusBarColor = statusBarColor,
    cardBg = cardBg,
    swipeDefault = swipeDismissDefaultBg,
    labelColor = label,
    textColor=textColor,
    iconTint= tintIcon,
    red = red,
    green = green,
    blue = blue,
    background= background,
    searchIconTint = search_icon,
    optionsSel = optionsSel
)
@Immutable
data class ExtendedColors(
    val material: ColorScheme,
    val statusBarColor: Color,
    val cardBg: Color,
    val swipeDefault: Color,
    val labelColor: Color,
    val textColor: Color,
    val iconTint: Color,
    val red: Color,
    val green: Color,
    val blue: Color,
    val background: Color,
    val searchIconTint: Color,
    val optionsSel: Color
) {
    val primary: Color get()=material.primary
    val secondary: Color get() = material.secondary
    val surface: Color get() = material.surface
    val error: Color get()=material.error
    val onPrimary: Color get() = material.onPrimary
    val onBackground: Color get() = material.onBackground
    val onSecondary: Color get() = material.onSecondary
    val onSurface: Color get() = material.onSurface
    val onError: Color get() = material.onError
}
private val localExtendedColors = staticCompositionLocalOf {
    _lightColorScheme
}
@Composable
fun ClinFindTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            //val context = LocalContext.current
            if (darkTheme) _darkColorScheme else _lightColorScheme
        }

        darkTheme -> _darkColorScheme
        else -> _lightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.statusBarColor.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }
    CompositionLocalProvider(localExtendedColors provides colors) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
        shapes = Shapes
    )}
}

object CFT{
    val colors:ExtendedColors
        @Composable
        get() = localExtendedColors.current
}