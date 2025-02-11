package com.example.dog_emotions_mobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val botonPerfilDueno = findViewById<Button>(R.id.btn_dueno)
        botonPerfilDueno
            .setOnClickListener {
                irActividad(PerfilDueno::class.java)
            }

        val botonPerfilMascota = findViewById<Button>(R.id.btn_mascota)
        botonPerfilMascota
            .setOnClickListener {
                irActividad(PerfilMascota::class.java)
            }

        val botonUbicacion = findViewById<Button>(R.id.btn_ubicacion)
        botonUbicacion
            .setOnClickListener {
                irActividad(Ubicacion::class.java)
            }

        val botonGaleria = findViewById<Button>(R.id.btn_galeria)
        botonGaleria
            .setOnClickListener {
                irActividad(Galeria::class.java)
            }

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
}