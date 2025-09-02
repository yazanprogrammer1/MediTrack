package com.example.meditrack.feature.presentation.common

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.meditrack.MainActivity
import com.example.meditrack.feature.presentation.navgraph.Routs
import com.example.meditrack.ui.theme.AppThemeSettings
import com.example.meditrack.ui.theme.DarkPrimaryColor
import com.example.meditrack.ui.theme.LightPrimaryColor


@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AppBottomBar(
    navController: NavHostController,
    onNavIconClicked: (Routs) -> Unit = {},
    onAddClicked: () -> Unit = {},
    modifier: Modifier = Modifier,
) {

    val underFloatingButtonColor = MaterialTheme.colorScheme.background.copy(alpha = .5f)
    //val iconColor = if (selectedScreen == 0) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.primary
    val bottomBarBackground =
        if (AppThemeSettings.isDarkTheme) Color.Black else Color.White
    val currentNavStack by navController.currentBackStackEntryAsState()

    val window = calculateWindowSizeClass(activity = LocalContext.current as MainActivity)
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp
    val screenHeight = config.screenHeightDp.dp

    // Bottom bar height كنسبة من ارتفاع الشاشة
    val boxHeight = screenHeight * 0.12f      // 12% من ارتفاع الشاشة

    // FAB كنسبة من عرض الشاشة
    val fabSize = screenWidth * 0.15f         // 15% من عرض الشاشة
    val fabRounded = fabSize / 2               // دائري تمام
    val fabOffsetX = screenWidth / 2 - fabSize / 2
    val fabOffsetY = -boxHeight / 2            // فوق البار نص ارتفاعه

    // حجم الأيقونات كنسبة من عرض الشاشة
    val iconsSize = screenWidth * 0.06f
    val radiusUnderFabSize = fabSize / 1.2f


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(boxHeight),
        contentAlignment = Alignment.BottomStart
    ) {
        val width = this.minWidth
        val height = this.minHeight
        Card(
            elevation = CardDefaults.cardElevation(0.dp),
            modifier = Modifier.shadow(
                elevation = 16.dp,
                ambientColor = Color(0x14000000),
                spotColor = Color(0x14000000),
                shape = RoundedCornerShape(
                    topStart = 30.dp,
                    topEnd = 30.dp,
                    bottomEnd = 0.dp,
                    bottomStart = 0.dp
                )
            ),
            shape = RoundedCornerShape(
                topStart = 30.dp,
                topEnd = 30.dp,
                bottomEnd = 0.dp,
                bottomStart = 0.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(boxHeight)
                    .clip(
                        RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = 24.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
                    .graphicsLayer {
                        compositingStrategy = CompositingStrategy.Offscreen
                    }
                    .drawWithContent {
                        drawContent()

                        drawCircle(
                            color = underFloatingButtonColor,
                            radius = radiusUnderFabSize.toPx(),
                            center = Offset(x = size.width / 2, y = 0f),
                            blendMode = BlendMode.Modulate,
                        )
                    }

                    //  color = Color.Black, offsetY = (-20).dp, spread = 20.dp
                    .shadow(
                        elevation = 20.dp,
                        ambientColor = Color.Black,
                        spotColor = Color.Black,
                    )
                    .background(bottomBarBackground)
                    .padding(horizontal = 48.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = { onNavIconClicked(Routs.Medicine) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = if (currentNavStack?.destination?.route == Routs.Medicine.rout) DarkPrimaryColor else LightPrimaryColor.copy(
                            alpha = .5f
                        ),
                        modifier = Modifier.size(iconsSize)
                    )
                }
                IconButton(
                    onClick = { onNavIconClicked(Routs.Analysis) }
                ) {
                    Icon(
                        imageVector = Icons.Default.PieChart,
                        contentDescription = null,
                        tint = if (currentNavStack?.destination?.route == Routs.Analysis.rout) DarkPrimaryColor else LightPrimaryColor.copy(
                            alpha = .5f
                        ),
                        modifier = Modifier.size(iconsSize)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .offset(
                    x = (width / 2) - (fabSize / 2), // يضع FAB بالضبط في وسط الدائرة
                    y = -radiusUnderFabSize
                )
                .size(fabSize)
                .clip(RoundedCornerShape(fabRounded))
                .background(MaterialTheme.colorScheme.primary)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onAddClicked()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.background,
                modifier = Modifier.size(iconsSize)
            )
        }
    }
}