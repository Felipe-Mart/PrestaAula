
package com.example.prestaaula.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ReservaDao {

    @Query("SELECT * FROM reservas ORDER BY id DESC")
    abstract fun obtenerReservas(): Flow<List<ReservaEntity>>

    @Query(
        """
        SELECT COUNT(*) FROM reservas
        WHERE recursoId = :recursoId
        AND fecha = :fecha
        AND bloqueHorario = :bloqueHorario
        AND estado != 'Cancelada'
        """
    )
    abstract suspend fun contarConflictos(
        recursoId: Int,
        fecha: String,
        bloqueHorario: String
    ): Int

    @Insert
    abstract suspend fun insertarReserva(
        reserva: ReservaEntity
    ): Long

    @Query("SELECT * FROM reservas WHERE id = :id LIMIT 1")
    abstract suspend fun obtenerReservaPorId(
        id: Int
    ): ReservaEntity?

    @Query(
        """
        UPDATE reservas
        SET estado = 'Entregada'
        WHERE id = :reservaId
        AND estado = 'Pendiente'
        """
    )
    abstract suspend fun registrarEntrega(
        reservaId: Int
    ): Int

    @Query(
        """
        UPDATE reservas
        SET estado = 'Devuelta',
            estadoFisicoDevolucion = :estadoFisico,
            observacionesDevolucion = :observaciones
        WHERE id = :reservaId
        AND estado = 'Entregada'
        """
    )
    abstract suspend fun registrarDevolucion(
        reservaId: Int,
        estadoFisico: String,
        observaciones: String
    ): Int

    @Transaction
    open suspend fun guardarSiDisponible(
        reserva: ReservaEntity
    ): Boolean {

        val conflictos = contarConflictos(
            recursoId = reserva.recursoId,
            fecha = reserva.fecha,
            bloqueHorario = reserva.bloqueHorario
        )

        if (conflictos > 0) {
            return false
        }

        insertarReserva(reserva)

        return true
    }
}
