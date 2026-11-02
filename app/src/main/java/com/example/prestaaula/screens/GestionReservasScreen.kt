package com.example.prestaaula.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prestaaula.data.listaRecursos
import com.example.prestaaula.data.local.PrestaAulaDatabase
import com.example.prestaaula.data.local.ReservaEntity
import com.example.prestaaula.repository.ReservaRepository
import com.example.prestaaula.viewmodel.GestionReservasViewModel
import com.example.prestaaula.viewmodel.GestionReservasViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionReservasScreen(
    onVolver: () -> Unit
) {
    val context = LocalContext.current

    // Conexión con la base de datos y el repositorio.
    val database = remember(context) {
        PrestaAulaDatabase.obtenerBaseDeDatos(context)
    }

    val repository = remember(database) {
        ReservaRepository(database)
    }

    // Creamos el ViewModel.
    val viewModel: GestionReservasViewModel = viewModel(
        factory = GestionReservasViewModelFactory(repository)
    )

    // Observamos los datos y mensajes del ViewModel.
    val reservas by viewModel.reservas.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    // Estados locales del formulario de devolución.
    var reservaParaDevolver by remember {
        mutableStateOf<ReservaEntity?>(null)
    }

    var estadoFisico by remember {
        mutableStateOf("Buen estado")
    }

    var observacionesDevolucion by remember {
        mutableStateOf("")
    }

    var mostrarErrorValidacion by remember {
        mutableStateOf(false)
    }

    fun limpiarFormulario() {
        reservaParaDevolver = null
        estadoFisico = "Buen estado"
        observacionesDevolucion = ""
        mostrarErrorValidacion = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Gestión de reservas CRA")
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
        ) {

            if (mensaje.isNotEmpty()) {
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )
            }

            if (reservas.isEmpty()) {
                Text(
                    text = "Todavía no hay reservas registradas.",
                    modifier = Modifier.padding(24.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = reservas,
                        key = { it.id }
                    ) { reserva ->

                        val nombreRecurso = listaRecursos
                            .find { it.id == reserva.recursoId }
                            ?.nombre ?: "Recurso desconocido"

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = nombreRecurso,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text("Reserva #${reserva.id}")
                                Text("Docente: ${reserva.nombreDocente}")
                                Text("Curso: ${reserva.curso}")
                                Text("Fecha: ${reserva.fecha}")
                                Text("Bloque: ${reserva.bloqueHorario}")

                                Text(
                                    text = "Estado: ${reserva.estado}",
                                    color = when (reserva.estado) {
                                        "Pendiente" ->
                                            MaterialTheme.colorScheme.secondary

                                        "Entregada" ->
                                            MaterialTheme.colorScheme.primary

                                        "Devuelta" ->
                                            MaterialTheme.colorScheme.tertiary

                                        else ->
                                            MaterialTheme.colorScheme.onSurface
                                    }
                                )

                                // Información de la devolución.
                                if (reserva.estado == "Devuelta") {
                                    HorizontalDivider()

                                    Text(
                                        text = "Datos de devolución",
                                        style = MaterialTheme.typography.titleSmall
                                    )

                                    Text(
                                        text = "Estado físico: ${
                                            reserva.estadoFisicoDevolucion
                                                ?: "No registrado"
                                        }"
                                    )

                                    if (!reserva.observacionesDevolucion.isNullOrBlank()) {
                                        Text(
                                            text = "Observaciones: ${reserva.observacionesDevolucion}"
                                        )
                                    }
                                }

                                // Registrar entrega mediante el ViewModel.
                                if (reserva.estado == "Pendiente") {
                                    Button(
                                        onClick = {
                                            viewModel.limpiarMensaje()
                                            viewModel.registrarEntrega(reserva.id)
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Registrar entrega")
                                    }
                                }

                                // Abrir formulario de devolución.
                                if (reserva.estado == "Entregada") {
                                    Button(
                                        onClick = {
                                            viewModel.limpiarMensaje()
                                            estadoFisico = "Buen estado"
                                            observacionesDevolucion = ""
                                            mostrarErrorValidacion = false
                                            reservaParaDevolver = reserva
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Registrar devolución")
                                    }
                                }

                                if (reserva.estado == "Devuelta") {
                                    Text(
                                        text = "Préstamo finalizado correctamente.",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Formulario emergente para registrar una devolución.
    if (reservaParaDevolver != null) {
        val reservaSeleccionada = reservaParaDevolver!!

        val nombreRecurso = listaRecursos
            .find { it.id == reservaSeleccionada.recursoId }
            ?.nombre ?: "Recurso desconocido"

        val observacionesObligatorias = estadoFisico != "Buen estado"

        val observacionesValidas =
            !observacionesObligatorias ||
                    observacionesDevolucion.isNotBlank()

        Dialog(
            onDismissRequest = {
                limpiarFormulario()
            }
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Registrar devolución",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = nombreRecurso,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text("Reserva #${reservaSeleccionada.id}")

                    HorizontalDivider()

                    Text(
                        text = "Estado físico del recurso",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Row {
                        RadioButton(
                            selected = estadoFisico == "Buen estado",
                            onClick = {
                                estadoFisico = "Buen estado"
                                mostrarErrorValidacion = false
                            }
                        )
                        Text(
                            text = "Buen estado",
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }

                    Row {
                        RadioButton(
                            selected = estadoFisico == "Con observaciones",
                            onClick = {
                                estadoFisico = "Con observaciones"
                            }
                        )
                        Text(
                            text = "Con observaciones",
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }

                    Row {
                        RadioButton(
                            selected = estadoFisico == "Dañado",
                            onClick = {
                                estadoFisico = "Dañado"
                            }
                        )
                        Text(
                            text = "Dañado",
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }

                    OutlinedTextField(
                        value = observacionesDevolucion,
                        onValueChange = {
                            observacionesDevolucion = it
                        },
                        label = {
                            Text("Observaciones")
                        },
                        placeholder = {
                            Text("Describe las condiciones del equipo")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 4,
                        isError = mostrarErrorValidacion &&
                                !observacionesValidas
                    )

                    if (observacionesObligatorias) {
                        Text(
                            text = "Debes describir el problema o daño.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    if (mostrarErrorValidacion && !observacionesValidas) {
                        Text(
                            text = "Las observaciones son obligatorias.",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                limpiarFormulario()
                            }
                        ) {
                            Text("Cancelar")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                mostrarErrorValidacion = true

                                if (observacionesValidas) {
                                    viewModel.registrarDevolucion(
                                        reservaId = reservaSeleccionada.id,
                                        estadoFisico = estadoFisico,
                                        observaciones = observacionesDevolucion
                                    )

                                    limpiarFormulario()
                                }
                            }
                        ) {
                            Text("Confirmar")
                        }
                    }
                }
            }
        }
    }
}