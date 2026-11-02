
package com.example.prestaaula.repository

import androidx.room.withTransaction
import com.example.prestaaula.data.EstadoRecurso
import com.example.prestaaula.data.listaRecursos
import com.example.prestaaula.data.local.EstadoMantenimientoEntity
import com.example.prestaaula.data.local.PrestaAulaDatabase
import com.example.prestaaula.data.local.ReservaEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ReservaRepository(
    private val database: PrestaAulaDatabase
) {

    private val reservaDao = database.reservaDao()

    private val mantenimientoDao =
        database.estadoMantenimientoDao()

    // Obtener todas las reservas
    fun obtenerReservas(): Flow<List<ReservaEntity>> {
        return reservaDao.obtenerReservas()
    }

    // Guardar reserva solamente si el recurso
    // está habilitado y no existe conflicto.
    suspend fun guardarReserva(
        reserva: ReservaEntity
    ): Boolean {

        return database.withTransaction {

            // 1. Comprobar que el recurso exista.
            val recurso = listaRecursos.find {
                it.id == reserva.recursoId
            } ?: return@withTransaction false

            // 2. Consultar las reservas guardadas.
            val reservasActuales =
                reservaDao.obtenerReservas().first()

            // 3. Consultar el mantenimiento.
            val estadosMantenimiento =
                mantenimientoDao.obtenerEstados().first()

            // 4. Calcular el estado real del recurso.
            val estadoActual = EstadoRecurso.obtenerEstado(
                recurso = recurso,
                reservas = reservasActuales,
                estadosMantenimiento = estadosMantenimiento
            )

            // 5. Rechazar recursos no habilitados.
            if (
                !EstadoRecurso.permiteSolicitarReserva(
                    estadoActual
                )
            ) {
                return@withTransaction false
            }

            // 6. Mantener la validación de conflictos
            // por recurso, fecha y bloque.
            reservaDao.guardarSiDisponible(reserva)
        }
    }

    // Registrar entrega
    suspend fun registrarEntrega(
        reservaId: Int
    ): Boolean {
        return reservaDao.registrarEntrega(reservaId) > 0
    }

    // Registrar devolución y actualizar mantenimiento
    suspend fun registrarDevolucion(
        reservaId: Int,
        estadoFisico: String,
        observaciones: String
    ): Boolean {

        val estadosPermitidos = listOf(
            "Buen estado",
            "Con observaciones",
            "Dañado"
        )

        if (estadoFisico !in estadosPermitidos) {
            return false
        }

        if (
            estadoFisico != "Buen estado" &&
            observaciones.isBlank()
        ) {
            return false
        }

        return database.withTransaction {

            val reserva = reservaDao.obtenerReservaPorId(
                reservaId
            ) ?: return@withTransaction false

            if (reserva.estado != "Entregada") {
                return@withTransaction false
            }

            val actualizado = reservaDao.registrarDevolucion(
                reservaId = reservaId,
                estadoFisico = estadoFisico,
                observaciones = observaciones.trim()
            )

            if (actualizado == 0) {
                return@withTransaction false
            }

            // Una nueva devolución dañada invalida
            // cualquier rehabilitación anterior.
            if (estadoFisico == "Dañado") {

                mantenimientoDao.guardarEstado(
                    EstadoMantenimientoEntity(
                        recursoId = reserva.recursoId,
                        habilitado = false,
                        observaciones =
                            "Requiere revisión por nueva devolución dañada."
                    )
                )
            }

            true
        }
    }
}
