
package com.example.prestaaula.data

import com.example.prestaaula.data.local.EstadoMantenimientoEntity
import com.example.prestaaula.data.local.ReservaEntity

object EstadoRecurso {

    const val DISPONIBLE = "Disponible"
    const val PRESTADO = "Prestado"
    const val MANTENCION = "Mantención"
    const val REQUIERE_REVISION = "Requiere revisión"

    fun obtenerEstado(
        recurso: Recurso,
        reservas: List<ReservaEntity>,
        estadosMantenimiento: List<EstadoMantenimientoEntity>
    ): String {

        // Mantenimiento definido inicialmente en el catálogo.
        if (recurso.estado == MANTENCION) {
            return MANTENCION
        }

        // Revisamos si el equipo tiene una entrega activa.
        val tienePrestamoActivo = reservas.any { reserva ->
            reserva.recursoId == recurso.id &&
                    reserva.estado == "Entregada"
        }

        // Buscamos la devolución con el ID más alto.
        val ultimaDevolucion = reservas
            .filter { reserva ->
                reserva.recursoId == recurso.id &&
                        reserva.estado == "Devuelta"
            }
            .maxByOrNull { reserva ->
                reserva.id
            }

        val ultimaDevolucionDanada =
            ultimaDevolucion?.estadoFisicoDevolucion == "Dañado"

        // Consultamos si el CRA registró una rehabilitación.
        val mantenimiento = estadosMantenimiento.find {
            it.recursoId == recurso.id
        }

        // La rehabilitación habilita el equipo respecto de
        // la última devolución dañada.
        val rehabilitado = mantenimiento?.habilitado == true

        if (ultimaDevolucionDanada && !rehabilitado) {
            return REQUIERE_REVISION
        }

        if (tienePrestamoActivo) {
            return PRESTADO
        }

        return DISPONIBLE
    }

    fun permiteSolicitarReserva(
        estado: String
    ): Boolean {
        return estado != MANTENCION &&
                estado != REQUIERE_REVISION
    }
}
