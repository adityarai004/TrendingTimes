package com.example.trendingtimes.auth.presentation.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.trendingtimes.R
import com.example.trendingtimes.core.ui.MediumTitleText

@Composable
fun CustomButton(modifier: Modifier = Modifier, onClick: () -> Unit, text: String) {
    ElevatedButton(
        onClick = onClick, modifier = modifier, colors = ButtonDefaults.elevatedButtonColors()
            .copy(containerColor = colorResource(R.color.purple_200))
    ) {
        MediumTitleText(text = text, modifier = Modifier.padding(6.dp))
    }
}