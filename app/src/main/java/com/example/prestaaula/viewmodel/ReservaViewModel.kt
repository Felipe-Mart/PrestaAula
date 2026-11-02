
package com.example.prestaaula.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.prestaaula.data.local.ReservaEntity
import com.example.prestaaula.repository.ReservaRepository
import kotlinx.coroutines.launch

class ReservaViewModel(
    private val repository: ReservaRepository
) : ViewModel() {

    fun registrarReserva(
        recursoId: Int,
        nombreDocente: String,
        curso: String,
        fecha: String,
        bloqueHorario: String,
        observaciones: String,
        onResultado: (Boolean) -> Unit
    ) {

        viewModelScope.launch {

            val nuevaReserva = ReservaEntity(
                recursoId = recursoId,
                nombreDocente = nombreDocente.trim(),
                curso = curso.trim(),
                fecha = fecha.trim(),
                bloqueHorario = bloqueHorario,
                observaciones = observaciones.trim()
            )

            val guardada = repository.guardarReserva(
                nuevaReserva
            )

            onResultado(guardada)
        }
    }
}

class ReservaViewModelFactory(
    private val repository: ReservaRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        return ReservaViewModel(repository) as T
    }
}
