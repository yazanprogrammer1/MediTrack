package com.example.meditrack.feature.presentation.screens.analysis_screen

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.meditrack.R
import com.example.meditrack.feature.domain.model.Medicine
import com.example.meditrack.feature.presentation.screens.analysis_screen.component.AnalyticsCompleting
import com.example.meditrack.feature.presentation.screens.analysis_screen.component.TimeAnalysisPage
import com.example.meditrack.ui.theme.DarkPrimaryColor
import com.example.meditrack.ui.theme.LightPrimaryColor
import com.example.meditrack.ui.theme.dimens
import kotlinx.coroutines.CoroutineScope

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnalysisScreen(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    context: Context = LocalContext.current,
    paddingValues: PaddingValues = PaddingValues(),
    todayMedicine: List<Medicine>,
) {
    val completedMedicines = remember(todayMedicine) {
        todayMedicine.filter { it.isCompleted }
    }

    val missedMedicines = remember(todayMedicine) {
        todayMedicine.filter { it.isMissed } // حقل عندك بالـ Medicine
    }

    val unCompletedMedicines = remember(todayMedicine) {
        todayMedicine.filter { !it.isCompleted }
    }

    val tabItems = listOf(stringResource(id = R.string.daily_report), stringResource(id = R.string.timechart))
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    val pagerState = rememberPagerState {
        tabItems.size
    }

    LaunchedEffect(key1 = selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    LaunchedEffect(key1 = pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }


    Column(Modifier.padding(paddingValues)) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            indicator = { tabPositions ->
                if (selectedTabIndex < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = DarkPrimaryColor
                    )
                }
            },
        ) {
            tabItems.forEachIndexed { index, item ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(MaterialTheme.dimens.analysisDimens.tapPadding)
                        )
                    },
                    selectedContentColor = DarkPrimaryColor,
                    unselectedContentColor = LightPrimaryColor
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.analysisDimens.pagerPadding)
                .weight(1f)
        ) { index ->
            if (index == 0) {
                AnalyticsCompleting(
                    doneProgressStateProvider = {
                        if (todayMedicine.isNotEmpty())
                            completedMedicines.size / todayMedicine.size.toFloat()
                        else 0f
                    },
                    missedProgressStateProvider = {
                        if (todayMedicine.isNotEmpty())
                            missedMedicines.size / todayMedicine.size.toFloat()
                        else 0f
                    }
                )
            } else {
                TimeAnalysisPage(context = context, todayMedicine = todayMedicine)
            }
        }
    }


}




