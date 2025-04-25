package com.example.healhub

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.healhub.database.AppDatabase
import com.example.healhub.database.CareRecord
import com.example.healhub.database.CareRecordDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CareDataView(context: Context, roomId: Int) {
    val dao = remember { AppDatabase.getInstance(context).careRecordDao() }
    var showDialog by remember { mutableStateOf(false) }
    var careRecords by remember { mutableStateOf(listOf<CareRecord>()) }

    LaunchedEffect(roomId) {
        withContext(Dispatchers.IO) {
            careRecords = dao.getByRoomId(roomId)
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        if (careRecords.isNotEmpty()) {
            BloodPressureChart(careRecords)
        }
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

