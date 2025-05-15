package com.example.healhub

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.healhub.ui.MedicalDataScreen
//import com.example.healhub.ui.PatientInfoScreen
//import com.example.healhub.ui.PersonalDataScreen
import com.example.healhub.ui.PatientMenuScreen
import com.example.healhub.ui.PersonalDataScreen
import com.example.healhub.ui.dataClasses.Room
import com.example.healhub.ui.remote.Auxiliar
import com.example.healhub.ui.remote.RemoteFindRoomById
import com.example.healhub.ui.remote.RemoteInterface
import com.example.healhub.ui.remote.RemoteLoginUiState
import com.example.healhub.ui.remote.RemoteRoomListState
import com.example.healhub.ui.screens.LoginScreen
import com.example.healhub.ui.screens.PrincipalScreen
import com.example.healhub.ui.screens.SplashScreen
import com.example.healhub.ui.theme.HealHubTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealHubTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val viewModel: AppViewModel = viewModel()
    val screen = viewModel.currentScreen
    var selectedRoom by remember { mutableStateOf<Room?>(null) }

    when (screen) {
        "splash" -> SplashScreen(viewModel)
        "login" -> LoginScreen(viewModel)
        "rooms" -> PrincipalScreen(
            viewModel = viewModel,
            onLogout = { viewModel.logout() },
            onRoomClick = {
                selectedRoom = it
                viewModel.navigateTo("patient_menu")
            }
        )
        "patient_menu" -> selectedRoom?.let { room ->
            PatientMenuScreen(
                roomId = room.habitacionId,
                onBack = { viewModel.navigateTo("rooms") },
                onNavigateToPersonal = { viewModel.navigateTo("personal_data") },
                onNavigateToMedical = { viewModel.navigateTo("medical_data") },
                onNavigateToCare = { viewModel.navigateTo("patient") }
            )
        }
        "personal_data" -> selectedRoom?.let { room ->
            PersonalDataScreen(
                roomId = room.habitacionId,
                onBack = { viewModel.navigateTo("patient_menu") },
                viewModel = viewModel
            )
        }
//        "medical_data" -> selectedRoom?.let { room ->
//            MedicalDataScreen(
//                roomId = room.habitacionId,
//                onBack = { viewModel.navigateTo("patient_menu") }
//            )
//        }
//        "patient" -> selectedRoom?.let { room ->
//            PatientInfoScreen(
//                roomId = room.habitacionId,
//                onBack = { viewModel.navigateTo("rooms") }
//            )
//        }
    }
}

class AppViewModel : ViewModel() {

    // Estado del login remoto
    var remoteLoginUiState: RemoteLoginUiState by mutableStateOf(RemoteLoginUiState.Cargant)
        private set
    var remoteRoomListState: RemoteRoomListState by mutableStateOf(RemoteRoomListState.Cargant)
        private set
    var remoteFindRoomById: RemoteFindRoomById by mutableStateOf(RemoteFindRoomById.Cargant)
        private set
    // Retrofit configurado con tu backend
    private val connection = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/") // Asegúrate de que el puerto y la IP son correctos para el emulador
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RemoteInterface::class.java)

    // Enfermero logueado (si lo hay)
    private val _loggedInNurse = MutableStateFlow<Auxiliar?>(null)

    // Pantalla actual
    var currentScreen by mutableStateOf("splash")
        private set

    // Navegación
    fun navigateTo(screen: String) {
        currentScreen = screen
    }

    fun logout() {
        _loggedInNurse.value = null
        remoteLoginUiState = RemoteLoginUiState.Cargant
        navigateTo("login")
    }

    // Lógica de login remoto
    fun postRemoteLogin(nombre: String, numTrabajador: String) {
        Log.d("Login", "Intentando login con numTrabajador: $numTrabajador, nombre: $nombre")

        viewModelScope.launch {
            remoteLoginUiState = RemoteLoginUiState.Cargant // Estado de cargando
            try {
                val response = connection.postRemoteLogin(
                    Auxiliar(
                        numTrabajador = numTrabajador,
                        nombre = nombre
                    )
                )
                Log.d("Login", "Respuesta del backend: ${response.toString()}")

                // Verifica que la respuesta del backend coincida
                if (response.numTrabajador == numTrabajador && response.nombre == nombre) {
                    remoteLoginUiState = RemoteLoginUiState.Success(response)
                    _loggedInNurse.update { response }
                    navigateTo("rooms")
                } else {
                    remoteLoginUiState = RemoteLoginUiState.Error
                    Log.d("Login", "Credenciales incorrectas")
                }
            } catch (e: Exception) {
                Log.d("Login", "Error en la respuesta del backend: ${e.message}")
                remoteLoginUiState = RemoteLoginUiState.Error
            }
        }
    }
    fun getRemoteAllRooms() {
        viewModelScope.launch {
            remoteRoomListState = RemoteRoomListState.Cargant
            try {
                val rooms = connection.getRemoteAllRooms()
                Log.d("Rooms", "Fetched rooms: $rooms")
                remoteRoomListState = RemoteRoomListState.Success(rooms)
            } catch (e: Exception) {
                Log.e("Rooms", "Error fetching rooms: ${e.message}")
                remoteRoomListState = RemoteRoomListState.Error
            }
        }
    }
    fun getByRoomID(roomId: Int) {
        viewModelScope.launch {
            remoteFindRoomById = RemoteFindRoomById.Cargant
            try {
                val rooms = connection.getRemoteAllRooms()
                Log.d("Rooms", "Fetched rooms: $rooms")

                val room = rooms.find { it.habitacionId == roomId }

                if (room != null) {
                    remoteFindRoomById = RemoteFindRoomById.Success(room)
                } else {
                    Log.w("Rooms", "Room with ID $roomId not found")
                    remoteFindRoomById = RemoteFindRoomById.Error
                }
            } catch (e: Exception) {
                Log.e("Rooms", "Error fetching rooms: ${e.message}")
                remoteFindRoomById = RemoteFindRoomById.Error
            }
        }
    }

}
