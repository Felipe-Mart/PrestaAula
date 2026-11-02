
package com.example.prestaaula

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.prestaaula.navigation.AppNavigation
import com.example.prestaaula.ui.theme.PrestaAulaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val preferencias = getSharedPreferences(
            "presta_aula_preferencias",
            MODE_PRIVATE
        )

        setContent {
            var modoOscuro by remember {
                mutableStateOf(
                    preferencias.getBoolean("modo_oscuro", false)
                )
            }

            PrestaAulaTheme(
                darkTheme = modoOscuro
            ) {
                AppNavigation(
                    onCambiarTema = {
                        modoOscuro = !modoOscuro

                        preferencias.edit()
                            .putBoolean("modo_oscuro", modoOscuro)
                            .apply()
                    }
                )
            }
        }
    }
}
