package com.example.healhub.ui.remote

import retrofit2.http.Body
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

interface RemoteInterface {
    @POST("/auxiliar/login")
    suspend fun postRemoteLogin(@Body auxiliar: Auxiliar): Auxiliar
}
