package com.example.meditrack.feature.presentation.screens.analysis_screen.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.meditrack.R
import com.example.meditrack.ui.theme.CoolBlue2
import com.example.meditrack.ui.theme.CoolGreen
import com.example.meditrack.ui.theme.CoolRed2
import com.example.meditrack.ui.theme.dimens
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnalyticsCompleting(
    doneProgressStateProvider: () -> Float = { 0.4f },
    missedProgressStateProvider: () -> Float = { 0.2f },
) {
    val sweepDone = remember { Animatable(initialValue = 0f) }
    val sweepMissed = remember { Animatable(initialValue = 0f) }
    val sweepTodo = remember { Animatable(initialValue = 0f) }
    val donePercentage = remember { Animatable(initialValue = 0f) }
    val missedPercentage = remember { Animatable(initialValue = 0f) }
    val todoPercentage = remember { Animatable(initialValue = 0f) }


    val dimens = MaterialTheme.dimens


    LaunchedEffect(key1 = null) {
        launch {
            sweepDone.animateTo(doneProgressStateProvider() * 360, tween(1000))
            donePercentage.animateTo(doneProgressStateProvider() * 100, tween(1000))
        }
        launch {
            sweepMissed.animateTo(missedProgressStateProvider() * 360, tween(1000))
            missedPercentage.animateTo(missedProgressStateProvider() * 100, tween(1000))
        }
        launch {
            val todo = (1f - doneProgressStateProvider() - missedProgressStateProvider())
            sweepTodo.animateTo(todo * 360, tween(1000))
            todoPercentage.animateTo(todo * 100, tween(1000))
        }

    }


    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Canvas(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(MaterialTheme.dimens.analysisDimens.completedChartCanvasSize)
                    .graphicsLayer {
                        compositingStrategy = CompositingStrategy.Offscreen
                    }
                    .drawWithContent {
                        drawContent()
                        if (doneProgressStateProvider() > 0f && doneProgressStateProvider() < 1.0f) {
                            drawLine(
                                color = Color.Black,
                                start = Offset(
                                    x = dimens.analysisDimens.completedChartTopLineOffset.toPx(),
                                    y = 0.dp.toPx()
                                ),
                                end = Offset(
                                    x = dimens.analysisDimens.completedChartTopLineOffset.toPx(),
                                    y = dimens.analysisDimens.completedChartTopLineOffset.toPx()
                                ),
                                strokeWidth = dimens.analysisDimens.lineBetweenArcsStroke,
                                blendMode = BlendMode.Clear
                            )
                        }
                        drawCircle(
                            color = Color.Black,
                            radius = dimens.analysisDimens.completedChartRadius,
                            blendMode = BlendMode.Clear
                        )
                    }
            ) {
                val startAngle = -90f
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = 320f
                val startAngle1 = -90f
                val startAngle2 = (doneProgressStateProvider() * 360) - 90f

                // ✅ done arc
                drawArc(
                    color = CoolGreen,
                    startAngle = startAngle,
                    sweepAngle = sweepDone.value,
                    useCenter = true
                )

                // ✅ missed arc (يبدأ بعد الـ done)
                drawArc(
                    color = CoolBlue2,
                    startAngle = startAngle + sweepDone.value,
                    sweepAngle = sweepMissed.value,
                    useCenter = true
                )

                // ✅ todo arc (يبدأ بعد الاثنين)
                drawArc(
                    color = CoolRed2,
                    startAngle = startAngle + sweepDone.value + sweepMissed.value,
                    sweepAngle = sweepTodo.value,
                    useCenter = true
                )

                val offsetAngle = startAngle1 + sweepDone.value
                val offsetRadians = Math.toRadians(offsetAngle.toDouble())
                val xOffset =
                    center.x + (radius * cos(offsetRadians)).toFloat() * dimens.analysisDimens.completedChartBottomLineMultiply
                val yOffset =
                    center.y + (radius * sin(offsetRadians)).toFloat() * dimens.analysisDimens.completedChartBottomLineMultiply

                if (doneProgressStateProvider() > 0f && doneProgressStateProvider() < 1.0f) {
                    drawLine(
                        color = Color.Red,
                        start = center,
                        end = Offset(xOffset, yOffset),
                        strokeWidth = dimens.analysisDimens.lineBetweenArcsStroke,
                        blendMode = BlendMode.Clear
                    )
                }

            }
            Column {
                Text(
                    text = "${donePercentage.value.toInt()}%",
                    modifier = Modifier
                        .width(dimens.analysisDimens.completedChartPercentageBoxWidth)
                        .background(color = CoolGreen, shape = RoundedCornerShape(4.dp))
                        .padding(dimens.analysisDimens.completedChartPercentageBoxPadding),
                    color = Color.White,
                    //fontFamily = SfDisplay,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "${missedPercentage.value.toInt()}%",
                    modifier = Modifier
                        .width(dimens.analysisDimens.completedChartPercentageBoxWidth)
                        .background(color = CoolBlue2, shape = RoundedCornerShape(4.dp))
                        .padding(dimens.analysisDimens.completedChartPercentageBoxPadding),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "${todoPercentage.value.toInt()}%",
                    modifier = Modifier
                        .width(dimens.analysisDimens.completedChartPercentageBoxWidth)
                        .background(
                            color = CoolRed2,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(dimens.analysisDimens.completedChartPercentageBoxPadding),
                    color = Color.White,
                    //fontFamily = SfDisplay,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    textAlign = TextAlign.Center
                )
            }

        }

        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Canvas(modifier = Modifier.size(dimens.analysisDimens.completedChartColorCircleSize)) {
                    drawCircle(color = CoolGreen)
                }
                Text(
                    text = stringResource(id = R.string.taken),
                    //fontFamily = SfDisplay,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Canvas(modifier = Modifier.size(dimens.analysisDimens.completedChartColorCircleSize)) {
                    drawCircle(color = CoolBlue2)
                }
                Text(
                    text = stringResource(id = R.string.missing),
                    //fontFamily = SfDisplay,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Canvas(modifier = Modifier.size(dimens.analysisDimens.completedChartColorCircleSize)) {
                    drawCircle(color = CoolRed2)
                }
                Text(
                    text = stringResource(id = R.string.you_should_take_it),
                    //fontFamily = SfDisplay,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0x66DFDFDF))
        )
    }
}