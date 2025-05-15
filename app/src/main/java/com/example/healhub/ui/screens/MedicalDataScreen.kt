package com.example.healhub.ui


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MedicalDataScreen(
//    roomId: Int,
//    onBack: () -> Unit
//) {
//    val context = LocalContext.current
//    val dao = AppDatabase.getInstance(context).medicalDataDao()
//
//    var hasUrinaryCatheter by remember { mutableStateOf(false) }
//    var hasVenousCatheter by remember { mutableStateOf(false) }
//    var needsOxygen by remember { mutableStateOf(false) }
//
//    val dependencyOptions = listOf("Independent", "Partial", "Total")
//    var selectedDependency by remember { mutableStateOf(dependencyOptions[0]) }
//    var dependencyExpanded by remember { mutableStateOf(false) }
//
//    var isEditing by remember { mutableStateOf(false) }
//
//    LaunchedEffect(roomId) {
//        withContext(Dispatchers.IO) {
//            dao.getByRoomId(roomId)?.let {
//                hasUrinaryCatheter = it.hasUrinaryCatheter
//                hasVenousCatheter = it.hasVenousCatheter
//                needsOxygen = it.needsOxygen
//                selectedDependency = it.dependencyLevel
//            }
//        }
//    }
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        TopAppBar(
//            title = { Text("Medical Data") },
//            navigationIcon = {
//                IconButton(onClick = onBack) {
//                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                }
//            },
//            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CB8B3))
//        )
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            if (!isEditing) {
//                // 🔒 Read-only mode
//                Text("Urinary Catheter: ${if (hasUrinaryCatheter) "Yes" else "No"}")
//                Text("Venous Catheter: ${if (hasVenousCatheter) "Yes" else "No"}")
//                Text("Oxygen Therapy: ${if (needsOxygen) "Yes" else "No"}")
//                Text("Dependency Level: $selectedDependency")
//
//                GreenButton(
//                    text = "Edit",
//                    onClick = { isEditing = true },
//                    modifier = Modifier.fillMaxWidth()
//                )
//            } else {
//                // ✍️ Editable mode
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Text("Urinary Catheter")
//                    Spacer(modifier = Modifier.weight(1f))
//                    Switch(checked = hasUrinaryCatheter, onCheckedChange = { hasUrinaryCatheter = it })
//                }
//
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Text("Venous Catheter")
//                    Spacer(modifier = Modifier.weight(1f))
//                    Switch(checked = hasVenousCatheter, onCheckedChange = { hasVenousCatheter = it })
//                }
//
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Text("Oxygen Therapy")
//                    Spacer(modifier = Modifier.weight(1f))
//                    Switch(checked = needsOxygen, onCheckedChange = { needsOxygen = it })
//                }
//
//                Text("Dependency Level")
//                ExposedDropdownMenuBox(
//                    expanded = dependencyExpanded,
//                    onExpandedChange = { dependencyExpanded = !dependencyExpanded }
//                ) {
//                    OutlinedTextField(
//                        value = selectedDependency,
//                        onValueChange = {},
//                        readOnly = true,
//                        label = { Text("Select level") },
//                        modifier = Modifier
//                            .menuAnchor()
//                            .fillMaxWidth()
//                    )
//                    ExposedDropdownMenu(
//                        expanded = dependencyExpanded,
//                        onDismissRequest = { dependencyExpanded = false }
//                    ) {
//                        dependencyOptions.forEach {
//                            DropdownMenuItem(
//                                text = { Text(it) },
//                                onClick = {
//                                    selectedDependency = it
//                                    dependencyExpanded = false
//                                }
//                            )
//                        }
//                    }
//                }
//
//                GreenButton(
//                    text = "Save",
//                    onClick = {
//                        CoroutineScope(Dispatchers.IO).launch {
//                            dao.insert(
//                                MedicalData(
//                                    roomId = roomId,
//                                    hasUrinaryCatheter = hasUrinaryCatheter,
//                                    hasVenousCatheter = hasVenousCatheter,
//                                    needsOxygen = needsOxygen,
//                                    dependencyLevel = selectedDependency
//                                )
//                            )
//                            withContext(Dispatchers.Main) {
//                                isEditing = false
//                            }
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                GreenOutlinedButton(
//                    text = "Cancel",
//                    onClick = { isEditing = false },
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//
//            GreenOutlinedButton(
//                text = "Back",
//                onClick = onBack,
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//    }
//}
//
