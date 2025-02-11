package com.example.dog_emotions_mobile

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteHelperMascota(
    contexto: Context?
) : SQLiteOpenHelper(
    contexto,
    "mascotas", // nombre del archivo sqlite
    null,
    2  // Actualizar la versión de la base de datos
) {

    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaMascota =
            """
                CREATE TABLE MASCOTA(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre VARCHAR(100),
                    raza VARCHAR(50),
                    edad INTEGER,
                    peso REAL,
                    tamaño VARCHAR(50),
                    color VARCHAR(50),
                    latitud REAL,   -- Nueva columna para latitud
                    longitud REAL   -- Nueva columna para longitud
                )
            """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaMascota)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            val scriptSQLAgregarColumnas =
                """
                    ALTER TABLE MASCOTA ADD COLUMN latitud REAL;
                    ALTER TABLE MASCOTA ADD COLUMN longitud REAL;
                """.trimIndent()
            db?.execSQL(scriptSQLAgregarColumnas)
        }
    }

    fun crearMascota(
        nombre: String,
        raza: String,
        edad: Int,
        peso: Double,
        tamaño: String,
        color: String,
        latitud: Double,
        longitud: Double
    ): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresGuardar = ContentValues()
        valoresGuardar.put("nombre", nombre)
        valoresGuardar.put("raza", raza)
        valoresGuardar.put("edad", edad)
        valoresGuardar.put("peso", peso)
        valoresGuardar.put("tamaño", tamaño)
        valoresGuardar.put("color", color)
        valoresGuardar.put("latitud", latitud)   // Guardar latitud
        valoresGuardar.put("longitud", longitud) // Guardar longitud

        val resultadoGuardar = baseDatosEscritura.insert(
            "MASCOTA",
            null,
            valoresGuardar
        )
        baseDatosEscritura.close()
        return resultadoGuardar != -1L
    }

    fun eliminarMascota(id: Int): Boolean {
        val baseDatosEscritura = writableDatabase
        val resultadoEliminar = baseDatosEscritura.delete(
            "MASCOTA",
            "id=?",
            arrayOf(id.toString())
        )
        baseDatosEscritura.close()
        return resultadoEliminar > 0
    }

    fun actualizarMascota(
        id: Int,
        nombre: String,
        raza: String,
        edad: Int,
        peso: Double,
        tamaño: String,
        color: String,
        latitud: Double,
        longitud: Double
    ): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("raza", raza)
        valoresAActualizar.put("edad", edad)
        valoresAActualizar.put("peso", peso)
        valoresAActualizar.put("tamaño", tamaño)
        valoresAActualizar.put("color", color)
        valoresAActualizar.put("latitud", latitud)   // Actualizar latitud
        valoresAActualizar.put("longitud", longitud) // Actualizar longitud

        val resultadoActualizar = baseDatosEscritura.update(
            "MASCOTA",
            valoresAActualizar,
            "id=?",
            arrayOf(id.toString())
        )
        baseDatosEscritura.close()
        return resultadoActualizar > 0
    }

    fun consultarMascotaPorId(id: Int): Mascota? {
        val baseDatosLectura = readableDatabase
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            "SELECT * FROM MASCOTA WHERE id = ?",
            arrayOf(id.toString())
        )

        return if (resultadoConsultaLectura.moveToFirst()) {
            val mascota = Mascota(
                resultadoConsultaLectura.getInt(0),
                resultadoConsultaLectura.getString(1),
                resultadoConsultaLectura.getString(2),
                resultadoConsultaLectura.getInt(3),
                resultadoConsultaLectura.getDouble(4),
                resultadoConsultaLectura.getString(5),
                resultadoConsultaLectura.getString(6),
                resultadoConsultaLectura.getDouble(7),  // Leer latitud
                resultadoConsultaLectura.getDouble(8)   // Leer longitud
            )
            resultadoConsultaLectura.close()
            mascota
        } else {
            resultadoConsultaLectura.close()
            null
        }
    }

    fun obtenerTodasLasMascotas(): List<Mascota> {
        val baseDatosLectura = readableDatabase
        val resultadoConsultaLectura = baseDatosLectura.rawQuery("SELECT * FROM MASCOTA", null)

        val listaMascotas = mutableListOf<Mascota>()
        while (resultadoConsultaLectura.moveToNext()) {
            val mascota = Mascota(
                resultadoConsultaLectura.getInt(0),
                resultadoConsultaLectura.getString(1),
                resultadoConsultaLectura.getString(2),
                resultadoConsultaLectura.getInt(3),
                resultadoConsultaLectura.getDouble(4),
                resultadoConsultaLectura.getString(5),
                resultadoConsultaLectura.getString(6),
                resultadoConsultaLectura.getDouble(7),  // Obtener latitud
                resultadoConsultaLectura.getDouble(8)   // Obtener longitud
            )
            listaMascotas.add(mascota)
        }
        resultadoConsultaLectura.close()
        return listaMascotas
    }
}
