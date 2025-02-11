package com.example.dog_emotions_mobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class FirebaseLogin : AppCompatActivity() {
    private val respuestaLoginUiAuth = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ){
            res: FirebaseAuthUIAuthenticationResult ->
        if(res.resultCode == RESULT_OK){
            if(res.idpResponse != null){
                seLogeo(res.idpResponse!!)
            }
        }
    }
    fun seLogeo(res: IdpResponse){
        val nombre = FirebaseAuth.getInstance().currentUser?.displayName
        cambiarInterfaz(View.INVISIBLE, View.VISIBLE, View.VISIBLE,nombre!!)
        if(res.isNewUser == true){
            registrarUsuarioPorPrimeraVez(res)
        }
        irActividad(Menu::class.java)
    }
    fun cambiarInterfaz(
        visibilidadLogin:Int = View.VISIBLE,
        visibilidadLogout: Int = View.INVISIBLE,
        visibilidadIngresar: Int = View.INVISIBLE,
        textoTextView:String = "Bienvenido"){
        val btnLogin = findViewById<Button>(R.id.btn_login_firebase)
        val btnLogout = findViewById<Button>(R.id.btn_logout_firebase)
        val btnIngresar = findViewById<Button>(R.id.btn_ingresar)
        val tvBienvenida = findViewById<TextView>(R.id.tv_bienvenido)
        btnLogin.visibility = visibilidadLogin
        btnLogout.visibility = visibilidadLogout
        btnIngresar.visibility = visibilidadIngresar
        tvBienvenida.text = textoTextView
    }
    // Registramos en nuestro sistema y ej enviamos correo etc etc
    fun registrarUsuarioPorPrimeraVez(usuario: IdpResponse){}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_firebase_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnLogin = findViewById<Button>(R.id.btn_login_firebase)
        btnLogin.setOnClickListener {
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build()
            )
            val logearseIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers).build()
            respuestaLoginUiAuth.launch(logearseIntent)
        }
        val btnLogout = findViewById<Button>(R.id.btn_logout_firebase)
        btnLogout.setOnClickListener {
            cambiarInterfaz()
            FirebaseAuth.getInstance().signOut()
        }
        val usuario = FirebaseAuth.getInstance().currentUser
        if(usuario !=null){
            cambiarInterfaz(View.INVISIBLE, View.VISIBLE, View.VISIBLE, usuario.displayName!!)
        }

        val botonIngresar= findViewById<Button>(R.id.btn_ingresar)
        botonIngresar
            .setOnClickListener {
                irActividad(Menu::class.java)
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