
package com.example.prestaaula.data

data class Reserva(
    val id: Int,
    val recursoId: Int,
    val nombreDocente: String,
    val curso: String,
    val fecha: String,
    val bloqueHorario: String,
    val observaciones: String,
    val estado: String = "Pendiente"
)
