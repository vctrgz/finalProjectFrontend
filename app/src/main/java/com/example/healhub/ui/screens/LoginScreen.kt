package com.example.healhub.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.healhub.AppViewModel
import com.example.healhub.ui.remote.RemoteLoginUiState

@Composable
fun LoginScreen(viewModel: AppViewModel) {
    val context = LocalContext.current
    val remoteLoginUiState = viewModel.remoteLoginUiState

    var numTrabajador by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf("") }

    // Observa el resultado del login
    LaunchedEffect(remoteLoginUiState) {
        when (remoteLoginUiState) {
            is RemoteLoginUiState.Success -> {
                loginError = ""
            }
            is RemoteLoginUiState.Error -> {
                loginError = "Credenciales inválidas. Por favor, inténtalo de nuevo."
            }
            RemoteLoginUiState.Cargant -> {
                loginError = ""
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("HealHub", fontSize = 32.sp, color = Color(0xFF4CB8B3))
            Spacer(modifier = Modifier.height(40.dp))
            OutlinedTextField(
                value = numTrabajador,
                onValueChange = { numTrabajador = it },
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("ID de trabajador") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(icon, contentDescription = "Toggle password visibility")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    viewModel.postRemoteLogin(numTrabajador, nombre)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CB8B3),
                    contentColor = Color.White
                )
            ) {
                Text("Login")
            }
            if (loginError.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(loginError, color = Color.Red, fontSize = 14.sp)
            }
        }
    }
}

