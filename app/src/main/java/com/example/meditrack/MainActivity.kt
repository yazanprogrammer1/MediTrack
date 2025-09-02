package com.example.meditrack

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.meditrack.feature.presentation.common.AppBottomBar
import com.example.meditrack.feature.presentation.common.AppDrawer
import com.example.meditrack.feature.presentation.common.AppTopBar
import com.example.meditrack.feature.presentation.navgraph.Routs
import com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.AddEditMedicineViewModel
import com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.AddEditMedicinesScreen
import com.example.meditrack.feature.presentation.screens.analysis_screen.AnalysisScreen
import com.example.meditrack.feature.presentation.screens.analysis_screen.AnalysisViewModel
import com.example.meditrack.feature.presentation.screens.instructions_before_use_screen.InstructionsBeforeUseScreen
import com.example.meditrack.feature.presentation.screens.medicines_screen.MedicinesScreen
import com.example.meditrack.feature.presentation.screens.medicines_screen.MedicinesViewModel
import com.example.meditrack.feature.presentation.screens.onboarding.OnBoardingScreen
import com.example.meditrack.feature.presentation.screens.onboarding.OnBoardingViewModel
import com.example.meditrack.feature.presentation.screens.request_permission_screen.RequestPermissionScreen
import com.example.meditrack.ui.theme.AppThemeSettings
import com.example.meditrack.ui.theme.MediTrackTheme
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var dataStore: DataStore<Preferences>
    lateinit var alarmService: AlarmService
    private var isBound by mutableStateOf(false)
    private val viewModel by viewModels<MainViewModel>()

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            val binder = service as AlarmService.AlarmBinder
            alarmService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                showPermissionDeniedDialog()
            }
        }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            dataStore.data.collectLatest { preferences ->
                val key = booleanPreferencesKey("isDarkTheme")
                Log.e("Yz", "Theme: ${preferences[key]}")
                AppThemeSettings.isDarkTheme = preferences[key] ?: false
            }

        }

        // Splash Screen
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.splashCondition
            }
        }

        setContent {
            MediTrackTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val coroutineScope = rememberCoroutineScope()
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val snackbarHostState = remember {
                        SnackbarHostState()
                    }
                    val currentBackStackEntry by navController.currentBackStackEntryAsState()
                    val isBottomBarVisible by remember {
                        derivedStateOf { currentBackStackEntry?.destination?.route == Routs.Medicine.rout || currentBackStackEntry?.destination?.route == Routs.Analysis.rout }
                    }
                    val isTopBarVisible by remember {
                        derivedStateOf { currentBackStackEntry?.destination?.route == Routs.Medicine.rout }
                    }
                    val context = LocalContext.current
                    var isSnackBarActionVisible by remember {
                        mutableStateOf(false)
                    }
                    RequestPermissionScreen(context = this) {
                        ModalNavigationDrawer(
                            drawerContent = {
                                ModalDrawerSheet {
                                    AppDrawer(
                                        dataStore = dataStore,
                                        coroutineScope = coroutineScope,
                                        navController = navController,
                                        drawerState = drawerState
                                    )
                                }
                            },
                            drawerState = drawerState,
                        ) {
                            Scaffold(bottomBar = {
                                AnimatedVisibility(
                                    visible = isBottomBarVisible,
                                    enter = slideInVertically(
                                        initialOffsetY = {
                                            it
                                        }),
                                    exit = slideOutVertically(
                                        targetOffsetY = { it }, animationSpec = tween(
                                            durationMillis = 100
                                        )
                                    )
                                ) {
                                    AppBottomBar(
                                        onNavIconClicked = {
                                            navController.navigate(it.rout) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }, onAddClicked = {
                                            navController.navigate(Routs.AddEdit.rout)
                                        }, navController = navController
                                    )
                                }


                            }, topBar = {
                                AnimatedVisibility(
                                    visible = isTopBarVisible, enter = slideInVertically(
                                        initialOffsetY = { -it },
                                    ), exit = slideOutVertically(
                                        targetOffsetY = { -it }, animationSpec = tween(
                                            100
                                        )
                                    )
                                ) {
                                    AppTopBar { coroutineScope.launch { drawerState.open() } }
                                }


                            }, snackbarHost = {
                                SnackbarHost(
                                    hostState = snackbarHostState,
                                ) {
                                    Snackbar(
                                        action = {
                                            if (isSnackBarActionVisible) {
                                                TextButton(
                                                    onClick = { it.performAction() }) {
                                                    Text(
                                                        text = stringResource(id = R.string.undo),
                                                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                                                    )
                                                }
                                            }
                                        }) {
                                        Text(text = it.visuals.message)
                                    }
                                }
                            }) { paddingValues ->

                                NavHost(
                                    navController = navController,
                                    startDestination = Routs.Medicine.rout,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    composable(
                                        route = Routs.OnBoardingScreen.rout
                                    ) {
                                        val viewModel: OnBoardingViewModel = hiltViewModel()
                                        OnBoardingScreen(
                                            event = {
                                                viewModel.onEvent(it)
                                            })
                                    }
                                    composable(Routs.Medicine.rout) {

                                        val tasksViewModel = hiltViewModel<MedicinesViewModel>()

                                        if (isBound) {
                                            MedicinesScreen(
                                                navController = navController,
                                                drawerState = drawerState,
                                                scope = coroutineScope,
                                                snackbarHostState = snackbarHostState,
                                                paddingValues = paddingValues,
                                                state = tasksViewModel.state.value,
                                                onEvent = tasksViewModel::onEvent,
                                                uiEvent = tasksViewModel.uiEvent,
                                                context = context,
                                                alarmService = alarmService,
                                                dataStore = dataStore,
                                                onChangeSnackActionState = {
                                                    isSnackBarActionVisible = true
                                                })
                                        }


                                    }

                                    composable(Routs.Analysis.rout) {
                                        val analysisViewModel = hiltViewModel<AnalysisViewModel>()
                                        AnalysisScreen(
                                            navController = navController,
                                            coroutineScope = coroutineScope,
                                            drawerState = drawerState,
                                            paddingValues = paddingValues,
                                            todayMedicine = analysisViewModel.todayMedicines.value
                                        )
                                    }

                                    composable(
                                        route = Routs.AddEdit.rout + "?medicineId={medicineId}",
                                        arguments = listOf(
                                            navArgument("medicineId") {
                                                type = NavType.IntType
                                                defaultValue = -1
                                            }),
                                        enterTransition = {
                                            slideInHorizontally(
                                                animationSpec = tween(
                                                    durationMillis = 500, easing = EaseIn
                                                ), initialOffsetX = { -it })
                                        },
                                        exitTransition = {
                                            slideOutVertically(
                                                animationSpec = tween(
                                                    durationMillis = 500, easing = EaseInOut
                                                ), targetOffsetY = { it })
                                        },
                                    ) {
                                        val addEditViewModel =
                                            hiltViewModel<AddEditMedicineViewModel>()
                                        AddEditMedicinesScreen(
                                            onEvent = addEditViewModel::onEvent,
                                            isRestEnabled = addEditViewModel.restButtonEnabled.value,
                                            navController = navController,
                                            snackbarHostState = snackbarHostState,
                                            paddingValues = paddingValues,
                                            medicineName = addEditViewModel.medicineName.value,
                                            medicineDescription = addEditViewModel.medicineDescription.value,
                                            medicineStrength = addEditViewModel.medicineStrength.value,
                                            medicineWhenToTake = addEditViewModel.medicineWhenToTake.value,
                                            medicineType = addEditViewModel.medicineType.value,
                                            medicineAmount = addEditViewModel.medicineAmount.value,
                                            medicineFrequency = addEditViewModel.medicineFrequency.value,
                                            selectedDays = addEditViewModel.medicineDays,
                                            medicineEndDate = addEditViewModel.medicineEndDate.value,
                                            medicineStartDate = addEditViewModel.medicineStartDate.value,
                                            medicineStartTime = addEditViewModel.medicineStartTime.value,
                                            medicinePriority = addEditViewModel.medicinePriority.value,
                                            uiEvent = addEditViewModel.eventFlow,
                                            pickerInitialStartDate = addEditViewModel.pickerInitialStartDate.value,
                                            pickerInitialEndDate = addEditViewModel.pickerInitialEndDate.value,
                                            onChangeSnackActionState = {
                                                isSnackBarActionVisible = false
                                            },
                                            calenderStartTime = addEditViewModel.calenderStartTime.value
                                        )
                                    }

                                    composable(Routs.Instructions.rout) {
                                        InstructionsBeforeUseScreen()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                // ✅ عنده إذن بالفعل
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // ما نعمل شيء
                }

                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        MaterialAlertDialogBuilder(this).setTitle("إذن الإشعارات")
            .setMessage("لتستفيد من التذكيرات بشكل صحيح، يرجى تفعيل إشعارات التطبيق.")
            .setPositiveButton("فتح الإعدادات") { dialog, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
                dialog.dismiss()
            }.setCancelable(false).show()
    }


    override fun onStart() {
        super.onStart()
        checkAndRequestNotificationPermission()
        Intent(this, AlarmService::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }
}