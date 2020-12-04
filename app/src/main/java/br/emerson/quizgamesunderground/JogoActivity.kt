package br.emerson.quizgamesunderground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.room.Room
import br.emerson.quizgamesunderground.Model.AjudaPulo
import br.emerson.quizgamesunderground.Model.Pergunta
import br.emerson.quizgamesunderground.Model.Placar
import br.emerson.quizgamesunderground.Service.AjudaPuloService
import kotlinx.android.synthetic.main.activity_jogo.*
import kotlinx.android.synthetic.main.fragment_pergunta.*
import kotlinx.android.synthetic.main.fragment_resultados.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class JogoActivity : AppCompatActivity() {

    var perguntaAtualOrdem: Int = 0
    var placar: Int = 0
    var limiteAjuda: Int = 2
    var limitePular: Int = 1

    var perguntaAtual: Pergunta? = null

    override fun onResume() {

        if ( FireBase().getCurrentUser() == null  )
        {
            avisoPosAcao(this, "Sua sessão expirou!")
            finish()
        }

        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jogo)

        criarPerguntas()
        carregarMain()
    }

    fun carregarMain()
    {
        progressBarJogo.visibility = View.VISIBLE

        val client = OkHttpClient.Builder()
            .connectTimeout(800, TimeUnit.SECONDS)
            .readTimeout(800, TimeUnit.SECONDS)
            .writeTimeout(800, TimeUnit.SECONDS)
            .callTimeout(800, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

        val base = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .client( client )
            .build()

        val ajudaPuloService = base.create( AjudaPuloService::class.java )

        val ajudaPuloCall = ajudaPuloService.ajudaPulo()

        val ajudaPuloCallBack = object : Callback<AjudaPulo>
        {
            override fun onFailure(call: Call<AjudaPulo>, t: Throwable)
            {
                avisoPosAcao(this@JogoActivity, "Falha ao se conectar")
                Log.e( "JogoActivity", "carregarAjudaPulo", t )
            }

            override fun onResponse(call: Call<AjudaPulo>, response: Response<AjudaPulo>)
            {
                if ( response.isSuccessful )
                {
                    val ajudaPuloRetorno = response.body()

                    ajudaPuloRetorno?.let {
                        limitePular = it.pulos
                        limiteAjuda = it.ajudas

                        //criarPerguntas()
                        carregarProximaPergunta()
                    }
                }
                else
                {
                    avisoPosAcao(this@JogoActivity, "Não autorizado" )
                    Log.e( response.code().toString(), response.errorBody().toString() )
                }
                progressBarJogo.visibility = View.GONE
            }
        }
        ajudaPuloCall.enqueue( ajudaPuloCallBack )
    }


    fun carregarProximaPergunta()
    {
        perguntaAtualOrdem += 1

        Thread{
            val bd = Room.databaseBuilder(this, AppDatabase::class.java, "AppDb" ).fallbackToDestructiveMigration().build()
            perguntaAtual = bd.perguntaDao().carregarPergunta( perguntaAtualOrdem )

            runOnUiThread {
                val frag = PerguntaFragment.newInstance()
                supportFragmentManager.beginTransaction().replace( R.id.fragContainer, frag ).commit()
            }

        }.start()
    }


    fun criarPerguntas()
    {
        Thread{
            val bd = Room.databaseBuilder(this, AppDatabase::class.java, "AppDb" ).fallbackToDestructiveMigration().build()
            val perguntas = bd.perguntaDao().listar()

            if ( perguntas.isEmpty() )
            {
                val pergunta1 = Pergunta (
                ordem = 1
                ,pergunta = "Qual o objetivo de Crash no primeiro jogo da série?"
                ,ajudaDica = "Se dar bem"
                ,imagem = R.drawable.pergunta1
                ,resposta = 1
                ,umaRespostaErrada = 5
                ,opcao1 = "Salvar sua namorada, Tawna!"
                ,opcao2 = "Viajar pelas ilhas e conhecer novos amigos."
                ,opcao3 = "Roubar as plantas do castelo de Cortex."
                ,opcao4 = "Se aliar ao Dr. Neo Cortex!"
                ,opcao5 = "Comprar um sofá."
                )
                bd.perguntaDao().inserir( pergunta1 )

                val pergunta2 = Pergunta (
                    ordem = 2
                    ,pergunta = "Qual o nome da primeira fase de Neve de Crash Bandicoot 2: Cortex Strikes Back?"
                    ,ajudaDica = "Vai!"
                    ,imagem = R.drawable.pergunta2
                    ,resposta = 3
                    ,umaRespostaErrada = 1
                    ,opcao1 = "Snow Biz"
                    ,opcao2 = "Arctic Antics"
                    ,opcao3 = "Snow Go"
                    ,opcao4 = "Cold Hard Crash"
                    ,opcao5 = "The Eel Deal"
                )
                bd.perguntaDao().inserir( pergunta2 )

                val pergunta3 = Pergunta (
                    ordem = 3
                    ,pergunta = "Qual jogo da série possui animações em estilos diferentes umas das outras em cada cutcene?"
                    ,ajudaDica = "Segundo jogo do novo estilo visual dos anos 2000..."
                    ,imagem = R.drawable.pergunta3
                    ,resposta = 3
                    ,umaRespostaErrada = 4
                    ,opcao1 = "Crash Bandicoot: The Huge Adventure"
                    ,opcao2 = "Crash of the Titans"
                    ,opcao3 = "Crash: Mind Over Mutant"
                    ,opcao4 = "Crash Twinsanity"
                    ,opcao5 = "Crash Team Racing"
                )
                bd.perguntaDao().inserir( pergunta3 )

                val pergunta4 = Pergunta (
                    ordem = 4
                    ,pergunta = "Qual é o número de chefões (bosses) de Crash Bandicoot 2?"
                    ,ajudaDica = "50 ÷ 10"
                    ,imagem = R.drawable.pergunta4
                    ,resposta = 4
                    ,umaRespostaErrada = 3
                    ,opcao1 = "10"
                    ,opcao2 = "3"
                    ,opcao3 = "7"
                    ,opcao4 = "5"
                    ,opcao5 = "9"
                )
                bd.perguntaDao().inserir( pergunta4 )

                val pergunta5 = Pergunta (
                    ordem = 5
                    ,pergunta = "Qual desses personagens não aparece em Crash Twinsanity?"
                    ,ajudaDica = "Corrida"
                    ,imagem = R.drawable.pergunta5
                    ,resposta = 1
                    ,umaRespostaErrada = 2
                    ,opcao1 = "Nitros Oxide"
                    ,opcao2 = "Tiny Tiger"
                    ,opcao3 = "Crunch Bandicoot"
                    ,opcao4 = "Pura"
                    ,opcao5 = "Nina"
                )
                bd.perguntaDao().inserir( pergunta5 )

                runOnUiThread {

                }
            }
        }.start()
    }


    fun exibeResultado()
    {
        val frag = ResultadosFragment.newInstance()
        supportFragmentManager.beginTransaction().replace( R.id.fragContainer, frag ).commit()
    }

    fun gravarResultado()
    {
        progressBarResultado.visibility = View.VISIBLE
        tvEnviandoResultado.visibility = View.VISIBLE

        var usuarioNome = FireBase().getCurrentUser()?.displayName

        if ( usuarioNome.isNullOrEmpty() )
        {
            usuarioNome = "Sem Nome"
        }

        val placarAtual = Placar(
            usuario = usuarioNome
            ,placar = this.placar
        )

        val novoPlacar = FireBase().database?.child("score")?.push()

        placarAtual.id = novoPlacar?.key

        novoPlacar?.setValue( placarAtual )

        progressBarResultado.visibility = View.INVISIBLE
        tvEnviandoResultado.visibility = View.INVISIBLE

        avisoPosAcao(this, "Placar Gravado!")
        finish()
    }

    // Fecha Jogo atual e volta para a tela de placar para iniciar novo jogo
    fun desistir()
    {
        finish()
    }
}