
package com.example.prestaaula.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estado_mantenimiento")
data class EstadoMantenimientoEntity(

    @PrimaryKey
    val recursoId: Int,

    val habilitado: Boolean,

    val observaciones: String
)
