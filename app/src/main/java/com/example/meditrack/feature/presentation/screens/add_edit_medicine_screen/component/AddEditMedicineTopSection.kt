package com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import com.example.meditrack.ui.theme.dimens
import com.example.meditrack.R

@Preview
@Composable
fun AddEditMedicineTopSection(
    onBackClicked:() -> Unit = {},
    onRestClicked:() -> Unit = {},
    isRestEnabled:Boolean = false
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.addEditScreenDimens.topSectionPadding)
    ){

        IconButton(onClick = onBackClicked) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack ,
                contentDescription = null,
                modifier = Modifier.size(MaterialTheme.dimens.addEditScreenDimens.iconsSize)
            )

        }

        Text(
            text = stringResource(R.string.add_edit_medicine_title),
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            fontWeight = FontWeight.Bold
        )


        IconButton(
            onClick = onRestClicked,
            enabled = isRestEnabled
        ) {
            Icon(
                imageVector = Icons.Default.Autorenew,
                contentDescription = null,
                modifier = Modifier.size(MaterialTheme.dimens.addEditScreenDimens.iconsSize
                )
            )
        }
    }
}