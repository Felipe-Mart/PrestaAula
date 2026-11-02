
package com.example.prestaaula.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.prestaaula.screens.*

object Rutas {

    const val INICIO = "inicio"

    const val MENU_DOCENTE = "menu_docente"
    const val MENU_ENCARGADO = "menu_encargado"
    const val MENU_ADMINISTRADOR = "menu_administrador"

    const val INVENTARIO = "inventario"
    const val DETALLE = "detalle/{recursoId}"
    const val RESERVA = "reserva/{recursoId}"
    const val LISTA_RESERVAS = "lista_reservas"
    const val GESTION_RESERVAS = "gestion_reservas"
    const val REPORTES = "reportes"

    fun detalleDe(recursoId: Int): String {
        return "detalle/$recursoId"
    }

    fun reservaDe(recursoId: Int): String {
        return "reserva/$recursoId"
    }
}

@Composable
fun AppNavigation(
    onCambiarTema: () -> Unit
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.INICIO
    ) {

        composable(Rutas.INICIO) {
            InicioScreen(
                onDocente = {
                    navController.navigate(Rutas.MENU_DOCENTE)
                },
                onEncargado = {
                    navController.navigate(Rutas.MENU_ENCARGADO)
                },
                onAdministracion = {
                    navController.navigate(Rutas.MENU_ADMINISTRADOR)
                },
                onCambiarTema = onCambiarTema
            )
        }

        composable(Rutas.MENU_DOCENTE) {
            MenuDocenteScreen(
                onInventario = {
                    navController.navigate(Rutas.INVENTARIO)
                },
                onReservas = {
                    navController.navigate(Rutas.LISTA_RESERVAS)
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.MENU_ENCARGADO) {
            MenuEncargadoScreen(
                onInventario = {
                    navController.navigate(Rutas.INVENTARIO)
                },
                onReservas = {
                    navController.navigate(Rutas.GESTION_RESERVAS)
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.MENU_ADMINISTRADOR) {
            MenuAdministradorScreen(
                onReservas = {
                    navController.navigate(Rutas.LISTA_RESERVAS)
                },
                onReportes = {
                    navController.navigate(Rutas.REPORTES)
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.INVENTARIO) {
            InventarioScreen(
                onRecursoClick = { recurso ->
                    navController.navigate(
                        Rutas.detalleDe(recurso.id)
                    )
                },
                onVolver = {
                    navController.popBackStack()
                },
                onVerReservas = {
                    navController.navigate(Rutas.LISTA_RESERVAS)
                }
            )
        }

        composable(
            route = Rutas.DETALLE,
            arguments = listOf(
                navArgument("recursoId") {
                    type = NavType.IntType
                }
            )
        ) { entry ->

            val recursoId =
                entry.arguments?.getInt("recursoId") ?: -1

            DetalleRecursoScreen(
                recursoId = recursoId,
                onVolver = {
                    navController.popBackStack()
                },
                onReservar = {
                    navController.navigate(
                        Rutas.reservaDe(recursoId)
                    )
                }
            )
        }

        composable(
            route = Rutas.RESERVA,
            arguments = listOf(
                navArgument("recursoId") {
                    type = NavType.IntType
                }
            )
        ) { entry ->

            val recursoId =
                entry.arguments?.getInt("recursoId") ?: -1

            ReservaScreen(
                recursoId = recursoId,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.LISTA_RESERVAS) {
            ReservasScreen(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.GESTION_RESERVAS) {
            GestionReservasScreen(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.REPORTES) {
            ReportesScreen(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}
