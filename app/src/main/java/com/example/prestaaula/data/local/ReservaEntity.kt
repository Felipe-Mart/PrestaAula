
package com.example.prestaaula.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reservas",
    indices = [
        Index(
            value = ["recursoId", "fecha", "bloqueHorario"],
            unique = true
        )
    ]
)
data class ReservaEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val recursoId: Int,

    val nombreDocente: String,

    val curso: String,

    val fecha: String,

    val bloqueHorario: String,

    val observaciones: String,

    val estado: String = "Pendiente",

    // Nuevos campos
    val estadoFisicoDevolucion: String? = null,

    val observacionesDevolucion: String? = null
)
