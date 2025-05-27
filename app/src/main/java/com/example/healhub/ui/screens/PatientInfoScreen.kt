package com.example.healhub.ui

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.healhub.AddCareDialog
import com.example.healhub.AppViewModel
import com.example.healhub.VitalSignsChart
import com.example.healhub.ui.dataClasses.Care
import com.example.healhub.ui.dataClasses.VitalSigns
import com.example.healhub.ui.remote.RemoteCareListByPatientState
import com.example.healhub.ui.remote.RemoteFindRoomById
import com.example.healhub.ui.remote.RemoteSaveCareDataState
import com.example.healhub.ui.theme.GreenButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientInfoScreen(
    viewModel: AppViewModel,
    roomId: Int,
    onBack: () -> Unit
) {
    val remoteFindRoomById = viewModel.remoteFindRoomById
    val remoteCareListByPatientState = viewModel.remoteCareListByPatientState
    val remoteSaveCareDataState = viewModel.remoteSaveCareDataState

    val context = LocalContext.current
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    var expandedItems by remember { mutableStateOf(setOf<Int>()) }
    val careList = (remoteCareListByPatientState as? RemoteCareListByPatientState.Success)?.careList.orEmpty()
    val orderedCareList = careList.sortedByDescending { it.fechaRegistro }
    var newCare by remember { mutableStateOf<Care?>(null) }
    val room = (remoteFindRoomById as? RemoteFindRoomById.Success)?.room
    val patient = room?.paciente
    var showDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(newCare) {
        newCare?.let {
            viewModel.postRemoteSaveCare(it)
            newCare = null
        }
    }

    // 1. Obtener la habitación por ID
    LaunchedEffect(roomId) {
        viewModel.getByRoomID(roomId)
    }

    // 2. Cuando el paciente cambia (es decir, se obtiene exitosamente), obtener las curas
    LaunchedEffect(patient?.numHistorial) {
        patient?.let {
            viewModel.getRemoteCareListByPatient(it)
        }
    }
    LaunchedEffect(remoteSaveCareDataState) {
        when (remoteSaveCareDataState) {
            is RemoteSaveCareDataState.Success -> {
                Toast.makeText(context, "Care added", Toast.LENGTH_SHORT).show()
                patient?.let { viewModel.getRemoteCareListByPatient(it) } // <-- Aquí se recarga
            }
            is RemoteSaveCareDataState.Error -> {
                Toast.makeText(context, "Failed to save care", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = { Text("Patient Info") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF4CB8B3),
                titleContentColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(12.dp))
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            when (remoteFindRoomById) {
                is RemoteFindRoomById.Cargant -> {
                    Text("Loading room info...", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                is RemoteFindRoomById.Success -> {
                    Text(
                        "Room: ${room?.habitacionId}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text("Patient: ${patient?.nombre}", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        "Observations: ${room?.observaciones ?: "None"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                is RemoteFindRoomById.Error -> {
                    Text(text = "Error fetching room info", color = Color.Red)
                }
            }

            Divider()
            Spacer(modifier = Modifier.height(4.dp))

            // Título y espacio
            Text("Gráfica de constantes vitales", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(24.dp))
            // Contenedor de la gráfica con tamaño definido
            if (careList.any { it.constantesVitales.pulso != null || it.constantesVitales.temperatura != null || it.constantesVitales.frecuenciaRespiratoria != null}) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(bottom = 16.dp)
                ) {
                    VitalSignsChart(data = careList)
                }

            } else {
                Text("No hay datos suficientes para mostrar la gráfica.", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Care Records", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            GreenButton(
                text = "Add Care",
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth()
            )

            when (remoteCareListByPatientState) {
                is RemoteCareListByPatientState.Cargant -> {
                    Text("Loading cares info...", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                is RemoteCareListByPatientState.Success -> {
                    orderedCareList.forEach { record ->
                        val isExpanded = expandedItems.contains(record.registroId)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    expandedItems = (if (isExpanded) {
                                        expandedItems - record.registroId
                                    } else {
                                        expandedItems + record.registroId
                                    }) as Set<Int>
                                },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(
                                    horizontal = 20.dp,
                                    vertical = 12.dp
                                ))
                                {
                                    Text("Date: ${record.fechaRegistro}", fontWeight = FontWeight.Bold)


                                if (isExpanded) {
//                                    record.fechaRegistro?.let {
//                                    Text("Date: ${it.format()}", fontWeight = FontWeight.Bold)
//
//                                    }

                                    record.constantesVitales?.let { constantes ->
                                        val sistolica = constantes.sistolicaTA
                                        val diastolica = constantes.diastolicaTA

                                        if (sistolica != null && diastolica != null) {
                                            val bpColor = if (
                                                sistolica > 140 || sistolica < 90 ||
                                                diastolica >= 90 || diastolica < 50
                                            ) Color.Red else Color.Unspecified

                                            Text(
                                                "Blood Pressure: $sistolica/$diastolica",
                                                color = bpColor
                                            )
                                        } else {
                                            // Mostrar algo si falta alguno de los dos valores
                                            Text("Blood Pressure: N/A", color = Color.Gray)
                                        }
                                        record.constantesVitales.frecuenciaRespiratoria?.let {
                                            val color =
                                                if (it < 12 || it > 20) Color.Red else Color.Unspecified
                                            Text("Respiratory Rate: $it", color = color)
                                        }
                                        record.constantesVitales.pulso?.let {
                                            val color =
                                                if (it < 50 || it > 100) Color.Red else Color.Unspecified
                                            Text("Pulse: $it", color = color)
                                        }
                                        record.constantesVitales.temperatura?.let {
                                            val color =
                                                if (it < 34.9f || it > 38.5f) Color.Red else Color.Unspecified
                                            Text("Temperature: $it ºC", color = color)
                                        }
                                        record.constantesVitales.saturacionOxigeno?.let {
                                            val color =
                                                if (it < 94) Color.Red else Color.Unspecified
                                            Text("Oxygen Saturation: $it%", color = color)
                                        }
                                    } ?: run {
                                        // Mostrar mensaje si constantesVitales es completamente null
                                        Text("Vital signs not available", color = Color.Gray)
                                    }
                                    Text("Note: ${record.observaciones}")
                                }
                            }
                        }
                    }
                }

                is RemoteCareListByPatientState.Error -> {
                    Text("No care records found.", color = Color.Gray)
                }
            }

        }
        if (showDialog) {
            AddCareDialog(
                onDismiss = { showDialog = false },
                onSave = { date, bp, resp, pulse, temp, oxy, note ->
                    showDialog = false
                    val vitalSigns = VitalSigns(
                        sistolicaTA = bp.split("/").getOrNull(0)?.toFloatOrNull(),
                        diastolicaTA = bp.split("/").getOrNull(1)?.toFloatOrNull(),
                        frecuenciaRespiratoria = resp?.toFloatOrNull(),
                        pulso = pulse?.toFloatOrNull(),
                        temperatura = temp?.toFloatOrNull(),
                        saturacionOxigeno = oxy?.toFloatOrNull(),
                        peso = null,
                        talla = null,
                        diuresis = null,
                        deposiciones = null,
                        STP = null
                    )

                    newCare = Care(
                        registroId = null, // o lo que se necesite (por ejemplo, 0 si el backend lo genera)
                        paciente = patient,
                        constantesVitales = vitalSigns,
                        numTrabajador = viewModel.numeroTrabajador.toString(),
                        fechaRegistro = date.toString(),
                        thigId = 1,
                        observaciones = note
                    )


//                    LaunchedEffect(newCare) {
//                        viewModel.postRemoteSaveCare(newCare)
//                        viewModel.getRemoteCareListByPatient(patient)
//                    }
                }
            )
        }
    }
}

