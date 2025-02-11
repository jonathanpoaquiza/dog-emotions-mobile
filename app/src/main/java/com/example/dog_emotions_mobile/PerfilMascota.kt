package com.example.dog_emotions_mobile

import android.os.Bundle
import android.widget.TextView
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PerfilMascota : AppCompatActivity() {

    private lateinit var ivPerfilMascota: ImageView
    private lateinit var tvNombreMascota: TextView
    private lateinit var tvRazaMascota: TextView
    private lateinit var tvEdadMascota: TextView
    private lateinit var tvPesoMascota: TextView
    private lateinit var tvTamanoMascota: TextView
    private lateinit var tvColorMascota: TextView
    private lateinit var sqliteHelperMascota: SqliteHelperMascota

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil_mascota)

        // Ajuste de márgenes para el sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializando los elementos
        ivPerfilMascota = findViewById(R.id.iv_perfil_mascota)
        tvNombreMascota = findViewById(R.id.tv_nombre_mascota)
        tvRazaMascota = findViewById(R.id.tv_raza_mascota)
        tvEdadMascota = findViewById(R.id.tv_edad_mascota)
        tvPesoMascota = findViewById(R.id.tv_peso_mascota)
        tvTamanoMascota = findViewById(R.id.tv_tamano_mascota)
        tvColorMascota = findViewById(R.id.tv_color_mascota)

        // Inicializar el helper de la base de datos
        sqliteHelperMascota = SqliteHelperMascota(this)

        // Cargar los datos desde la base de datos
        cargarPerfil()
    }

    private fun cargarPerfil() {
        // Obtener el perfil de la mascota (por ejemplo, con id = 1)
        val mascota = sqliteHelperMascota.consultarMascotaPorId(1)

        if (mascota != null) {
            // Asignar los valores a los TextView
            tvNombreMascota.text = mascota.nombre
            tvRazaMascota.text = mascota.raza
            tvEdadMascota.text = "Edad: ${mascota.edad} años"
            tvPesoMascota.text = "Peso: ${mascota.peso} kg"
            tvTamanoMascota.text = "Tamaño: ${mascota.tamano} cm"
            tvColorMascota.text = "Color: ${mascota.color}"

            // Puedes asignar la imagen de la mascota (si existe en el recurso)
            // ivPerfilMascota.setImageResource(mascota.imagenResourceId)
        } else {
            // Manejo en caso de que no se encuentre la mascota en la base de datos
            tvNombreMascota.text = "No se encontró la mascota"
            tvRazaMascota.text = "No disponible"
            tvEdadMascota.text = "No disponible"
            tvPesoMascota.text = "No disponible"
            tvTamanoMascota.text = "No disponible"
            tvColorMascota.text = "No disponible"
        }
    }
}
