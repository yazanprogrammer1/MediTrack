package com.example.meditrack.feature.presentation.screens.medicines_screen.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.meditrack.feature.domain.model.Medicine
import com.example.meditrack.ui.theme.AppThemeSettings
import com.example.meditrack.ui.theme.CoolRed2
import com.example.meditrack.ui.theme.DarkPrimaryColor
import com.example.meditrack.ui.theme.LightPrimaryColor
import com.example.meditrack.ui.theme.dimens


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun SomeUnCompletedMedicineCard(
    medicine: Medicine = Medicine.DEFAULT_TASK,
) {

    val cardColor = CoolRed2

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = {},
    ) {
        Column(
            modifier = Modifier
                .height(MaterialTheme.dimens.homeScreenDimens.uncompletedTaskCardHeight)
                .width(MaterialTheme.dimens.homeScreenDimens.uncompletedTaskCardWidth)
                .padding(MaterialTheme.dimens.homeScreenDimens.uncompletedTaskCardPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = medicine.name,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.homeScreenDimens.uncompletedCardSpacerBetweenTaskTitleAndTaskDes))

            Text(
                text = medicine.description,
                overflow = TextOverflow.Ellipsis,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                maxLines = 2,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.homeScreenDimens.uncompletedCardSpacerBetweenDescriptionAndTime))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    modifier = Modifier.size(MaterialTheme.dimens.homeScreenDimens.uncompletedTimeIconSize)
                )

                Text(
                    text = medicine.getFormattedTime(),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    modifier = Modifier.padding(start = MaterialTheme.dimens.homeScreenDimens.uncompletedTaskPaddingBetweenTimeIconAndTimeTxt)
                )
            }

        }
    }

}