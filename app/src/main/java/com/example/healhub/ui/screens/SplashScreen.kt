package com.example.healhub.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healhub.AppViewModel
import com.example.healhub.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(viewModel: AppViewModel) {
    LaunchedEffect(Unit) {
        delay(2000L) // Espera 2 segundos
        viewModel.navigateTo("login")  // Navega automáticamente
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), // 替换成你的 logo
            contentDescription = "Logo",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Welcome to HealHub", fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
    }
}
