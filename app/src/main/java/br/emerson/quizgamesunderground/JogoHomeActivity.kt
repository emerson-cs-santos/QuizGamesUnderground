package br.emerson.quizgamesunderground


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.emerson.quizgamesunderground.Model.Placar
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_jogo_home.*
import kotlinx.android.synthetic.main.placar_item.view.*

class JogoHomeActivity : AppCompatActivity() {

    var placarListaExibir: List<Placar>? = null

    var databaseListar: DatabaseReference? = null

    override fun onResume() {

        if ( FireBase().getCurrentUser() == null  )
        {
            avisoPosAcao(this, "Sua sessão expirou!")
            finish()
        }

        carregarPlacar()

        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jogo_home)

        btnIniciarJogo.setOnClickListener {
            iniciarJogo()
        }

        tvJogoHomeUsuario.text =  "Bem vindo(a) " +  FireBase().getCurrentUser()?.displayName + "!"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate( R.menu.menu_padrao, menu )
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if ( id == R.id.jogar )
        {
            iniciarJogo()
            return true
        }

        if ( id == R.id.sair )
        {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    fun iniciarJogo()
    {
        val i = Intent(this, JogoActivity::class.java)
        startActivity(i)
    }

    fun carregarPlacar()
    {
        progressBarJogoHome.visibility = View.VISIBLE

        val usuario = FireBase().getCurrentUser()

        usuario?.let {
           // Assim só carrega placar do usuário logado, se mudar isso, precisa apagar todos os dados do firebase do realtime database e começar de novo a gravar
            // database = FirebaseDatabase.getInstance().reference.child(usuario.uid)

            databaseListar = FirebaseDatabase.getInstance().reference

            val valueEventListener = object : ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot)
                {
                    atualizarTela( converterDados( snapshot ) )
                }

                override fun onCancelled(error: DatabaseError)
                {
                    Log.w( "class FireBase", "init", error.toException()  )
                }
            }
            databaseListar?.addValueEventListener( valueEventListener )
        }
    }

    fun converterDados( snapshot: DataSnapshot ): List<Placar>
    {
        val listaPlacar = arrayListOf<Placar>()

        snapshot.child("score").children.forEach{
            val mapa = it.getValue() as HashMap< String, Any >

            val id = mapa.get("id") as String
            val placar = mapa.get("placar") as Long
            //val placar = 0
            val usuario = mapa.get("usuario") as String

            val placarItem = Placar( id, usuario , placar.toInt() )

            listaPlacar.add( placarItem )
        }
        return listaPlacar
    }

    fun atualizarTela(placarLista: List<Placar>)
    {
        containerHome.removeAllViews()

        val listaOrdenada = placarLista.sortedByDescending { it.placar }

        listaOrdenada.forEach {
            var placarItem = layoutInflater.inflate( R.layout.placar_item, containerHome, false )

            placarItem.tvPlacarItemNome.text = it.usuario
            placarItem.tvPlacarItemPontos.text = it.placar.toString() + " pontos"

            containerHome.addView( placarItem )
        }

        progressBarJogoHome.visibility = View.GONE
    }


}