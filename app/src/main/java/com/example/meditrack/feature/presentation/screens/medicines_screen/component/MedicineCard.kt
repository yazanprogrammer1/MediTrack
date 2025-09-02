package com.example.meditrack.feature.presentation.screens.medicines_screen.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.meditrack.AlarmService
import com.example.meditrack.R
import com.example.meditrack.feature.domain.model.Medicine
import com.example.meditrack.feature.domain.model.Priority
import com.example.meditrack.ui.theme.AppThemeSettings
import com.example.meditrack.ui.theme.Black200
import com.example.meditrack.ui.theme.DarkPrimaryColor
import com.example.meditrack.ui.theme.HighPriorityColor
import com.example.meditrack.ui.theme.LightPrimaryColor
import com.example.meditrack.ui.theme.LowPriorityColor
import com.example.meditrack.ui.theme.NormalPriorityColor
import com.example.meditrack.ui.theme.dimens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MedicineCard(
    medicine: Medicine = Medicine.DEFAULT_TASK,
    elevation: Dp = MaterialTheme.dimens.homeScreenDimens.taskCardElevation,
    borderStroke: BorderStroke? = null,
    showPinnedIcon: Boolean = false,
    modifier: Modifier = Modifier,
    alarmService: AlarmService,
    dataStore: DataStore<Preferences>,
    scope: CoroutineScope,
    onCheckedChange: (Medicine) -> Unit,
    onTaskClicked: () -> Unit = {},
) {


    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(MaterialTheme.dimens.homeScreenDimens.taskCardRounded),
        elevation = CardDefaults.cardElevation(elevation),
        border = borderStroke,
        onClick = onTaskClicked
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(MaterialTheme.dimens.homeScreenDimens.taskRowContainerPadding)
                .padding(end = MaterialTheme.dimens.homeScreenDimens.taskRowContainerPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.homeScreenDimens.spaceBetweenToggleIconAndPendIcon)
            ) {

                if (showPinnedIcon) {

                    val transX = remember {
                        Animatable(0f)
                    }

                    val transY = remember {
                        Animatable(-100f)
                    }
                    val density = LocalDensity.current.density



                    LaunchedEffect(key1 = Unit) {
                        launch {
                            transX.animateTo(
                                targetValue = 5 * density,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                )
                            )
                        }

                        launch {
                            transY.animateTo(
                                targetValue = 5 * density,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = 50f
                                )
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.PushPin,
                        contentDescription = null,
                        modifier = Modifier
                            .size(MaterialTheme.dimens.homeScreenDimens.pendingIconSize)
                            .graphicsLayer {
                                translationX = transX.value
                                translationY = transY.value
                                rotationZ = if (Locale.getDefault().language == "ar") 30f else -30f
                            },
                        tint = Color.Red,
                    )
                }

                CustomIconToggleButton(
                    checked = medicine.isCompleted,
                    onCheckedChange = {
                        onCheckedChange(
                            medicine.copy(
                                isCompleted = !medicine.isCompleted,
                                isRestored = true
                            )
                        )
                    },
                    size = MaterialTheme.dimens.homeScreenDimens.iconToggleButtonSize,
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.homeScreenDimens.iconToggleButtonPadding)
                ) {

                    AnimatedVisibility(
                        visible = medicine.isCompleted,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = 1000
                            )
                        ),
                        exit = fadeOut(
                            animationSpec = tween(
                                durationMillis = 100
                            )
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.inversePrimary,
                            modifier = Modifier.size(MaterialTheme.dimens.homeScreenDimens.iconToggleButtonSize)
                        )
                    }

                    AnimatedVisibility(
                        visible = !medicine.isCompleted,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = 1000
                            )
                        ),
                        exit = fadeOut(
                            animationSpec = tween(
                                durationMillis = 100
                            )
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Circle,
                            contentDescription = null,
                            tint = Black200,
                            modifier = Modifier.size(MaterialTheme.dimens.homeScreenDimens.iconToggleButtonSize)
                        )
                    }
                }


                if (showPinnedIcon) {
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.homeScreenDimens.spacerIfShowPendingIcon))
                }
            }


            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = medicine.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = if (medicine.isCompleted) MaterialTheme.colorScheme.onBackground.copy(
                        alpha = .5f
                    ) else MaterialTheme.colorScheme.onBackground,
                    textDecoration = if (medicine.isCompleted) TextDecoration.LineThrough else null
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.homeScreenDimens.spacerBetweenTaskTitleAndDescription))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = medicine.description,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(MaterialTheme.dimens.homeScreenDimens.spacerBetweenColumnAndTimer))

                    if (!medicine.isCompleted && medicine.isEnteredTime) {

                        var idFromService by remember {
                            mutableIntStateOf(0)
                        }

                        LaunchedEffect(key1 = Unit) {
                            dataStore.data.collectLatest { preferences ->
                                idFromService = preferences[intPreferencesKey("medicineId")] ?: 0
                            }
                        }

                        val zero = String.format(stringResource(id = R.string.numberRes), 0)

                        Text(
                            text = if (medicine.id == idFromService) alarmService.timeValue.value else "$zero$zero:$zero$zero:$zero$zero",
                            modifier = Modifier.weight(0.3f),
                            color = if (AppThemeSettings.isDarkTheme)
                                Color.Red else Color.Red.copy(alpha = .5f),
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
                        )

                    } else {
                        Spacer(
                            modifier = Modifier
                                .width(MaterialTheme.dimens.homeScreenDimens.spacerIfNoTimerWidth)
                                .weight(MaterialTheme.dimens.homeScreenDimens.spacerIfNoTimerWeight)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(MaterialTheme.dimens.homeScreenDimens.taskCardPrioritySize)
                            .clip(MaterialTheme.dimens.homeScreenDimens.taskCardPriorityRounded)
                            .background(
                                if (medicine.priority == Priority.NORMAL.value) {
                                    NormalPriorityColor
                                } else if (medicine.priority == Priority.HIGH.value) {
                                    HighPriorityColor
                                } else {
                                    LowPriorityColor
                                }
                            )
                    )

                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.homeScreenDimens.spacerBetweenTaskTitleAndDescription))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(// Canvas here instead of icon
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(MaterialTheme.dimens.homeScreenDimens.taskCardDateIconSize),
                            tint = if (AppThemeSettings.isDarkTheme)
                                Color.White else Color.White
                        )

                        Text(
                            text = medicine.getFormattedStartDate(),
                            modifier = Modifier.padding(start = MaterialTheme.dimens.homeScreenDimens.taskCardPaddingDateTxt),
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                color = MaterialTheme.colorScheme.background
                            ),
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(// Canvas here instead of icon
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            modifier = Modifier.size(MaterialTheme.dimens.homeScreenDimens.taskCardTimeIconSize),
                            tint = if (AppThemeSettings.isDarkTheme)
                                LightPrimaryColor else DarkPrimaryColor
                        )

                        Text(
                            text = medicine.getFormattedTime(),
                            modifier = Modifier.padding(start = MaterialTheme.dimens.homeScreenDimens.taskCardPaddingTimeTxt),
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                color = MaterialTheme.colorScheme.background
                            ),
                        )
                    }
                }
            }

        }
    }
}