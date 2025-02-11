package com.example.dog_emotions_mobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class CrudMascota : AppCompatActivity() {

    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.main),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }

    fun irActividad(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        startActivity(intentExplicito)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crud_mascota)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val botonCrearBDD = findViewById<Button>(R.id.btn_crear_mascota)
        botonCrearBDD.setOnClickListener {
            val nombre = findViewById<EditText>(R.id.input_mascota_nombre)
            val raza = findViewById<EditText>(R.id.input_mascota_raza)
            val edad = findViewById<EditText>(R.id.input_mascota_edad)
            val peso = findViewById<EditText>(R.id.input_mascota_peso)
            val tamaño = findViewById<EditText>(R.id.input_mascota_tamaño)
            val color = findViewById<EditText>(R.id.input_mascota_color)
            val latitud = findViewById<EditText>(R.id.input_mascota_latitud)
            val longitud = findViewById<EditText>(R.id.input_mascota_longitud)

            val edadMascota = edad.text.toString().toIntOrNull()
            val pesoMascota = peso.text.toString().toDoubleOrNull()
            val lat = latitud.text.toString().toDoubleOrNull()
            val lon = longitud.text.toString().toDoubleOrNull()

            if (edadMascota != null && pesoMascota != null && lat != null && lon != null) {
                val respuesta = SqliteHelperMascota(this).crearMascota(
                    nombre.text.toString(),
                    raza.text.toString(),
                    edadMascota,
                    pesoMascota,
                    tamaño.text.toString(),
                    color.text.toString(),
                    lat,
                    lon
                )

                if (respuesta) {
                    mostrarSnackbar("Mascota creada")
                    irActividad(PerfilMascota::class.java)
                } else {
                    mostrarSnackbar("Fallo")
                }
            } else {
                mostrarSnackbar("Por favor ingresa edad válida, peso, latitud y longitud")
            }
        }

        val botonActualizarBDD = findViewById<Button>(R.id.btn_actualizar_mascota)
        botonActualizarBDD.setOnClickListener {
            val id = findViewById<EditText>(R.id.input_mascota_id)
            val nombre = findViewById<EditText>(R.id.input_mascota_nombre)
            val raza = findViewById<EditText>(R.id.input_mascota_raza)
            val edad = findViewById<EditText>(R.id.input_mascota_edad)
            val peso = findViewById<EditText>(R.id.input_mascota_peso)
            val tamaño = findViewById<EditText>(R.id.input_mascota_tamaño)
            val color = findViewById<EditText>(R.id.input_mascota_color)
            val latitud = findViewById<EditText>(R.id.input_mascota_latitud)
            val longitud = findViewById<EditText>(R.id.input_mascota_longitud)

            val edadMascota = edad.text.toString().toIntOrNull()
            val pesoMascota = peso.text.toString().toDoubleOrNull()
            val lat = latitud.text.toString().toDoubleOrNull()
            val lon = longitud.text.toString().toDoubleOrNull()

            if (edadMascota != null && pesoMascota != null && lat != null && lon != null) {
                val respuesta = SqliteHelperMascota(this).actualizarMascota(
                    id.text.toString().toInt(),
                    nombre.text.toString(),
                    raza.text.toString(),
                    edadMascota,
                    pesoMascota,
                    tamaño.text.toString(),
                    color.text.toString(),
                    lat,
                    lon
                )

                if (respuesta) {
                    mostrarSnackbar("Mascota actualizada")
                    irActividad(PerfilMascota::class.java)
                } else {
                    mostrarSnackbar("Fallo")
                }
            } else {
                mostrarSnackbar("Por favor ingresa edad válida, peso, latitud y longitud")
            }
        }

        val botonEliminarBDD = findViewById<Button>(R.id.btn_eliminar_mascota)
        botonEliminarBDD.setOnClickListener {
            val id = findViewById<EditText>(R.id.input_mascota_id)
            val respuesta = SqliteHelperMascota(this).eliminarMascota(id.text.toString().toInt())

            if (respuesta) {
                mostrarSnackbar("Mascota eliminada")
                irActividad(PerfilMascota::class.java)
            } else {
                mostrarSnackbar("Fallo al eliminar")
            }
        }
    }
}
