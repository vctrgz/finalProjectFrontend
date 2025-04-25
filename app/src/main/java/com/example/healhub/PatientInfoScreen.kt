package com.example.healhub.ui

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
import com.example.healhub.AddCareDialog
import com.example.healhub.database.AppDatabase
import com.example.healhub.database.CareRecord
import com.example.healhub.database.RoomEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientInfoScreen(roomId: Int, onBack: () -> Unit) {
    val context = LocalContext.current
    val careDao = AppDatabase.getInstance(context).careRecordDao()
    val roomDao = AppDatabase.getInstance(context).roomDao()

    var careRecords by remember { mutableStateOf(listOf<CareRecord>()) }
    var room by remember { mutableStateOf<RoomEntity?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    // 查询房间详情和护理记录
    LaunchedEffect(roomId) {
        withContext(Dispatchers.IO) {
            room = roomDao.getRoomById(roomId)
            careRecords = careDao.getByRoomId(roomId)
        }
    }

    // 整体滚动列（根据数据需要你可以用 LazyColumn 等）
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

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

        // 显示房间信息
        if (room != null) {
            Text("Room: ${room!!.name}", style = MaterialTheme.typography.titleMedium)
            Text("Patient: ${room!!.patientName}", style = MaterialTheme.typography.bodyLarge)
            Text("Diagnosis: ${room!!.diagnosis}", style = MaterialTheme.typography.bodyLarge)
            Text("Observations: ${room!!.observaciones ?: "None"}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))
        } else {
            // 如果未加载到房间信息，显示占位提示
            Text("Loading room info...", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(12.dp))
        }

        Divider()

        Spacer(modifier = Modifier.height(8.dp))
        Text("Care Records", style = MaterialTheme.typography.titleMedium)

        // 显示护理记录
        if (careRecords.isNotEmpty()) {
            careRecords.forEach { record ->
                Card(modifier = Modifier.padding(8.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Date: ${record.date}")
                        Text("Type: ${record.type}")
                        Text("Blood Pressure: ${record.bloodPressure}")
                        Text("Note: ${record.note}")
                    }
                }
            }
        } else {
            Text("No care records found.", color = Color.Gray)
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
                    // 新增记录操作在 IO 线程中执行
                    kotlinx.coroutines.CoroutineScope(Dispatchers.IO).launch {
                        careDao.insert(
                            CareRecord(
                                roomId = roomId,
                                date = date,
                                type = type,
                                bloodPressure = bp,
                                note = note
                            )
                        )
                        // 更新护理记录数据
                        careRecords = careDao.getByRoomId(roomId)
                    }
                }
            )
        }
    }
}
