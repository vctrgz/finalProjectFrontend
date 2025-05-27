package com.example.healhub.ui.remote

import com.example.healhub.ui.dataClasses.Care
import com.example.healhub.ui.dataClasses.Patient
import com.example.healhub.ui.dataClasses.Room
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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
sealed interface RemoteCareListByPatientState {
    data class Success(val careList: List<Care>) : RemoteCareListByPatientState
    object Error : RemoteCareListByPatientState
    object Cargant : RemoteCareListByPatientState
}
sealed interface RemotePatientDataState {
    data class Success(val patientData: Patient) : RemotePatientDataState
    object Error : RemotePatientDataState
    object Cargant : RemotePatientDataState
}
sealed interface RemoteSaveCareDataState {
    data class Success(val care: Care) : RemoteSaveCareDataState
    object Error : RemoteSaveCareDataState
    object Cargant : RemoteSaveCareDataState
}
interface RemoteInterface {
    @POST("/auxiliar/login")
    suspend fun postRemoteLogin(@Body auxiliar: Auxiliar): Auxiliar
    @GET("/habitacion/habitaciones")
    suspend fun getRemoteAllRooms(): List<Room>
    @GET("/registro/patient/{id}")
    suspend fun getRemoteAllCaresByPatient(@Path("id") patientId: Int?): List<Care>
    @POST("/registro/nuevoRegistro")
    suspend fun postRemoteSaveCareData(@Body care: Care): Care
}
