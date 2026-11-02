
package com.example.prestaaula.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prestaaula.data.ReservaValidator
import com.example.prestaaula.data.listaRecursos
import com.example.prestaaula.data.local.PrestaAulaDatabase
import com.example.prestaaula.repository.ReservaRepository
import com.example.prestaaula.viewmodel.ReservaViewModel
import com.example.prestaaula.viewmodel.ReservaViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaScreen(
    recursoId: Int,
    onVolver: () -> Unit
) {

    val context = LocalContext.current

    val repository = remember {
        val database =
            PrestaAulaDatabase.obtenerBaseDeDatos(context)

        ReservaRepository(database)
    }

    val reservaViewModel: ReservaViewModel = viewModel(
        factory = ReservaViewModelFactory(repository)
    )

    val recurso = listaRecursos.find { it.id == recursoId }

    var nombreDocente by remember { mutableStateOf("") }
    var curso by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var bloqueHorario by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }

    var mostrarErrores by remember { mutableStateOf(false) }
    var menuExpandido by remember { mutableStateOf(false) }
    var guardando by remember { mutableStateOf(false) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    var mensajeErrorReserva by remember { mutableStateOf("") }

    val bloques = listOf(
        "Bloque 1",
        "Bloque 2",
        "Bloque 3",
        "Bloque 4"
    )

    val errorNombre =
        ReservaValidator.validarNombre(nombreDocente)

    val errorCurso =
        ReservaValidator.validarCurso(curso)

    val errorFecha =
        ReservaValidator.validarFecha(fecha)

    val errorBloque =
        ReservaValidator.validarBloque(bloqueHorario)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reservar recurso") },
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

        if (recurso == null) {

            Text(
                text = "Recurso no encontrado",
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(24.dp)
            )

        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {

                Text(
                    text = "Recurso seleccionado",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = recurso.nombre,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Código: ${recurso.codigoInterno}"
                )

                Spacer(Modifier.height(24.dp))

                OutlinedTextField(
                    value = nombreDocente,
                    onValueChange = {
                        nombreDocente = it
                        mensajeErrorReserva = ""
                    },
                    label = { Text("Nombre del docente") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = mostrarErrores && errorNombre != null,
                    supportingText = {
                        if (mostrarErrores && errorNombre != null) {
                            Text(errorNombre)
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = curso,
                    onValueChange = {
                        curso = it
                        mensajeErrorReserva = ""
                    },
                    label = { Text("Curso") },
                    placeholder = { Text("Ejemplo: 4° Básico A") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = mostrarErrores && errorCurso != null,
                    supportingText = {
                        if (mostrarErrores && errorCurso != null) {
                            Text(errorCurso)
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = fecha,
                    onValueChange = {
                        fecha = it
                        mensajeErrorReserva = ""
                    },
                    label = { Text("Fecha de reserva") },
                    placeholder = { Text("DD/MM/AAAA") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = mostrarErrores && errorFecha != null,
                    supportingText = {
                        if (mostrarErrores && errorFecha != null) {
                            Text(errorFecha)
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = menuExpandido,
                    onExpandedChange = {
                        menuExpandido = !menuExpandido
                    }
                ) {

                    OutlinedTextField(
                        value = bloqueHorario,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Bloque horario") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = menuExpandido
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(
                                ExposedDropdownMenuAnchorType.PrimaryNotEditable
                            ),
                        isError = mostrarErrores && errorBloque != null,
                        supportingText = {
                            if (mostrarErrores && errorBloque != null) {
                                Text(errorBloque)
                            }
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = menuExpandido,
                        onDismissRequest = {
                            menuExpandido = false
                        }
                    ) {

                        bloques.forEach { bloque ->

                            DropdownMenuItem(
                                text = { Text(bloque) },
                                onClick = {
                                    bloqueHorario = bloque
                                    menuExpandido = false
                                    mensajeErrorReserva = ""
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = observaciones,
                    onValueChange = {
                        observaciones = it
                    },
                    label = { Text("Observaciones (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(Modifier.height(24.dp))

                if (mensajeErrorReserva.isNotEmpty()) {

                    Text(
                        text = mensajeErrorReserva,
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(Modifier.height(12.dp))
                }

                Button(
                    onClick = {

                        mostrarErrores = true
                        mensajeErrorReserva = ""

                        if (
                            !guardando &&
                            recurso.estado == "Disponible" &&
                            ReservaValidator.formularioValido(
                                nombreDocente,
                                curso,
                                fecha,
                                bloqueHorario
                            )
                        ) {

                            guardando = true

                            reservaViewModel.registrarReserva(
                                recursoId = recurso.id,
                                nombreDocente = nombreDocente,
                                curso = curso,
                                fecha = fecha,
                                bloqueHorario = bloqueHorario,
                                observaciones = observaciones
                            ) { guardada ->

                                guardando = false

                                if (guardada) {
                                    mostrarConfirmacion = true
                                } else {
                                    mensajeErrorReserva =
                                        "Este recurso ya tiene una reserva " +
                                                "para la fecha y bloque seleccionados."
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = recurso.estado == "Disponible" &&
                            !guardando
                ) {
                    Text(
                        if (guardando) {
                            "Guardando..."
                        } else {
                            "Solicitar reserva"
                        }
                    )
                }

                if (recurso.estado != "Disponible") {

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Este recurso no está disponible para reservar.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    if (mostrarConfirmacion) {

        AlertDialog(
            onDismissRequest = {
                mostrarConfirmacion = false
                onVolver()
            },
            title = {
                Text("Reserva registrada")
            },
            text = {
                Text(
                    "La reserva fue guardada correctamente " +
                            "en la base de datos de PrestaAula."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacion = false
                        onVolver()
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}
