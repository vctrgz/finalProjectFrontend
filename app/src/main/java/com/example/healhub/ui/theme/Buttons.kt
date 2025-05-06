package com.example.healhub.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val GreenColor = Color(0xFF4CB8B3)

@Composable
fun GreenButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = GreenColor),
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
    ) {
        Text(text, color = Color.White)
    }
}

@Composable
fun GreenOutlinedButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenColor),
        modifier = modifier
    ) {
        Text(text)
    }
}
