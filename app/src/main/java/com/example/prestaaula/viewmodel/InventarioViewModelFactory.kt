package com.example.prestaaula.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.prestaaula.data.local.PrestaAulaDatabase

class InventarioViewModelFactory(
    private val database: PrestaAulaDatabase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(InventarioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventarioViewModel(database) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}