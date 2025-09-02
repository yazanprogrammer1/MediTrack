package com.example.meditrack.feature.presentation.screens.medicines_screen.component

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import kotlinx.coroutines.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDeleteContainer(
    item:T,
    onDelete:(T) -> Unit,
    animationDuration:Int,
    modifier: Modifier = Modifier,
    content:@Composable (T) -> Unit,
) {

    var isRemoved by remember {
        mutableStateOf(false)
    }

    val isArabic = Locale.getDefault().language == "ar"
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true
                true
            } else if (value == SwipeToDismissBoxValue.StartToEnd) {
                isRemoved = true
                true
            }
            else{
                false
            }
        }
    )

    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved){
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }


        Box(modifier = modifier){
            AnimatedVisibility(
                visible = !isRemoved,
                exit = shrinkVertically(
                    animationSpec = tween(
                        durationMillis = animationDuration
                    )
                ),
                modifier = modifier
            ) {

                SwipeToDismissBox(
                    state = state,
                    backgroundContent = {
                        DeleteMedicineBackground(swipeDismissState = state)
                    },
                    enableDismissFromStartToEnd = isArabic,
                    enableDismissFromEndToStart = !isArabic
                ) {
                    content(item)
                }

            }
        }
}