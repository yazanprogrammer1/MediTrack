package com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.component

import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.meditrack.ui.theme.dimens

@Preview
@Composable
fun MedicineTextField(
    title: String = "",
    text: String = "",
    hint: String = "",
    onValueChange: (String) -> Unit = {},
    isHintVisible: Boolean = true,
    onFocusChanged: (FocusState) -> Unit = {},
    height: Dp = MaterialTheme.dimens.addEditScreenDimens.taskTextFieldHeight,
    roundedCornerSize: Dp = 16.dp,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    textStyle: TextStyle = TextStyle.Default,
    icon: (@Composable () -> Unit)? = null,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    contentAlignment: Alignment.Vertical = Alignment.CenterVertically,
    paddingValues: PaddingValues = PaddingValues(horizontal = 16.dp),
    modifier: Modifier = Modifier, // <- أضفنا الـ modifier هنا
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            modifier = Modifier.onFocusChanged {
                onFocusChanged(it)

            },
            decorationBox = {
                Row(
                    verticalAlignment = contentAlignment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height)
                        .clip(RoundedCornerShape(roundedCornerSize))
                        .background(Color(0xFFEAF9F7))
                        .padding(paddingValues)
                ) {
                    if (icon != null) {
                        icon()
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Box(
                        contentAlignment = Alignment.TopStart
                    ) {
                        it()
                        if (isHintVisible) {
                            Text(
                                text = hint,
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                color = Color.Black.copy(alpha = .5f)
                            )
                        }

                    }
                }
            },
            singleLine = singleLine,
            maxLines = maxLines,
            textStyle = textStyle,
            readOnly = readOnly,
            enabled = enabled
        )
    }

}