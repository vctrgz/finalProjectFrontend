package com.example.healhub.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.healhub.ui.theme.GreenButton
import com.example.healhub.ui.theme.GreenOutlinedButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientMenuScreen(
    roomId: Int,
    onBack: () -> Unit,
    onNavigateToPersonal: (roomId: Int) -> Unit,
    onNavigateToMedical: (roomId: Int) -> Unit,
    onNavigateToCare: (roomId: Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Patient Menu") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CB8B3))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GreenButton(
                text = "Personal Data",
                onClick = { onNavigateToPersonal(roomId) },
                modifier = Modifier.fillMaxWidth()
            )

            GreenButton(
                text = "Medical Data",
                onClick = { onNavigateToMedical(roomId) },
                modifier = Modifier.fillMaxWidth()
            )

            GreenButton(
                text = "Care Data",
                onClick = { onNavigateToCare(roomId) },
                modifier = Modifier.fillMaxWidth()
            )

            GreenOutlinedButton(
                text = "Back",
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}
