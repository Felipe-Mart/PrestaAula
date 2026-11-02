
package com.example.prestaaula.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EstadoMantenimientoDao {

    @Query("SELECT * FROM estado_mantenimiento")
    fun obtenerEstados(): Flow<List<EstadoMantenimientoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarEstado(
        estado: EstadoMantenimientoEntity
    )

    // Si el equipo vuelve a dañarse,
    // una rehabilitación anterior deja de ser válida.
    @Query(
        """
        UPDATE estado_mantenimiento
        SET habilitado = 0
        WHERE recursoId = :recursoId
        """
    )
    suspend fun invalidarRehabilitacion(
        recursoId: Int
    ): Int
}
