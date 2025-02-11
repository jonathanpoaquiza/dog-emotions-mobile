package com.example.dog_emotions_mobile
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteHelperDueno(
    contexto: Context?
) : SQLiteOpenHelper(
    contexto,
    "duenos", // nombre del archivo sqlite
    null,
    1
) {

    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaDueno =
            """
                CREATE TABLE DUENO(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre VARCHAR(100),
                    correo TEXT,
                    celular TEXT,
                    latitud REAL,
                    longitud REAL
                )
            """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaDueno)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun crearDueno(
        nombre: String,
        correo: String,
        celular: String,
        latitud: Double,
        longitud: Double
    ): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresGuardar = ContentValues()
        valoresGuardar.put("nombre", nombre)
        valoresGuardar.put("correo", correo)
        valoresGuardar.put("celular", celular)
        valoresGuardar.put("latitud", latitud)
        valoresGuardar.put("longitud", longitud)

        val resultadoGuardar = baseDatosEscritura.insert(
            "DUENO",
            null,
            valoresGuardar
        )
        baseDatosEscritura.close()
        return resultadoGuardar != -1L
    }

    fun eliminarDueno(id: Int): Boolean {
        val baseDatosEscritura = writableDatabase
        val resultadoEliminar = baseDatosEscritura.delete(
            "DUENO",
            "id=?",
            arrayOf(id.toString())
        )
        baseDatosEscritura.close()
        return resultadoEliminar > 0
    }

    fun actualizarDueno(
        id: Int,
        nombre: String,
        correo: String,
        celular: String,
        latitud: Double,
        longitud: Double
    ): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("correo", correo)
        valoresAActualizar.put("celular", celular)
        valoresAActualizar.put("latitud", latitud)
        valoresAActualizar.put("longitud", longitud)

        val resultadoActualizar = baseDatosEscritura.update(
            "DUENO",
            valoresAActualizar,
            "id=?",
            arrayOf(id.toString())
        )
        baseDatosEscritura.close()
        return resultadoActualizar > 0
    }

    fun consultarDuenoPorId(id: Int): Dueno? {
        val baseDatosLectura = readableDatabase
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            "SELECT * FROM DUENO WHERE id = ?",
            arrayOf(id.toString())
        )

        return if (resultadoConsultaLectura.moveToFirst()) {
            val dueno = Dueno(
                resultadoConsultaLectura.getInt(0),
                resultadoConsultaLectura.getString(1),
                resultadoConsultaLectura.getString(2),
                resultadoConsultaLectura.getString(3),
                resultadoConsultaLectura.getDouble(4),
                resultadoConsultaLectura.getDouble(5)
            )
            resultadoConsultaLectura.close()
            dueno
        } else {
            resultadoConsultaLectura.close()
            null
        }
    }

    fun obtenerTodosLosDuenos(): List<Dueno> {
        val baseDatosLectura = readableDatabase
        val resultadoConsultaLectura = baseDatosLectura.rawQuery("SELECT * FROM DUENO", null)

        val listaDuenos = mutableListOf<Dueno>()
        while (resultadoConsultaLectura.moveToNext()) {
            val dueno = Dueno(
                resultadoConsultaLectura.getInt(0),
                resultadoConsultaLectura.getString(1),
                resultadoConsultaLectura.getString(2),
                resultadoConsultaLectura.getString(3),
                resultadoConsultaLectura.getDouble(4),
                resultadoConsultaLectura.getDouble(5)
            )
            listaDuenos.add(dueno)
        }
        resultadoConsultaLectura.close()
        return listaDuenos
    }
}