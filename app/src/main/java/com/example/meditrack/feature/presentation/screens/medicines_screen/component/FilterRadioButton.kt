package com.example.meditrack.feature.presentation.screens.medicines_screen.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import com.example.meditrack.ui.theme.dimens

@Composable
fun FilterRadioButton(
    txt:String,
    selected:Boolean,
    onSelect:() -> Unit,
    modifier: Modifier = Modifier
){

    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){

        CustomRadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onBackground
            ),
            radius = MaterialTheme.dimens.homeScreenDimens.radioButtonSize

        )


        Text(text = txt, style = MaterialTheme.typography.bodyMedium)

    }

}

