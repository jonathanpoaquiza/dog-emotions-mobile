package com.example.dog_emotions_mobile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileOutputStream

class Galeria : AppCompatActivity() {

    // Lista de ImageView para mostrar las imágenes
    private lateinit var imageViews: List<ImageView>

    // Lista para almacenar las URIs de las imágenes seleccionadas
    private val imagenesSeleccionadas = mutableListOf<Uri>()

    // Registra un contrato para obtener el resultado de la selección de imágenes
    private val seleccionarImagen = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // Obtiene la URI de la imagen seleccionada
            val imagenUri: Uri? = result.data?.data
            if (imagenUri != null && imagenesSeleccionadas.size < 4) {
                // Guarda la imagen en almacenamiento interno
                val archivoGuardado = guardarImagenEnAlmacenamientoInterno(imagenUri)
                if (archivoGuardado != null) {
                    imagenesSeleccionadas.add(Uri.fromFile(archivoGuardado))
                    actualizarImagenes()
                } else {
                    Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_galeria)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa los ImageView
        imageViews = listOf(
            findViewById(R.id.imageView1),
            findViewById(R.id.imageView2),
            findViewById(R.id.imageView3),
            findViewById(R.id.imageView4)
        )

        // Cargar imágenes guardadas al iniciar la actividad
        cargarImagenesGuardadas()

        // Configura el botón para subir archivos
        val btnSubirArchivo = findViewById<Button>(R.id.btn_subir_archivo)
        btnSubirArchivo.setOnClickListener {
            if (imagenesSeleccionadas.size < 4) {
                seleccionarImagenDesdeGaleria()
            } else {
                // Muestra un mensaje si ya se han seleccionado 4 imágenes
                Toast.makeText(this, "Máximo 4 imágenes permitidas", Toast.LENGTH_SHORT).show()
            }
        }

        // Configura el botón para volver al menú
        val btnVolverMenu = findViewById<Button>(R.id.btn_volver_menu_multimedia)
        btnVolverMenu.setOnClickListener {
            finish() // Cierra la actividad actual y vuelve al menú
        }
    }

    private fun seleccionarImagenDesdeGaleria() {
        // Crea un Intent para seleccionar una imagen de la galería
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        seleccionarImagen.launch(intent)
    }

    private fun actualizarImagenes() {
        // Recorre la lista de ImageView y asigna las imágenes seleccionadas
        for (i in imagenesSeleccionadas.indices) {
            imageViews[i].setImageURI(imagenesSeleccionadas[i])
        }
    }

    private fun guardarImagenEnAlmacenamientoInterno(imagenUri: Uri): File? {
        return try {
            // Abre un InputStream para leer la imagen seleccionada
            val inputStream = contentResolver.openInputStream(imagenUri)
            // Crea un archivo en el almacenamiento interno
            val archivo = File(filesDir, "imagen_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(archivo)
            // Copia la imagen al archivo
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            archivo // Retorna el archivo guardado
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun cargarImagenesGuardadas() {
        // Obtiene la lista de archivos en el directorio de la aplicación
        val archivos = filesDir.listFiles()
        if (archivos != null) {
            for (archivo in archivos) {
                if (archivo.name.startsWith("imagen_") && archivo.name.endsWith(".jpg")) {
                    imagenesSeleccionadas.add(Uri.fromFile(archivo))
                }
            }
            actualizarImagenes()
        }
    }
}