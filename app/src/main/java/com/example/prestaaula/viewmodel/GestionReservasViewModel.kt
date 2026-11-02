
package com.example.prestaaula.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prestaaula.data.local.ReservaEntity
import com.example.prestaaula.repository.ReservaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GestionReservasViewModel(
    private val repository: ReservaRepository
) : ViewModel() {

    // Lista de reservas observada desde Room
    val reservas = repository.obtenerReservas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Mensaje para informar el resultado de las operaciones
    var mensaje = kotlinx.coroutines.flow.MutableStateFlow("")
        private set

    // Registrar entrega de un recurso
    fun registrarEntrega(reservaId: Int) {

        viewModelScope.launch {

            val resultado =
                repository.registrarEntrega(reservaId)

            mensaje.value = if (resultado) {
                "Entrega registrada correctamente."
            } else {
                "No se pudo registrar la entrega."
            }
        }
    }

    // Registrar devolución de un recurso
    fun registrarDevolucion(
        reservaId: Int,
        estadoFisico: String,
        observaciones: String
    ) {

        viewModelScope.launch {

            val resultado = repository.registrarDevolucion(
                reservaId = reservaId,
                estadoFisico = estadoFisico,
                observaciones = observaciones
            )

            mensaje.value = if (resultado) {
                "Devolución registrada correctamente."
            } else {
                "No se pudo registrar la devolución."
            }
        }
    }

    fun limpiarMensaje() {
        mensaje.value = ""
    }
}
