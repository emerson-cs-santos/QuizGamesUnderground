package br.emerson.quizgamesunderground

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnIniciar.setOnClickListener {
            if( FireBase().getCurrentUser() == null  )
            {
                val providers = arrayListOf( AuthUI.IdpConfig.EmailBuilder().build() )

                val i = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setAvailableProviders(providers)
                    .build()

                startActivityForResult(i,0)
            }
            else
            {
                // configurarFirebase()
                avisoPosAcao( this, "Bem vindo(a) de Volta!" )
                abrirJogo()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ( requestCode == 0 )
        {
            if ( resultCode == RESULT_OK )
            {
               // configurarFirebase()
                avisoPosAcao(this, "Bem vindo(a)!")
                abrirJogo()
            }
            else
            {
                alert( "Problemas", "Usuário não autorizado!", this )
                // finishAffinity()
            }
        }
    }


    fun abrirJogo()
    {
        val i = Intent(this, JogoHomeActivity::class.java)
        startActivity(i)
    }


//    fun getCurrentUser(): FirebaseUser?
//    {
//        val auth = FirebaseAuth.getInstance()
//        return auth.currentUser
//    }


}