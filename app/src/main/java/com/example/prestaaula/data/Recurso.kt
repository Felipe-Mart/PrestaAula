package com.example.prestaaula.data

data class Recurso(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val marca: String,
    val codigoInterno: String,
    val estado: String,
    val descripcion: String
)

// Lista temporal de recursos.
// Más adelante estos datos se guardarán de forma local.
val listaRecursos = listOf(

    Recurso(
        id = 1,
        nombre = "Notebook Lenovo ThinkPad",
        categoria = "Notebooks",
        marca = "Lenovo",
        codigoInterno = "NB-001",
        estado = "Disponible",
        descripcion = "Notebook para actividades pedagógicas y presentaciones."
    ),

    Recurso(
        id = 2,
        nombre = "Notebook HP ProBook",
        categoria = "Notebooks",
        marca = "HP",
        codigoInterno = "NB-002",
        estado = "Prestado",
        descripcion = "Notebook destinado al uso de docentes en sala."
    ),

    Recurso(
        id = 3,
        nombre = "Tablet Samsung Galaxy Tab",
        categoria = "Tablets",
        marca = "Samsung",
        codigoInterno = "TB-001",
        estado = "Disponible",
        descripcion = "Tablet para actividades interactivas y apoyo pedagógico."
    ),

    Recurso(
        id = 4,
        nombre = "Proyector Epson PowerLite",
        categoria = "Proyectores",
        marca = "Epson",
        codigoInterno = "PR-001",
        estado = "Disponible",
        descripcion = "Proyector multimedia para salas de clases."
    ),

    Recurso(
        id = 5,
        nombre = "Cable HDMI 3 metros",
        categoria = "Accesorios",
        marca = "Genérico",
        codigoInterno = "AC-001",
        estado = "Disponible",
        descripcion = "Cable HDMI para conectar notebooks a proyectores."
    ),

    Recurso(
        id = 6,
        nombre = "Proyector BenQ",
        categoria = "Proyectores",
        marca = "BenQ",
        codigoInterno = "PR-002",
        estado = "Mantención",
        descripcion = "Proyector que actualmente se encuentra en revisión técnica."
    )
)