package com.example.dog_emotions_mobile
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate

class CrudDueno : AppCompatActivity() {

    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.main),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }

    fun irActividad(
        clase: Class<*>
    ){
        val intentExplicito = Intent(
            this,
            clase
        )
        startActivity(intentExplicito)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crud_dueno)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val botonCrearBDD = findViewById<Button>(R.id.btn_crear_dueno)
        botonCrearBDD.setOnClickListener {
            val nombre = findViewById<EditText>(R.id.input_dueno_nombre)
            val correo = findViewById<EditText>(R.id.input_dueno_correo)
            val celular = findViewById<EditText>(R.id.input_dueno_celular)
            val latitud = findViewById<EditText>(R.id.input_dueno_latitud)
            val longitud = findViewById<EditText>(R.id.input_dueno_longitud)

            val lat = latitud.text.toString().toDoubleOrNull()
            val lon = longitud.text.toString().toDoubleOrNull()

            if (lat != null && lon != null) {
                val respuesta = SqliteHelperDueno(this).crearDueno(
                    nombre.text.toString(),
                    correo.text.toString(),
                    celular.text.toString(),
                    lat,
                    lon
                )

                if (respuesta) {
                    mostrarSnackbar("Dueño creado")
                    irActividad(PerfilDueno::class.java)
                } else {
                    mostrarSnackbar("Fallo")
                }
            } else {
                mostrarSnackbar("Por favor ingresa coordenadas válidas de latitud y longitud")
            }
        }

        val botonActualizarBDD = findViewById<Button>(R.id.btn_actualizar_dueno)
        botonActualizarBDD.setOnClickListener {
            val id = findViewById<EditText>(R.id.input_dueno_id)
            val nombre = findViewById<EditText>(R.id.input_dueno_nombre)
            val correo = findViewById<EditText>(R.id.input_dueno_correo)
            val celular = findViewById<EditText>(R.id.input_dueno_celular)
            val latitud = findViewById<EditText>(R.id.input_dueno_latitud)
            val longitud = findViewById<EditText>(R.id.input_dueno_longitud)

            val lat = latitud.text.toString().toDoubleOrNull()
            val lon = longitud.text.toString().toDoubleOrNull()

            if (lat != null && lon != null) {
                val respuesta = SqliteHelperDueno(this).actualizarDueno(
                    id.text.toString().toInt(),
                    nombre.text.toString(),
                    correo.text.toString(),
                    celular.text.toString(),
                    lat,
                    lon
                )

                if (respuesta) {
                    mostrarSnackbar("Dueño actualizado")
                    irActividad(PerfilDueno::class.java)
                } else {
                    mostrarSnackbar("Fallo")
                }
            } else {
                mostrarSnackbar("Por favor ingresa coordenadas válidas de latitud y longitud")
            }
        }

        val botonEliminarBDD = findViewById<Button>(R.id.btn_eliminar_dueno)
        botonEliminarBDD.setOnClickListener {
            val id = findViewById<EditText>(R.id.input_dueno_id)
            val respuesta = SqliteHelperDueno(this).eliminarDueno(id.text.toString().toInt())

            if (respuesta) {
                mostrarSnackbar("Dueño eliminado")
                irActividad(PerfilDueno::class.java)
            } else {
                mostrarSnackbar("Fallo al eliminar")
            }
        }
    }
}