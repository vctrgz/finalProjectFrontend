package com.example.healhub

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.healhub.ui.theme.GreenButton
import com.example.healhub.ui.theme.GreenOutlinedButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCareDialog(
    onDismiss: () -> Unit,
    onSave: (
        date: String,
        bp: String,
        resp: String?,
        pulse: String?,
        temp: String?,
        oxy: String?,
        note: String
    ) -> Unit

) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var date by remember { mutableStateOf("") }
    var bloodPressure by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var respiratoryRate by remember { mutableStateOf("") }
    var pulse by remember { mutableStateOf("") }
    var temperature by remember { mutableStateOf("") }
    var oxygenSaturation by remember { mutableStateOf("") }

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                date = "%02d/%02d/%04d".format(day, month + 1, year)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    val careTypes = listOf("Routine Check", "Emergency", "Medication", "Therapy")
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            GreenButton(
                text = "Save",
                onClick = {
                    if (
                        date.isNotBlank() &&
                        bloodPressure.isNotBlank() &&
                        note.isNotBlank()
                    ) {
                        showError = false
                        onSave(
                            LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString(),                            bloodPressure,
                            respiratoryRate.ifBlank { null },
                            pulse.ifBlank { null },
                            temperature.ifBlank { null },
                            oxygenSaturation.ifBlank { null },
                            note
                        )
                    } else {
                        showError = true
                    }
                }
            )
        },
        dismissButton = {
            GreenOutlinedButton(
                text = "Cancel",
                onClick = onDismiss
            )
        },
        title = { Text("New Care Entry") },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = date,
                    onValueChange = {},
                    label = { Text("Date") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { datePickerDialog.show() }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = bloodPressure,
                    onValueChange = {
                        bloodPressure = it.filter { c -> c.isDigit() || c == '/' }
                        if (bloodPressure.length == 3 && !bloodPressure.contains("/")) {
                            bloodPressure = bloodPressure + "/"
                        }
                    },
                    label = { Text("Blood Pressure (e.g., 120/80)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = respiratoryRate,
                    onValueChange = { respiratoryRate = it.filter { c -> c.isDigit() } },
                    label = { Text("Respiratory Rate (e.g., 18)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = pulse,
                    onValueChange = { pulse = it.filter { c -> c.isDigit() } },
                    label = { Text("Pulse (e.g., 72)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = temperature,
                    onValueChange = { temperature = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("Temperature (e.g., 36.5)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = oxygenSaturation,
                    onValueChange = { oxygenSaturation = it.filter { c -> c.isDigit() } },
                    label = { Text("Oxygen Saturation (e.g., 96)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Notes") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    singleLine = false,
                    maxLines = 5
                )


                if (showError) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("\u26A0 Please complete all fields", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    )
}