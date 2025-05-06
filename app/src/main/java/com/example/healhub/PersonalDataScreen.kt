package com.example.healhub.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.healhub.database.AppDatabase
import com.example.healhub.database.PatientData
import kotlinx.coroutines.*
import java.util.*
import com.example.healhub.ui.theme.GreenButton
import com.example.healhub.ui.theme.GreenOutlinedButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen(
    roomId: Int,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val dao = AppDatabase.getInstance(context).patientDataDao()

    var fullName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }

    val languageOptions = listOf("English", "Spanish", "Catalan", "Chinese", "Arabic")
    var langExpanded by remember { mutableStateOf(false) }

    // Date Picker
    val calendar = Calendar.getInstance()
    val datePicker = remember {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                birthDate = "%02d/%02d/%04d".format(day, month + 1, year)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    // Load existing data
    LaunchedEffect(roomId) {
        withContext(Dispatchers.IO) {
            dao.getByRoomId(roomId)?.let {
                fullName = it.fullName
                birthDate = it.birthDate
                address = it.address
                language = it.language
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Personal Data") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CB8B3))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!isEditing) {
                // 🔒 Read-only view
                Text("Full Name: $fullName")
                Text("Date of Birth: $birthDate")
                Text("Address: $address")
                Text("Language: $language")

                Spacer(modifier = Modifier.height(12.dp))

                GreenButton(
                    text = "Edit",
                    onClick = { isEditing = true },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                // ✍️ Editable form
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = birthDate,
                    onValueChange = {},
                    label = { Text("Date of Birth") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { datePicker.show() }
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = language,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Language") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { langExpanded = true }
                )

                DropdownMenu(
                    expanded = langExpanded,
                    onDismissRequest = { langExpanded = false }
                ) {
                    languageOptions.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                language = it
                                langExpanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                GreenButton(
                    text = "Save",
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            dao.insert(
                                PatientData(
                                    roomId = roomId,
                                    fullName = fullName,
                                    birthDate = birthDate,
                                    address = address,
                                    language = language
                                )
                            )
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

            Spacer(modifier = Modifier.height(8.dp))
            GreenOutlinedButton(
                text = "Back",
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

