
package com.example.prestaaula.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.prestaaula.data.EstadoRecurso
import com.example.prestaaula.data.listaRecursos
import com.example.prestaaula.data.local.EstadoMantenimientoEntity
import com.example.prestaaula.data.local.PrestaAulaDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleRecursoScreen(
    recursoId: Int,
    onVolver: () -> Unit,
    onReservar: () -> Unit
) {

    val recurso = listaRecursos.find {
        it.id == recursoId
    }

    val context = LocalContext.current

    val database = remember(context) {
        PrestaAulaDatabase.obtenerBaseDeDatos(context)
    }

    val reservas by remember(database) {
        database.reservaDao().obtenerReservas()
    }.collectAsState(initial = emptyList())

    val estadosMantenimiento by remember(database) {
        database.estadoMantenimientoDao().obtenerEstados()
    }.collectAsState(initial = emptyList())

    val scope = rememberCoroutineScope()

    var mostrarDialogo by remember {
        mutableStateOf(false)
    }

    var observaciones by remember {
        mutableStateOf("")
    }

    var guardando by remember {
        mutableStateOf(false)
    }

    var mensaje by remember {
        mutableStateOf("")
    }

    val estadoActual = recurso?.let {
        EstadoRecurso.obtenerEstado(
            recurso = it,
            reservas = reservas,
            estadosMantenimiento = estadosMantenimiento
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del recurso") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            if (recurso == null) {

                Text("No se encontró el recurso solicitado.")

                Button(onClick = onVolver) {
                    Text("Volver")
                }

            } else {

                Text(
                    text = recurso.nombre,
                    style = MaterialTheme.typography.headlineSmall
                )

                HorizontalDivider()

                Text("Categoría: ${recurso.categoria}")

                Text("Marca: ${recurso.marca}")

                Text("Código interno: ${recurso.codigoInterno}")

                Text("Descripción: ${recurso.descripcion}")

                Text(
                    text = "Estado actual: $estadoActual",
                    style = MaterialTheme.typography.titleMedium,
                    color = when (estadoActual) {

                        EstadoRecurso.DISPONIBLE ->
                            MaterialTheme.colorScheme.primary

                        EstadoRecurso.PRESTADO,
                        EstadoRecurso.REQUIERE_REVISION ->
                            MaterialTheme.colorScheme.error

                        else ->
                            MaterialTheme.colorScheme.secondary
                    }
                )

                if (mensaje.isNotEmpty()) {
                    Text(
                        text = mensaje,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (
                    estadoActual ==
                    EstadoRecurso.REQUIERE_REVISION
                ) {

                    Text(
                        text = "Este recurso necesita revisión antes " +
                                "de volver a prestarse.",
                        color = MaterialTheme.colorScheme.error
                    )

                    OutlinedButton(
                        onClick = {
                            observaciones = ""
                            mostrarDialogo = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Registrar revisión y habilitar")
                    }
                }

                if (
                    estadoActual ==
                    EstadoRecurso.MANTENCION
                ) {
                    Text(
                        text = "Este recurso se encuentra en mantenimiento."
                    )
                }

                if (
                    estadoActual ==
                    EstadoRecurso.PRESTADO
                ) {
                    Text(
                        text = "Este equipo tiene una entrega " +
                                "pendiente de devolución."
                    )
                }

                Button(
                    onClick = onReservar,
                    enabled = estadoActual != null &&
                            EstadoRecurso.permiteSolicitarReserva(
                                estadoActual
                            ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Solicitar reserva")
                }

                Text(
                    text = "La disponibilidad para cada fecha " +
                            "y bloque se comprueba al guardar.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    if (mostrarDialogo && recurso != null) {

        AlertDialog(
            onDismissRequest = {
                if (!guardando) {
                    mostrarDialogo = false
                }
            },
            title = {
                Text("Registrar revisión del equipo")
            },
            text = {

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = "Confirma que el recurso fue revisado " +
                                "y está en condiciones de volver " +
                                "a utilizarse."
                    )

                    OutlinedTextField(
                        value = observaciones,
                        onValueChange = {
                            observaciones = it
                        },
                        label = {
                            Text("Trabajo realizado")
                        },
                        placeholder = {
                            Text("Ej.: Se reparó la pantalla")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        enabled = !guardando
                    )

                    if (observaciones.isBlank()) {
                        Text(
                            text = "Describe la revisión realizada.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            },
            confirmButton = {

                Button(
                    onClick = {

                        guardando = true

                        scope.launch {

                            try {

                                database.estadoMantenimientoDao()
                                    .guardarEstado(
                                        EstadoMantenimientoEntity(
                                            recursoId = recursoId,
                                            habilitado = true,
                                            observaciones =
                                                observaciones.trim()
                                        )
                                    )

                                mensaje =
                                    "Recurso habilitado correctamente."

                                mostrarDialogo = false
                                observaciones = ""

                            } catch (e: Exception) {

                                mensaje =
                                    "No se pudo registrar la revisión."

                            } finally {

                                guardando = false
                            }
                        }
                    },
                    enabled = !guardando &&
                            observaciones.isNotBlank()
                ) {
                    Text(
                        if (guardando) {
                            "Guardando..."
                        } else {
                            "Habilitar"
                        }
                    )
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        mostrarDialogo = false
                    },
                    enabled = !guardando
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
