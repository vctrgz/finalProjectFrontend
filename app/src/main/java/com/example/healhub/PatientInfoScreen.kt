package com.example.healhub.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.healhub.database.AppDatabase
import com.example.healhub.database.CareRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientInfoScreen(roomId: Int, onBack: () -> Unit) {
    val context = LocalContext.current
    val dao = AppDatabase.getInstance(context).careRecordDao()
    var careRecords by remember { mutableStateOf(listOf<CareRecord>()) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(roomId) {
        careRecords = dao.getByRoomId(roomId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(
            title = { Text("Patient Info") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CB8B3))
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Care Records", style = MaterialTheme.typography.titleMedium)

        careRecords.forEach {
            Card(modifier = Modifier.padding(8.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Date: ${it.date}")
                    Text("Type: ${it.type}")
                    Text("Blood Pressure: ${it.bloodPressure}")
                    Text("Note: ${it.note}")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        FloatingActionButton(onClick = { showDialog = true }) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }

        if (showDialog) {
            AddCareDialog(
                onDismiss = { showDialog = false },
                onSave = { date, type, bp, note ->
                    showDialog = false
                    CoroutineScope(Dispatchers.IO).launch {
                        dao.insert(
                            CareRecord(
                                roomId = roomId,
                                date = date,
                                type = type,
                                bloodPressure = bp,
                                note = note
                            )
                        )
                        careRecords = dao.getByRoomId(roomId)
                    }
                }
            )
        }
    }
}
