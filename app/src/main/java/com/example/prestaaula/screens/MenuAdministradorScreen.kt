
package com.example.prestaaula.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuAdministradorScreen(
    onReservas: () -> Unit,
    onReportes: () -> Unit,
    onVolver: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menú Administrador") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Administración de PrestaAula",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Consulta el historial y las estadísticas " +
                        "de los préstamos del colegio."
            )

            Button(
                onClick = onReportes,
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    imageVector = Icons.Default.Assessment,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Ver reportes y estadísticas")
            }

            OutlinedButton(
                onClick = onReservas,
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    imageVector = Icons.Default.EventNote,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Consultar historial de reservas")
            }
        }
    }
}
