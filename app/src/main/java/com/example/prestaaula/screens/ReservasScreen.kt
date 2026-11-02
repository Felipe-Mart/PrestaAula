
package com.example.prestaaula.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.prestaaula.data.listaRecursos
import com.example.prestaaula.data.local.PrestaAulaDatabase
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasScreen(
    onVolver: () -> Unit
) {

    val context = LocalContext.current

    val database = remember(context) {
        PrestaAulaDatabase.obtenerBaseDeDatos(context)
    }

    val reservas by database.reservaDao()
        .obtenerReservas()
        .collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reservas registradas") },
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

        if (reservas.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp)
            ) {
                Text(
                    text = "Todavía no hay reservas registradas.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
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
                            verticalArrangement =
                                Arrangement.spacedBy(6.dp)
                        ) {

                            Text(
                                text = nombreRecurso,
                                style =
                                    MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = "Docente: ${reserva.nombreDocente}"
                            )

                            Text(
                                text = "Curso: ${reserva.curso}"
                            )

                            Text(
                                text = "Fecha: ${reserva.fecha}"
                            )

                            Text(
                                text = "Bloque: ${reserva.bloqueHorario}"
                            )

                            Text(
                                text = "Estado: ${reserva.estado}"
                            )

                            if (reserva.observaciones.isNotBlank()) {
                                Text(
                                    text =
                                        "Observaciones: ${reserva.observaciones}"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
