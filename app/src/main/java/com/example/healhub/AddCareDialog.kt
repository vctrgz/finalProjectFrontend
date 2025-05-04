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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.healhub.ui.theme.GreenButton
import com.example.healhub.ui.theme.GreenOutlinedButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCareDialog(
    onDismiss: () -> Unit,
    onSave: (
        String,  // date
        String,  // type
        String,  // bloodPressure
        String?, // respiratoryRate
        String?, // pulse
        String?, // temperature
        String?, // oxygenSaturation
        String?,
        String?,
        String?,
        String?,
        String?,
        String?,
        String?,  // drainage
        String?,  // shiftNote
        String   // notes
    ) -> Unit

) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var date by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var bloodPressure by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var respiratoryRate by remember { mutableStateOf("") }
    var pulse by remember { mutableStateOf("") }
    var temperature by remember { mutableStateOf("") }
    var oxygenSaturation by remember { mutableStateOf("") }
    val dietTextures = listOf(
        "Absolute",            // Absoluta
        "Hydric",              // Hídrica
        "Liquid",              // Líquida
        "Purée (Túrmix)",      // Túrmix
        "Semi-soft",           // Semitova
        "Soft",                // Tova
        "Easy to chew"         // Fàcil masticació
    )
    val dietTypes = listOf(
        "Standard (Basal)",    // Basal
        "Diabetic",            // Diabètica
        "Low-fat",             // Hipolipídica
        "Low-calorie",         // Hipocalòrica
        "High-calorie",        // Hipercalòrica
        "Celiac",              // Celíaca
        "Lactose-free"         // Sense lactosa
        // can add more options like Vegan、High-protein etc.
    )
    val hygieneOptions = listOf(
        "Bed wash",             // Allitat
        "Partial bed wash",     // Parcial al llit
        "Assisted shower",      // Dutxa amb ajuda
        "Independent"           // Autònom
    )
    var selectedHygiene by remember { mutableStateOf("") }
    var hygieneExpanded by remember { mutableStateOf(false) }

    var selectedTexture by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("") }
    var textureExpanded by remember { mutableStateOf(false) }
    var typeExpanded by remember { mutableStateOf(false) }

    val sedestationOptions = listOf("Good tolerance", "Poor tolerance")
    val walkingOptions = listOf("Independent", "With cane", "With walker", "With physical help")
    val postureOptions = listOf("Supine", "Left side", "Right side")

    var selectedSedestation by remember { mutableStateOf("") }
    var selectedWalking by remember { mutableStateOf("") }
    var selectedPosture by remember { mutableStateOf("") }

    var sedestationExpanded by remember { mutableStateOf(false) }
    var walkingExpanded by remember { mutableStateOf(false) }
    var postureExpanded by remember { mutableStateOf(false) }

    var drainage by remember { mutableStateOf("") }
    var shiftNote by remember { mutableStateOf("") }

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
                        type.isNotBlank() &&
                        bloodPressure.isNotBlank() &&
                        notes.isNotBlank()
                    ) {
                        showError = false
                        onSave(
                            date,
                            type,
                            bloodPressure,
                            respiratoryRate.ifBlank { null },
                            pulse.ifBlank { null },
                            temperature.ifBlank { null },
                            oxygenSaturation.ifBlank { null },
                            selectedTexture,
                            selectedType,
                            selectedHygiene,
                            selectedSedestation,
                            selectedWalking,
                            selectedPosture,
                            drainage,
                            shiftNote,
                            notes
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
                Text("Diet Texture", style = MaterialTheme.typography.labelMedium)
                ExposedDropdownMenuBox(expanded = textureExpanded, onExpandedChange = { textureExpanded = !textureExpanded }) {
                    OutlinedTextField(
                        value = selectedTexture,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Texture") },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = textureExpanded, onDismissRequest = { textureExpanded = false }) {
                        dietTextures.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    selectedTexture = it
                                    textureExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Diet Type", style = MaterialTheme.typography.labelMedium)
                ExposedDropdownMenuBox(expanded = typeExpanded, onExpandedChange = { typeExpanded = !typeExpanded }) {
                    OutlinedTextField(
                        value = selectedType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Type") },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }) {
                        dietTypes.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    selectedType = it
                                    typeExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Hygiene", style = MaterialTheme.typography.labelMedium)

                ExposedDropdownMenuBox(
                    expanded = hygieneExpanded,
                    onExpandedChange = { hygieneExpanded = !hygieneExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedHygiene,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Hygiene") },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = hygieneExpanded,
                        onDismissRequest = { hygieneExpanded = false }
                    ) {
                        hygieneOptions.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    selectedHygiene = it
                                    hygieneExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Sedestation", style = MaterialTheme.typography.labelMedium)
                ExposedDropdownMenuBox(
                    expanded = sedestationExpanded,
                    onExpandedChange = { sedestationExpanded = !sedestationExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedSedestation,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Sedestation") },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = sedestationExpanded,
                        onDismissRequest = { sedestationExpanded = false }
                    ) {
                        sedestationOptions.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    selectedSedestation = it
                                    sedestationExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Walking", style = MaterialTheme.typography.labelMedium)
                ExposedDropdownMenuBox(
                    expanded = walkingExpanded,
                    onExpandedChange = { walkingExpanded = !walkingExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedWalking,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Walking") },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = walkingExpanded,
                        onDismissRequest = { walkingExpanded = false }
                    ) {
                        walkingOptions.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    selectedWalking = it
                                    walkingExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Postural Change", style = MaterialTheme.typography.labelMedium)
                ExposedDropdownMenuBox(
                    expanded = postureExpanded,
                    onExpandedChange = { postureExpanded = !postureExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedPosture,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Position") },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = postureExpanded,
                        onDismissRequest = { postureExpanded = false }
                    ) {
                        postureOptions.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    selectedPosture = it
                                    postureExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = drainage,
                    onValueChange = { drainage = it },
                    label = { Text("Drainage Info (e.g., Type and output)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = shiftNote,
                    onValueChange = { shiftNote = it },
                    label = { Text("Shift Change Observations") },
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
