
package com.example.prestaaula.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.prestaaula.data.local.PrestaAulaDatabase
import com.example.prestaaula.repository.ReservaRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportesScreen(
    onVolver: () -> Unit
) {
    val context = LocalContext.current

    // Conexión con Room mediante el repositorio.
    val database = remember(context) {
        PrestaAulaDatabase.obtenerBaseDeDatos(context)
    }

    val repository = remember(database) {
        ReservaRepository(database)
    }

    // Reservas guardadas en la base de datos.
    val reservas by remember(repository) {
        repository.obtenerReservas()
    }.collectAsState(initial = emptyList())

    // Cálculos de las estadísticas.
    val totalReservas = reservas.size

    val totalPendientes = reservas.count {
        it.estado == "Pendiente"
    }

    val totalEntregadas = reservas.count {
        it.estado == "Entregada"
    }

    val totalDevueltas = reservas.count {
        it.estado == "Devuelta"
    }

    val totalDanados = reservas.count {
        it.estadoFisicoDevolucion == "Dañado"
    }

    val totalConObservaciones = reservas.count {
        it.estadoFisicoDevolucion == "Con observaciones"
    }

    // Construimos el contenido que se compartirá.
    val contenidoReporte = """
        REPORTE DE PRESTAAULA
        Colegio Marcela Paz

        RESUMEN DE RESERVAS

        Total de reservas: $totalReservas
        Reservas pendientes: $totalPendientes
        Recursos entregados: $totalEntregadas
        Reservas devueltas: $totalDevueltas

        ESTADO FÍSICO DE LAS DEVOLUCIONES

        Devoluciones con daños: $totalDanados
        Devoluciones con observaciones: $totalConObservaciones

        Reporte generado desde la aplicación PrestaAula.
    """.trimIndent()

    // Función para abrir el menú nativo de compartir.
    fun compartirReporte() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Reporte de PrestaAula")
            putExtra(Intent.EXTRA_TEXT, contenidoReporte)
        }

        val selector = Intent.createChooser(
            intent,
            "Compartir reporte de PrestaAula"
        )

        context.startActivity(selector)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Reportes de PrestaAula")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Text(
                    text = "Resumen de reservas",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Estadísticas calculadas a partir de " +
                            "las reservas registradas en el colegio.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            item {
                TarjetaEstadistica(
                    titulo = "Total de reservas",
                    cantidad = totalReservas
                )
            }

            item {
                TarjetaEstadistica(
                    titulo = "Reservas pendientes",
                    cantidad = totalPendientes
                )
            }

            item {
                TarjetaEstadistica(
                    titulo = "Recursos entregados",
                    cantidad = totalEntregadas
                )
            }

            item {
                TarjetaEstadistica(
                    titulo = "Reservas devueltas",
                    cantidad = totalDevueltas
                )
            }

            item {
                HorizontalDivider()

                Text(
                    text = "Estado físico en las devoluciones",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            item {
                TarjetaEstadistica(
                    titulo = "Devoluciones con daños",
                    cantidad = totalDanados
                )
            }

            item {
                TarjetaEstadistica(
                    titulo = "Devoluciones con observaciones",
                    cantidad = totalConObservaciones
                )
            }

            // Botón para compartir el reporte.
            item {
                Button(
                    onClick = {
                        compartirReporte()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Compartir"
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Compartir reporte")
                }
            }

            item {
                Text(
                    text = "Nota: las reservas devueltas antes " +
                            "de implementar el registro físico " +
                            "pueden no tener un estado físico asociado.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
private fun TarjetaEstadistica(
    titulo: String,
    cantidad: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = cantidad.toString(),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
