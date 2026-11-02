
package com.example.prestaaula.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        ReservaEntity::class,
        EstadoMantenimientoEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class PrestaAulaDatabase : RoomDatabase() {

    abstract fun reservaDao(): ReservaDao

    abstract fun estadoMantenimientoDao(): EstadoMantenimientoDao

    companion object {

        @Volatile
        private var INSTANCE: PrestaAulaDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {

            override fun migrate(
                db: SupportSQLiteDatabase
            ) {
                db.execSQL(
                    "ALTER TABLE reservas " +
                            "ADD COLUMN estadoFisicoDevolucion TEXT"
                )

                db.execSQL(
                    "ALTER TABLE reservas " +
                            "ADD COLUMN observacionesDevolucion TEXT"
                )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {

            override fun migrate(
                db: SupportSQLiteDatabase
            ) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS
                    estado_mantenimiento (
                        recursoId INTEGER NOT NULL PRIMARY KEY,
                        habilitado INTEGER NOT NULL,
                        observaciones TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        fun obtenerBaseDeDatos(
            context: Context
        ): PrestaAulaDatabase {

            return INSTANCE ?: synchronized(this) {

                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    PrestaAulaDatabase::class.java,
                    "prestaaula_database"
                )
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3
                    )
                    .build()

                INSTANCE = instancia

                instancia
            }
        }
    }
}
