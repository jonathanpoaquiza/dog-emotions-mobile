package com.example.dog_emotions_mobile

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PerfilDueno : AppCompatActivity() {

    private lateinit var tvNombreDueno: TextView
    private lateinit var tvCorreoDueno: TextView
    private lateinit var tvCelularDueno: TextView
    private lateinit var sqliteHelperDueno: SqliteHelperDueno

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil_dueno)

        // Ajuste de márgenes para el sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializando los TextView
        tvNombreDueno = findViewById(R.id.tv_nombre_dueno)
        tvCorreoDueno = findViewById(R.id.tv_correo_dueno)
        tvCelularDueno = findViewById(R.id.tv_celular_dueno)

        // Inicializar el helper de la base de datos
        sqliteHelperDueno = SqliteHelperDueno(this)

        // Cargar los datos desde la base de datos
        cargarPerfil()
    }

    private fun cargarPerfil() {
        // Obtener el perfil del dueño (por ejemplo, con id = 1)
        val dueno = sqliteHelperDueno.consultarDuenoPorId(1)

        if (dueno != null) {
            // Asignar los valores a los TextView
            tvNombreDueno.text = dueno.nombre
            tvCorreoDueno.text = dueno.correo
            tvCelularDueno.text = dueno.celular
        } else {
            // Manejo en caso de que no se encuentre el dueño en la base de datos
            tvNombreDueno.text = "No se encontró el dueño"
            tvCorreoDueno.text = "No disponible"
            tvCelularDueno.text = "No disponible"
        }
    }
}
