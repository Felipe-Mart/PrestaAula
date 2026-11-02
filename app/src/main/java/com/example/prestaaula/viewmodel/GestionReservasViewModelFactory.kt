
package com.example.prestaaula.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.prestaaula.repository.ReservaRepository

class GestionReservasViewModelFactory(
    private val repository: ReservaRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                GestionReservasViewModel::class.java
            )
        ) {

            @Suppress("UNCHECKED_CAST")
            return GestionReservasViewModel(
                repository
            ) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido"
        )
    }
}
