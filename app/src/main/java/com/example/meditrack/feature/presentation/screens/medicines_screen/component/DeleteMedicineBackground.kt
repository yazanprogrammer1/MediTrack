package com.example.meditrack.feature.presentation.screens.medicines_screen.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.meditrack.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteMedicineBackground(
    swipeDismissState: SwipeToDismissBoxState,
) {
    val direction = swipeDismissState.dismissDirection
    val alignment = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
        else -> Alignment.CenterEnd
    }

    // حركة الأيقونة مع السحب
    val progress = swipeDismissState.progress
    val offsetX by animateDpAsState(
        targetValue = if (progress > 0f) (progress * 40).dp else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    val color =
        if (swipeDismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart ||
            swipeDismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd
        ) {
            Color.Red
        } else {
            Color.Transparent
        }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(24.dp)) // نفس شكل MedicineCard
            .background(
                color
            )
            .padding(horizontal = 24.dp), contentAlignment = alignment
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "delete Icon",
            tint = Color.White,
            modifier = Modifier
                .size(MaterialTheme.dimens.homeScreenDimens.menuIconSize * 1.2f) // تكبير قليلًا
                .offset(x = if (alignment == Alignment.CenterStart) offsetX else -offsetX) // يتحرك مع السحب
        )
    }
}
