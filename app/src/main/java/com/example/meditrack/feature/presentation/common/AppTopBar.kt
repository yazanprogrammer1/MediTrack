package com.example.meditrack.feature.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.meditrack.ui.theme.AppThemeSettings
import com.example.meditrack.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    onNavIconClicked: () -> Unit,
) {
    TopAppBar(
        title = {
        Text(
            text = appNameStyle(),
            modifier = Modifier.padding(start = MaterialTheme.dimens.homeScreenDimens.menuIconEndPadding)
        )
    }, navigationIcon = {
        IconButton(onClick = onNavIconClicked) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                modifier = Modifier.size(MaterialTheme.dimens.homeScreenDimens.menuIconSize)
            )
        }
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = if (AppThemeSettings.isDarkTheme) MaterialTheme.colorScheme.primary else Color.White
    ), windowInsets = MaterialTheme.dimens.homeScreenDimens.tobBarWindowInsets
    )
}