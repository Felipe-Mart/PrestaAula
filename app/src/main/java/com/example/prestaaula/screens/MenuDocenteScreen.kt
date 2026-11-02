
package com.example.prestaaula.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuDocenteScreen(
    onInventario: () -> Unit,
    onReservas: () -> Unit,
    onVolver: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menú Docente") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
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
                text = "Bienvenido al módulo docente",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Consulta los recursos disponibles y solicita " +
                        "equipamiento para tus clases."
            )

            Button(
                onClick = onInventario,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.Inventory2,
                    contentDescription = null
                )

                Spacer(Modifier.width(8.dp))

                Text("Consultar inventario")
            }

            OutlinedButton(
                onClick = onReservas,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.EventNote,
                    contentDescription = null
                )

                Spacer(Modifier.width(8.dp))

                Text("Ver reservas registradas")
            }
        }
    }
}
