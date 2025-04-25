package com.example.healhub

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCareDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var date by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var bloodPressure by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

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
            TextButton(onClick = {
                if (date.isNotBlank() && type.isNotBlank() && bloodPressure.isNotBlank() && notes.isNotBlank()) {
                    showError = false
                    onSave(date, type, bloodPressure, notes)
                } else {
                    showError = true
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("New Care Entry") },
        text = {
            Column {
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

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = type,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Care Type") },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        careTypes.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    type = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = bloodPressure,
                    onValueChange = {
                        bloodPressure = if (it.length == 3 && !it.contains("/")) {
                            it.substring(0, 3) + "/"
                        } else it
                    },
                    label = { Text("Blood Pressure (e.g., 120/80)") },
                    modifier = Modifier.fillMaxWidth()
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



                if (showError) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("\u26A0 Please complete all fields", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    )
}
