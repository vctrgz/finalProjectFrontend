package com.example.healhub.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.healhub.AppViewModel
import com.example.healhub.ui.remote.RemoteFindRoomById
import com.example.healhub.ui.theme.GreenButton
import com.example.healhub.ui.theme.GreenOutlinedButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalDataScreen(
    viewModel: AppViewModel,
    roomId: Int,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val remoteFindRoomById = viewModel.remoteFindRoomById

    var hasUrinaryCatheter by remember { mutableStateOf(false) }
    var hasVenousCatheter by remember { mutableStateOf(false) }
    var needsOxygen by remember { mutableStateOf(false) }

    val dependencyOptions = listOf("Independent", "Partial", "Total")
    var selectedDependency by remember { mutableStateOf(dependencyOptions[0]) }
    var dependencyExpanded by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }

    // Carga los datos del paciente al iniciar
    LaunchedEffect(roomId) {
        viewModel.getByRoomID(roomId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Medical Data") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            if (remoteFindRoomById is RemoteFindRoomById.Success) {
                val room = remoteFindRoomById.room
                val patient = room.paciente
                val diagnosis = patient.diagnostico

                if (!isEditing) {
                    if (diagnosis != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                                Text("Diagnóstico: ${diagnosis.diaO2 ?: "N/A"}")
                                Text("Descripción diagnóstico: ${diagnosis.diaDesc ?: "N/A"}")
                                Text("Motivo: ${diagnosis.diaMotivo ?: "N/A"}")

                                if (diagnosis.diaO2 == 1) {
                                    Text("O2: Necesita oxígeno")
                                    Text("Descripción O2: ${diagnosis.diaO2Desc ?: ""}")
                                } else {
                                    Text("O2: No necesita oxígeno")
                                }

                                if (diagnosis.diaPanales == 1) {
                                    Text("Pañales: Necesita pañales")
                                } else {
                                    Text("Pañales: No necesita pañales")
                                }

                                if (diagnosis.diaSV == 1) {
                                    Text("Sonda Vesical: Necesita sonda")
                                    Text("Tipo SV: ${diagnosis.diaSVTipo ?: "N/A"}")
                                    Text("Débito SV: ${diagnosis.diaSVDebito ?: "N/A"}")
                                } else {
                                    Text("Sonda Vesical: No necesita sonda")
                                }

                                if (diagnosis.diaSR == 1) {
                                    Text("Sonda Rectal: Necesita sonda")
                                    Text("Débito SR: ${diagnosis.diaSRDebito ?: "N/A"}")
                                } else {
                                    Text("Sonda Rectal: No necesita sonda")
                                }

                                if (diagnosis.diaSNG == 1) {
                                    Text("Sonda Nasal Gástrica: Necesita sonda")
                                    Text("Descripción SNG: ${diagnosis.diaSNGDesc ?: "N/A"}")
                                } else {
                                    Text("Sonda Nasal Gástrica: No necesita sonda")
                                }
                            }
                        }
                    } else {
                        Text("No diagnosis data available")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    GreenButton(
                        text = "Edit",
                        onClick = { isEditing = true },
                        modifier = Modifier.fillMaxWidth()
                    )

                } else {
                    // Modo edición
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Urinary Catheter")
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(
                            checked = hasUrinaryCatheter,
                            onCheckedChange = { hasUrinaryCatheter = it }
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Venous Catheter")
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(
                            checked = hasVenousCatheter,
                            onCheckedChange = { hasVenousCatheter = it }
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Oxygen Therapy")
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(
                            checked = needsOxygen,
                            onCheckedChange = { needsOxygen = it }
                        )
                    }

                    Text("Dependency Level")
                    ExposedDropdownMenuBox(
                        expanded = dependencyExpanded,
                        onExpandedChange = { dependencyExpanded = !dependencyExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedDependency,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select level") },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = dependencyExpanded,
                            onDismissRequest = { dependencyExpanded = false }
                        ) {
                            dependencyOptions.forEach {
                                DropdownMenuItem(
                                    text = { Text(it) },
                                    onClick = {
                                        selectedDependency = it
                                        dependencyExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    GreenButton(
                        text = "Save",
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
//                                viewModel.saveMedicalData(
//                                    MedicalData(
//                                        roomId = roomId,
//                                        hasUrinaryCatheter = hasUrinaryCatheter,
//                                        hasVenousCatheter = hasVenousCatheter,
//                                        needsOxygen = needsOxygen,
//                                        dependencyLevel = selectedDependency
//                                    )
//                                )
                                withContext(Dispatchers.Main) {
                                    isEditing = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    GreenOutlinedButton(
                        text = "Cancel",
                        onClick = { isEditing = false },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            GreenOutlinedButton(
                text = "Back",
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
