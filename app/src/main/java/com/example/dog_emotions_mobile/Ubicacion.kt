package com.example.dog_emotions_mobile

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar

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
        val fragmentoMapa = supportFragmentManager.findFragmentById(
            R.id.map
        ) as SupportMapFragment
        fragmentoMapa.getMapAsync{ googleMap ->
            with(googleMap)
            {
                mapa = googleMap
                establecerConfiguracionMapa()
                obtenerUbicaciones()
            }
        }
    }


    private fun moverUbicacion(lat: Double, lng: Double, titulo: String) {
        val ubicacion = LatLng(lat, lng)
        val marcador = anadirMarcador(ubicacion, titulo)
        marcador.tag = titulo
    }

    private fun anadirPolilinea(lat: Double, lng: Double) {
        with(mapa) {
            val polilinea = addPolyline(
                PolylineOptions()
                    .clickable(true)
                    .add(
                        LatLng(lat + 0.001, lng - 0.001),
                        LatLng(lat - 0.001, lng + 0.001),
                        LatLng(lat + 0.002, lng + 0.002)
                    )
            )
            polilinea.tag = "polilinea-uno"
        }
    }

    private fun anadirPoligono(lat: Double, lng: Double) {
        with(mapa) {
            val poligono = addPolygon(
                PolygonOptions()
                    .clickable(true)
                    .add(
                        LatLng(lat + 0.0015, lng - 0.0015),
                        LatLng(lat - 0.0015, lng - 0.0015),
                        LatLng(lat - 0.0015, lng + 0.0015),
                        LatLng(lat + 0.0015, lng + 0.0015)
                    )
            )
            poligono.tag = "poligono-uno"
        }
    }

    private fun escucharListeners() {
        mapa.setOnPolygonClickListener {
            mostrarSnackbar("setOnPolygonClickListener ${it.tag}")
        }
        mapa.setOnPolylineClickListener {
            mostrarSnackbar("setOnPolylineClickListener ${it.tag}")
        }
        mapa.setOnMarkerClickListener {
            mostrarSnackbar("setOnMarkerClickListener ${it.tag}")
            return@setOnMarkerClickListener true
        }
        mapa.setOnCameraIdleListener { mostrarSnackbar("setOnCameraIdleListener") }
        mapa.setOnCameraMoveListener { mostrarSnackbar("setOnCameraMoveListener") }
        mapa.setOnCameraMoveStartedListener { mostrarSnackbar("setOnCameraMoveStartedListener") }
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

    private fun anadirMarcador(latLang: LatLng, title: String): Marker {
        return mapa.addMarker(MarkerOptions().position(latLang).title(title))!!
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
        // Obtener la ubicación de la mascota desde la base de datos (Ejemplo de ID 1)
        val mascota = sqliteHelperMascota.consultarMascotaPorId(1) // Método para obtener la mascota por ID
        val duenio = sqliteHelperDueno.consultarDuenoPorId(1) // Método para obtener el dueño por ID

        if (mascota != null && duenio != null) {
            val latitudMascota = mascota.latitud
            val longitudMascota = mascota.longitud
            val nombreMascota = mascota.nombre

            val latitudDuenio = duenio.latitud
            val longitudDuenio = duenio.longitud
            val nombreDuenio = duenio.nombre

            // Verificar que las coordenadas sean válidas
            if (latitudMascota != 0.0 && longitudMascota != 0.0 &&
                latitudDuenio != 0.0 && longitudDuenio != 0.0
            ) {
                // Agregar marcadores para el dueño y la mascota
                moverUbicacion(latitudMascota, longitudMascota, nombreMascota)
                moverUbicacion(latitudDuenio, longitudDuenio, nombreDuenio)

                // Calcular un punto intermedio para mover la cámara
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