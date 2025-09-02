package com.example.meditrack.feature.presentation.screens.instructions_before_use_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.meditrack.R
import com.example.meditrack.ui.theme.dimens

@Composable
fun InstructionsBeforeUseScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        // العنوان الرئيسي
        Text(
            text = stringResource(id = R.string.instructions_before_use),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        )

        // --- Instruction 1 ---
        InstructionCard(
            number = 1,
            text = stringResource(id = R.string.instruction_1),
            extraContent = {
                Image(
                    painter = painterResource(id = R.drawable.deleteimg),
                    contentDescription = "swipe to delete gif",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(MaterialTheme.dimens.instructionScreenSwipeDeleteGifDimen)
                        .padding(top = 8.dp)
                )
            }
        )

        // --- Instruction 2 ---
        InstructionCard(
            number = 2,
            text = stringResource(id = R.string.instruction_2),
        )

        // --- Instruction 3 ---
        InstructionCard(
            number = 3,
            text = stringResource(id = R.string.instruction_3),
        )
    }
}

@Composable
private fun InstructionCard(
    number: Int,
    text: String,
    extraContent: (@Composable () -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // الرقم
            Text(
                text = "$number.",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            extraContent?.invoke()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InstructionsBeforeUseScreenPrev() {
    InstructionsBeforeUseScreen()
}
