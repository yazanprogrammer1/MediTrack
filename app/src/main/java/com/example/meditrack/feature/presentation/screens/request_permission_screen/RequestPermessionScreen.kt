package com.example.meditrack.feature.presentation.screens.request_permission_screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.meditrack.MainActivity
import com.example.meditrack.R

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun RequestPermissionScreen(
    @SuppressLint("ContextCastToActivity") context: Activity = LocalContext.current as MainActivity,
    onGrantedShowScreen: @Composable () -> Unit,
) {


    var showDialog by remember {
        mutableStateOf(false)
    }

    var goToSettings by remember {
        mutableStateOf(false)
    }

    var permission by remember {
        mutableStateOf(Manifest.permission.SCHEDULE_EXACT_ALARM)
    }


    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { result: Boolean ->
            if (!result) {
                if (!context.shouldShowRequestPermissionRationale(permission)) {
                    goToSettings = true
                }
                showDialog = true
            }
        }
    )
    if (showDialog) {
        PermissionRationalDialog(
            onDismissRequest = { showDialog = false },
            onConfirmRequest = {
                showDialog = false
                if (goToSettings) {
                    showDialog = false
                    Intent().also { intent ->
                        intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                        context.startActivity(intent)
                    }
                    showDialog = false
                } else {

                    requestPermissionLauncher.launch(permission)
                }

            }
        )
    }

    val isGranted =
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.S) {
            permission = Manifest.permission.SCHEDULE_EXACT_ALARM
            context.checkSelfPermission(Manifest.permission.SCHEDULE_EXACT_ALARM) == PackageManager.PERMISSION_GRANTED
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.USE_EXACT_ALARM
            context.checkSelfPermission(Manifest.permission.USE_EXACT_ALARM) == PackageManager.PERMISSION_GRANTED
        } else true


    if (isGranted) {
        showDialog = false
        onGrantedShowScreen()
    } else {
        if (context.shouldShowRequestPermissionRationale(permission)) {
            showDialog = true
        } else {
            SideEffect {
                requestPermissionLauncher.launch(permission)

            }

        }
    }


}


@Composable
fun PermissionRationalDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(onClick = onConfirmRequest) {
                Text(text = stringResource(id = R.string.cancel_btn))
            }
        },
        title = {
            Text(text = stringResource(id = R.string.permission_msg_title))
        },
        text = {
            Text(text = stringResource(id = R.string.permission_msg_content))
        }
    )

}