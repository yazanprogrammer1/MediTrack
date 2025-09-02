package com.example.meditrack.feature.presentation.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.example.meditrack.ui.theme.AppThemeSettings

@Composable
fun appNameStyle(): AnnotatedString {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val tertiary = MaterialTheme.colorScheme.tertiary

    return buildAnnotatedString {

        withStyle(
            SpanStyle(
                fontFamily = FontFamily.Cursive,
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                fontWeight = FontWeight.ExtraBold,
                color = if (AppThemeSettings.isDarkTheme) Color.White else primary
            )
        ) {
            append("M")
        }

        withStyle(
            SpanStyle(
                fontFamily = FontFamily.Cursive,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Medium,
                color = if (AppThemeSettings.isDarkTheme) Color.White else secondary
            )
        ) {
            append("edi")
        }

        withStyle(
            SpanStyle(
                fontFamily = FontFamily.Serif,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontWeight = FontWeight.SemiBold,
                color = tertiary
            )
        ) {
            append("Tra")
        }

        withStyle(
            SpanStyle(
                fontFamily = FontFamily.Monospace,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontWeight = FontWeight.Bold,
                brush = Brush.linearGradient(
                    listOf(
                        Color.Magenta,
                        Color.Cyan,
                        Color.Blue
                    )
                )
            )
        ) {
            append("ck")
        }
    }
}