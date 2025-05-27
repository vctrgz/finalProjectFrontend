package com.example.healhub.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healhub.ui.remote.RemoteRoomListState
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.font.FontWeight
import com.example.healhub.AppViewModel
import com.example.healhub.ui.dataClasses.Room


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalScreen(
    viewModel: AppViewModel,
    onLogout: () -> Unit,
    onRoomClick: (Room) -> Unit
) {
    val remoteRoomListState = viewModel.remoteRoomListState

    LaunchedEffect(Unit) {
        viewModel.getRemoteAllRooms()
    }
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
        Column {
            when (remoteRoomListState) {
                is RemoteRoomListState.Cargant -> {
                    Text(text = "Loading rooms...", color = Color.Gray)
                }
                is RemoteRoomListState.Success -> {
                    LazyColumn {
                        items(items = remoteRoomListState.roomList, key = { it.habitacionId }) { room ->

                        Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable { onRoomClick(room) },
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7F5))
                            ) {
                            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                                Text(
                                    "Room ${room.habitacionId}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                if (room.paciente != null) {
                                    Text("Patient: ${room.paciente.nombre} ${room.paciente.apellidos}", style = MaterialTheme.typography.bodyLarge)
                                } else {
                                    Text("Patient: No asignado", style = MaterialTheme.typography.bodyLarge)
                                }
                                Text(
                                    "Observations: ${room.observaciones ?: "None"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            }
                            }
                        }
                    }

                }
                is RemoteRoomListState.Error -> {
                    Text(text = "Error fetching nurses", color = Color.Red)
                }
            }
        }

    }
}

