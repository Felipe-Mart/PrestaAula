package com.example.prestaaula.data

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle

object ReservaValidator {

    private val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/uuuu")
        .withResolverStyle(ResolverStyle.STRICT)

    fun validarNombre(nombre: String): String? {

        return when {
            nombre.isBlank() ->
                "Ingresa el nombre del docente"

            nombre.trim().length < 3 ->
                "El nombre debe tener al menos 3 caracteres"

            else -> null
        }
    }

    fun validarCurso(curso: String): String? {

        return when {
            curso.isBlank() ->
                "Ingresa el curso"

            else -> null
        }
    }

    fun validarFecha(fecha: String): String? {

        if (fecha.isBlank()) {
            return "Ingresa una fecha"
        }

        return try {

            val fechaReserva = LocalDate.parse(
                fecha.trim(),
                formatoFecha
            )

            if (fechaReserva.isBefore(LocalDate.now())) {
                "No puedes reservar para una fecha pasada"
            } else {
                null
            }

        } catch (e: DateTimeParseException) {
            "Ingresa una fecha válida (DD/MM/AAAA)"
        }
    }

    fun validarBloque(bloque: String): String? {

        return when {
            bloque.isBlank() ->
                "Selecciona un bloque horario"

            bloque !in listOf(
                "Bloque 1",
                "Bloque 2",
                "Bloque 3",
                "Bloque 4"
            ) ->
                "Selecciona un bloque válido"

            else -> null
        }
    }

    fun formularioValido(
        nombre: String,
        curso: String,
        fecha: String,
        bloque: String
    ): Boolean {

        return validarNombre(nombre) == null &&
                validarCurso(curso) == null &&
                validarFecha(fecha) == null &&
                validarBloque(bloque) == null
    }
}
