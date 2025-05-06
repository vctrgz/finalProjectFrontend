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
import kotlinx.coroutines.CoroutineScope
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

    // search for room and care records when roomId changes
    LaunchedEffect(roomId) {
        withContext(Dispatchers.IO) {
            room = roomDao.getRoomById(roomId)
            careRecords = careDao.getByRoomId(roomId)
        }
    }

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

        // show info
        if (room != null) {
            Text("Room: ${room!!.name}", style = MaterialTheme.typography.titleMedium)
            Text("Patient: ${room!!.patientName}", style = MaterialTheme.typography.bodyLarge)
            Text("Diagnosis: ${room!!.diagnosis}", style = MaterialTheme.typography.bodyLarge)
            Text("Observations: ${room!!.observaciones ?: "None"}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))
        } else {
            // if room is null, show loading message
            Text("Loading room info...", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(12.dp))
        }

        Divider()

        Spacer(modifier = Modifier.height(8.dp))
        Text("Care Records", style = MaterialTheme.typography.titleMedium)


        if (careRecords.isNotEmpty()) {
            careRecords.forEach { record ->
                Card(modifier = Modifier.padding(8.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Date: ${record.date}")
                        Text("Type: ${record.type}")

                        val bpParts = record.bloodPressure.split("/")
                        val systolic = bpParts.getOrNull(0)?.toIntOrNull()
                        val diastolic = bpParts.getOrNull(1)?.toIntOrNull()
                        val bpColor = if (
                            (systolic != null && (systolic > 140 || systolic < 90)) ||
                            (diastolic != null && (diastolic >= 90 || diastolic < 50))
                        ) Color.Red else Color.Unspecified

                        Text("Blood Pressure: ${record.bloodPressure}", color = bpColor)


                        record.respiratoryRate?.let {
                            val color = if (it < 12 || it > 20) Color.Red else Color.Unspecified
                            Text("Respiratory Rate: $it", color = color)
                        }


                        record.pulse?.let {
                            val color = if (it < 50 || it > 100) Color.Red else Color.Unspecified
                            Text("Pulse: $it", color = color)
                        }


                        record.temperature?.let {
                            val color = if (it < 34.9f || it > 38.5f) Color.Red else Color.Unspecified
                            Text("Temperature: $it ºC", color = color)
                        }


                        record.oxygenSaturation?.let {
                            val color = if (it < 94) Color.Red else Color.Unspecified
                            Text("Oxygen Saturation: $it%", color = color)
                        }
                        record.dietTexture?.let {
                            Text("Diet Texture: $it")
                        }
                        record.dietType?.let {
                            Text("Diet Type: $it")
                        }
                        record.hygiene?.let {
                            Text("Hygiene: $it")
                        }
                        record.sedestation?.let {
                            Text("Sedestation: $it")
                        }
                        record.walking?.let {
                            Text("Walking: $it")
                        }
                        record.posture?.let {
                            Text("Posture: $it")
                        }
                        record.drainage?.let {
                            Text("Drainage: $it")
                        }
                        record.shiftNote?.let {
                            Text("Shift Observations: $it")
                        }

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
                onSave = { date, type, bp, resp, pulse, temp, oxy, dietTexture, dietType,hygiene, sedestation, walking, posture, drainage, shiftNote, note ->
                    showDialog = false
                    CoroutineScope(Dispatchers.IO).launch {
                        careDao.insert(
                            CareRecord(
                                roomId = roomId,
                                date = date,
                                type = type,
                                bloodPressure = bp,
                                respiratoryRate = resp?.toIntOrNull(),
                                pulse = pulse?.toIntOrNull(),
                                temperature = temp?.toFloatOrNull(),
                                oxygenSaturation = oxy?.toIntOrNull(),
                                dietTexture = dietTexture,
                                dietType = dietType,
                                hygiene = hygiene,
                                sedestation = sedestation,
                                walking = walking,
                                posture = posture,
                                drainage = drainage,
                                shiftNote = shiftNote,
                                note = note
                            )
                        )
                        careRecords = careDao.getByRoomId(roomId)
                    }
                }
            )

        }
    }
}
