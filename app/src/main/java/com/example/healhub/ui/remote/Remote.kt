package com.example.healhub.ui.remote

import com.example.healhub.ui.dataClasses.Patient
import com.example.healhub.ui.dataClasses.Room
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class Auxiliar(
    val numTrabajador: String,
    val nombre: String
)

sealed interface RemoteLoginUiState {
    data class Success(val auxiliar: Auxiliar) : RemoteLoginUiState
    object Error : RemoteLoginUiState
    object Cargant : RemoteLoginUiState
}
sealed interface RemoteRoomListState {
    data class Success(val roomList: List<Room>) : RemoteRoomListState
    object Error : RemoteRoomListState
    object Cargant : RemoteRoomListState
}
sealed interface RemoteFindRoomById {
    data class Success(val room: Room) : RemoteFindRoomById
    object Error : RemoteFindRoomById
    object Cargant : RemoteFindRoomById
}
sealed interface RemotePatientDataState {
    data class Success(val patientData: Patient) : RemotePatientDataState
    object Error : RemotePatientDataState
    object Cargant : RemotePatientDataState
}
interface RemoteInterface {
    @POST("/auxiliar/login")
    suspend fun postRemoteLogin(@Body auxiliar: Auxiliar): Auxiliar
    @GET("/habitacion/habitaciones")
    suspend fun getRemoteAllRooms(): List<Room>
}
