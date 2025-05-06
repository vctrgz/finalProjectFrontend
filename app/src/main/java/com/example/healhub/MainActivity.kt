package com.example.healhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.healhub.database.AppDatabase
import com.example.healhub.database.RoomEntity
import com.example.healhub.ui.MedicalDataScreen
import com.example.healhub.ui.PatientInfoScreen
import com.example.healhub.ui.PatientMenuScreen
import com.example.healhub.ui.PersonalDataScreen
import com.example.healhub.ui.theme.HealHubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealHubTheme {
                var screen by remember { mutableStateOf("splash") }
                var selectedRoom by remember { mutableStateOf<RoomEntity?>(null) }

                when (screen) {
                    "splash" -> SplashScreen { screen = "login" }
                    "login" -> LoginScreen { screen = "rooms" }
                    "rooms" -> PrincipalScreen(
                        rooms = listOf(
                            RoomEntity(1, "101", "John Doe", "Stable"),
                            RoomEntity(2, "102", "Jane Smith", "Observation"),
                            RoomEntity(3, "103", "Carlos Ruiz", "Pending Discharge")
                        ),
                        onLogout = { screen = "login" },
                        onRoomClick = {
                            selectedRoom = it
                            screen = "patient_menu"
                        }

                    )
                    "patient_menu" -> selectedRoom?.let { room ->
                        PatientMenuScreen(
                            roomId = room.id,
                            onBack = { screen = "rooms" },
                            onNavigateToPersonal = { screen = "personal_data" },
                            onNavigateToMedical = { screen = "medical_data" },
                            onNavigateToCare = { screen = "patient" }
                        )
                    }
                    "medical_data" -> selectedRoom?.let { room ->
                        MedicalDataScreen(
                            roomId = room.id,
                            onBack = { screen = "patient_menu" }
                        )
                    }
                    "personal_data" -> selectedRoom?.let { room ->
                        PersonalDataScreen(
                            roomId = room.id,
                            onBack = { screen = "patient_menu" }
                        )
                    }


                    "patient" -> selectedRoom?.let { room ->
                        PatientInfoScreen(
                            roomId = room.id,
                            onBack = { screen = "rooms" }
                        )
                    }
                }
            }
        }
    }
}
