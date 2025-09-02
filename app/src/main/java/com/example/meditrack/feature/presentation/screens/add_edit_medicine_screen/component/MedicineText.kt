package com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.meditrack.ui.theme.dimens

@Preview
@Composable
fun MedicineText(
    modifier: Modifier = Modifier,
    title: String = "",
    text: String = "",
    onValueChange: (String) -> Unit = {},
    height: Dp = MaterialTheme.dimens.addEditScreenDimens.taskTextFieldHeight,
    roundedCornerSize: Dp = 16.dp,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    textStyle: TextStyle = TextStyle.Default,
    icon: (@Composable () -> Unit)? = null,
    readOnly: Boolean = true,
    contentAlignment: Alignment.Vertical = Alignment.CenterVertically,
    paddingValues: PaddingValues = PaddingValues(horizontal = 16.dp),
    clickable: Boolean = true,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
) {

    Column(
        modifier = modifier
    ) {
        // العنوان فوق (زي: Medicine Name, Strength ...)
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            decorationBox = {
                Row(
                    verticalAlignment = contentAlignment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height)
                        .clip(RoundedCornerShape(roundedCornerSize))
                        .background(Color(0xFFEAF9F7))
                        .clickable(
                            enabled = clickable,
                            onClick = onClick
                        )
                        .padding(paddingValues)
                ) {
                    if (icon != null) {
                        icon()
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Box(
                        contentAlignment = Alignment.TopStart,
                        modifier = Modifier.weight(1f)
                    ) {
                        it()
                    }
                }
            },
            singleLine = singleLine,
            maxLines = maxLines,
            textStyle = textStyle,
            readOnly = readOnly,
            enabled = enabled,
        )
    }

}