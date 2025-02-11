package com.example.dog_emotions_mobile

class BaseDeDatos {
    companion object {
        // Instancia de los helpers para cada tabla
        var tablaDueno: SqliteHelperDueno? = null
        var tablaMascota: SqliteHelperMascota? = null
    }
}