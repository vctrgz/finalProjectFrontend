package com.example.healhub

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healhub.database.RoomEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalScreen(
    rooms: List<RoomEntity>,
    onLogout: () -> Unit,
    onRoomClick: (RoomEntity) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("HealHub") },
            actions = {
                TextButton(onClick = onLogout) {
                    Text("Logout", color = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF4CB8B3)
            )
        )

        Column(modifier = Modifier.padding(16.dp)) {
            rooms.forEach { room ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onRoomClick(room) },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7F5))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Room ${room.name}", fontSize = 18.sp, color = Color.Black)
                        Text(text = "Patient: ${room.patientName}", color = Color.DarkGray)
                        Text(text = "Diagnosis: ${room.diagnosis}", color = Color.DarkGray)
                        Text(
                            text = "Observations: ${room.observaciones ?: "None"}",
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}
