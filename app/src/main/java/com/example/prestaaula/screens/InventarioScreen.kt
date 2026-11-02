
package com.example.prestaaula.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prestaaula.data.EstadoRecurso
import com.example.prestaaula.data.Recurso
import com.example.prestaaula.data.listaRecursos
import com.example.prestaaula.data.local.PrestaAulaDatabase
import com.example.prestaaula.viewmodel.InventarioViewModel
import com.example.prestaaula.viewmodel.InventarioViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventarioScreen(
    onRecursoClick: (Recurso) -> Unit,
    onVolver: () -> Unit,
    onVerReservas: () -> Unit
) {
    val context = LocalContext.current

    // Conexión con la base de datos.
    val database = remember(context) {
        PrestaAulaDatabase.obtenerBaseDeDatos(context)
    }

    // ViewModel del inventario.
    val inventarioViewModel: InventarioViewModel = viewModel(
        factory = InventarioViewModelFactory(database)
    )

    // Observamos los datos almacenados en Room.
    val reservas by inventarioViewModel.reservas.collectAsState()

    val estadosMantenimiento by
    inventarioViewModel.estadosMantenimiento.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Inventario de recursos")
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Recursos del colegio",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Consulta el estado actual de los equipos."
                )
            }

            item {
                Button(
                    onClick = onVerReservas,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.EventNote,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Ver reservas registradas")
                }
            }

            items(
                items = listaRecursos,
                key = { it.id }
            ) { recurso ->

                // Calculamos el estado actual del recurso.
                val estadoActual = EstadoRecurso.obtenerEstado(
                    recurso = recurso,
                    reservas = reservas,
                    estadosMantenimiento = estadosMantenimiento
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onRecursoClick(recurso)
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = recurso.nombre,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "Categoría: ${recurso.categoria}"
                        )

                        Text(
                            text = "Código: ${recurso.codigoInterno}"
                        )

                        Text(
                            text = "Estado actual: $estadoActual",
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

                        if (estadoActual == EstadoRecurso.REQUIERE_REVISION) {
                            Text(
                                text = "Equipo dañado. Pendiente de revisión.",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        if (estadoActual == EstadoRecurso.PRESTADO) {
                            Text(
                                text = "Entrega pendiente de devolución.",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        TextButton(
                            onClick = {
                                onRecursoClick(recurso)
                            }
                        ) {
                            Text("Ver detalle")

                            Spacer(modifier = Modifier.width(4.dp))

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}
