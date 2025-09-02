package com.example.meditrack.feature.presentation.screens.analysis_screen.component

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.meditrack.feature.domain.model.Medicine
import com.example.meditrack.ui.theme.AppThemeSettings
import com.example.meditrack.ui.theme.dimens
import com.example.meditrack.ui.theme.pieChartColors

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeAnalysisPage(context: Context, todayMedicine: List<Medicine>) {
    val totalColors = pieChartColors.size

    val sortedMedicine = todayMedicine.sortedByDescending { it.getStartEndDateDifferenceInDays() }

    Column {

        val pieChartBgGradient = MaterialTheme.colorScheme.onBackground.copy(alpha = .1f)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    4.dp,
                    RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
                    spotColor = if (AppThemeSettings.isDarkTheme) Color.White.copy(alpha = .5f) else Color.Black.copy(
                        .3f
                    )

                )
                .padding(bottom = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            val data = sortedMedicine.map { it.getStartEndDateDifferenceInDays() }
            CustomPieChart(
                data = data,
                pieChartSize = MaterialTheme.dimens.analysisDimens.timePieChartSize
            )

        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.analysisDimens.spacerBetweenPieChartAndItems))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            itemsIndexed(items = sortedMedicine) { index, item ->
                PieChartItemComponent(
                    medicine = item,
                    itemColor = pieChartColors[index % totalColors],
                    animDelay = index * 100,
                    context = context
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.analysisDimens.spacerBetweenLazyItems))
            }
        }
    }
}