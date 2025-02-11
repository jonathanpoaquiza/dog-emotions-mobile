package com.example.dog_emotions_mobile

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar
import kotlin.math.*

class Ubicacion : AppCompatActivity() {
    private lateinit var mapa: GoogleMap
    private var permisos = false
    private val nombrePermisoFine = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val nombrePermisoCoarse = android.Manifest.permission.ACCESS_COARSE_LOCATION
    private lateinit var sqliteHelperDueno: SqliteHelperDueno
    private lateinit var sqliteHelperMascota: SqliteHelperMascota

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ubicacion)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar el helper de la base de datos
        sqliteHelperDueno = SqliteHelperDueno(this)
        sqliteHelperMascota = SqliteHelperMascota(this)

        solicitarPermisos()
        inicializarLogicaMapa()

        val botonVolverMenu = findViewById<Button>(R.id.btn_volver_menu_ubicacion)
        botonVolverMenu.setOnClickListener {
            irActividad(Menu::class.java)
        }

        val botonCalcularDistancia = findViewById<Button>(R.id.btn_calcular_distancia)
        botonCalcularDistancia.setOnClickListener {
            calcularDistancia()
        }
    }

    private fun calcularDistancia() {
        val mascota = sqliteHelperMascota.consultarMascotaPorId(1) // Método para obtener la mascota por ID
        val duenio = sqliteHelperDueno.consultarDuenoPorId(1) // Método para obtener el dueño por ID

        if (mascota != null && duenio != null) {
            val latitudMascota = mascota.latitud
            val longitudMascota = mascota.longitud

            val latitudDuenio = duenio.latitud
            val longitudDuenio = duenio.longitud

            if (latitudMascota != 0.0 && longitudMascota != 0.0 &&
                latitudDuenio != 0.0 && longitudDuenio != 0.0
            ) {
                val distancia = calcularDistanciaEntrePuntos(latitudDuenio, longitudDuenio, latitudMascota, longitudMascota)
                val distanciaFormateada = "%.2f km".format(distancia / 1000) // Convertir a kilómetros y formatear
                val tvDistancia = findViewById<TextView>(R.id.tv_distancia)
                tvDistancia.text = "Distancia: $distanciaFormateada"
            } else {
                mostrarSnackbar("Las coordenadas del dueño o la mascota no son válidas.")
            }
        } else {
            mostrarSnackbar("No se encontró al dueño o a la mascota.")
        }
    }

    private fun calcularDistanciaEntrePuntos(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val radioTierra = 6371000 // Radio de la Tierra en metros
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return radioTierra * c
    }

    fun irActividad(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        startActivity(intentExplicito)
    }

    private fun tengoPermisos(): Boolean {
        val contexto = applicationContext
        val permisoFine = ContextCompat.checkSelfPermission(contexto, nombrePermisoFine)
        val permisoCoarse = ContextCompat.checkSelfPermission(contexto, nombrePermisoCoarse)
        permisos = permisoFine == PackageManager.PERMISSION_GRANTED &&
                permisoCoarse == PackageManager.PERMISSION_GRANTED
        return permisos
    }

    private fun solicitarPermisos() {
        if (!tengoPermisos()) {
            ActivityCompat.requestPermissions(
                this, arrayOf(nombrePermisoFine, nombrePermisoCoarse), 1
            )
        }
    }

    private fun inicializarLogicaMapa() {
        val fragmentoMapa = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        fragmentoMapa.getMapAsync { googleMap ->
            with(googleMap) {
                mapa = googleMap
                establecerConfiguracionMapa()
                obtenerUbicaciones()
            }
        }
    }

    private fun moverUbicacion(lat: Double, lng: Double, titulo: String, color: Float) {
        val ubicacion = LatLng(lat, lng)
        val marcador = anadirMarcador(ubicacion, titulo, color)
        marcador.tag = titulo
    }

    private fun anadirMarcador(latLang: LatLng, title: String, color: Float): Marker {
        return mapa.addMarker(
            MarkerOptions()
                .position(latLang)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(color)) // Asignar color al marcador
        )!!
    }

    private fun trazarLineaEntreMarcadores(lat1: Double, lng1: Double, lat2: Double, lng2: Double) {
        val punto1 = LatLng(lat1, lng1)
        val punto2 = LatLng(lat2, lng2)

        mapa.addPolyline(
            PolylineOptions()
                .add(punto1, punto2)
                .width(5f) // Ancho de la línea
                .color(Color.RED) // Color de la línea
        )
    }

    @SuppressLint("MissingPermission")
    private fun establecerConfiguracionMapa() {
        with(mapa) {
            if (tengoPermisos()) {
                isMyLocationEnabled = true
                uiSettings.isMyLocationButtonEnabled = true
            }
            uiSettings.isZoomControlsEnabled = true
        }
    }

    private fun moverCamaraConZoom(latLang: LatLng, zoom: Float = 17f) {
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang, zoom))
    }

    private fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.main),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }

    private fun obtenerUbicaciones() {
        val mascota = sqliteHelperMascota.consultarMascotaPorId(1) // Método para obtener la mascota por ID
        val duenio = sqliteHelperDueno.consultarDuenoPorId(1) // Método para obtener el dueño por ID

        if (mascota != null && duenio != null) {
            val latitudMascota = mascota.latitud
            val longitudMascota = mascota.longitud
            val nombreMascota = mascota.nombre

            val latitudDuenio = duenio.latitud
            val longitudDuenio = duenio.longitud
            val nombreDuenio = duenio.nombre

            if (latitudMascota != 0.0 && longitudMascota != 0.0 &&
                latitudDuenio != 0.0 && longitudDuenio != 0.0
            ) {
                // Agregar marcadores con colores personalizados
                moverUbicacion(latitudMascota, longitudMascota, nombreMascota, BitmapDescriptorFactory.HUE_BLUE)
                moverUbicacion(latitudDuenio, longitudDuenio, nombreDuenio, BitmapDescriptorFactory.HUE_GREEN)

                // Trazar una línea entre los marcadores
                trazarLineaEntreMarcadores(latitudDuenio, longitudDuenio, latitudMascota, longitudMascota)

                // Mover la cámara a un punto intermedio
                val puntoIntermedio = calcularPuntoIntermedio(
                    latitudDuenio, longitudDuenio, latitudMascota, longitudMascota
                )
                moverCamaraConZoom(puntoIntermedio)
            } else {
                mostrarSnackbar("Las coordenadas del dueño o la mascota no son válidas.")
            }
        } else {
            mostrarSnackbar("No se encontró al dueño o a la mascota.")
        }
    }

    private fun calcularPuntoIntermedio(lat1: Double, lng1: Double, lat2: Double, lng2: Double): LatLng {
        val latitudIntermedia = (lat1 + lat2) / 2
        val longitudIntermedia = (lng1 + lng2) / 2
        return LatLng(latitudIntermedia, longitudIntermedia)
    }
}