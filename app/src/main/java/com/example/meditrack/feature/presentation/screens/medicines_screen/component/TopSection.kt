package com.example.meditrack.feature.presentation.screens.medicines_screen.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.meditrack.feature.domain.model.Medicine
import com.example.meditrack.ui.theme.dimens


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopSection(todayMedicines: List<Medicine>) {

    val numberOfTodayTasks = remember(todayMedicines) { todayMedicines.size }
    val numberOfCompletedTasks =
        remember(todayMedicines) { todayMedicines.filter { it.isCompleted }.size }
    val unTekenMedicine = remember(todayMedicines) { todayMedicines.filter { !it.isCompleted } }

    LazyRow(
        contentPadding = PaddingValues(MaterialTheme.dimens.homeScreenDimens.topSectionContentPadding)
    ) {
        item {
            CompletedMedicineCountCard(numberOfTodayTasks, numberOfCompletedTasks)
            Spacer(modifier = Modifier.width(MaterialTheme.dimens.homeScreenDimens.spacerBetweenLazyRowItems))
        }

        if (unTekenMedicine.isNotEmpty()) {
            item {
                SomeUnCompletedMedicineCard(unTekenMedicine[0])
                Spacer(modifier = Modifier.width(MaterialTheme.dimens.homeScreenDimens.spacerBetweenLazyRowItems))
            }
        }
    }

}