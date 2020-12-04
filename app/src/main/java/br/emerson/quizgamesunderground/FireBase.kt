package br.emerson.quizgamesunderground

import android.util.Log
import android.widget.Toast
import br.emerson.quizgamesunderground.Model.Placar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class FireBase
{
    var database: DatabaseReference? = null

    init {
        val usuario = getCurrentUser()

        usuario?.let {
            // Assim só carrega placar do usuário logado, se mudar isso, precisa apagar todos os dados do firebase do realtime database e começar de novo a gravar
            // database = FirebaseDatabase.getInstance().reference.child(usuario.uid)
            database = FirebaseDatabase.getInstance().reference

            val valueEventListener = object : ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot)
                {

                }

                override fun onCancelled(error: DatabaseError)
                {
                    Log.w( "MainActivity", "valueEventListener", error.toException()  )
                }
            }
            database?.addValueEventListener( valueEventListener )
        }
    }

    fun getCurrentUser(): FirebaseUser?
    {
        val auth = FirebaseAuth.getInstance()
        return auth.currentUser
    }
}