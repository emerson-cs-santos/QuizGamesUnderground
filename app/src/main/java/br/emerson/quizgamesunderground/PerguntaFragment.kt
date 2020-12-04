package br.emerson.quizgamesunderground

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import br.emerson.quizgamesunderground.Model.Pergunta
import kotlinx.android.synthetic.main.fragment_pergunta.*
import kotlinx.android.synthetic.main.fragment_pergunta.view.*

class PerguntaFragment : Fragment() {

    var resposta: Int = 0
    var pergunta: Pergunta? = null

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View?
    {
        val fragmento = inflater.inflate(R.layout.fragment_pergunta, container, false)

        fragmento.tvPerguntaQtdPulos.text = "Pulos: " + (activity as JogoActivity).limitePular.toString()
        fragmento.tvPerguntaQtdAjuda.text = "Ajudas: " + (activity as JogoActivity).limiteAjuda.toString()

        // Carregar Pergunta
        fragmento.tvPerguntaProgresso.text = "Pergunta " +  (activity as JogoActivity).perguntaAtualOrdem.toString() + " de 5"
        pergunta = (activity as JogoActivity).perguntaAtual

        fragmento.tvPerguntaDescrição.text = pergunta!!.pergunta
        pergunta?.imagem?.let { fragmento.imgPerguntaImagem.setImageResource(it) }
        fragmento.btnPerguntaResposta1.text = pergunta!!.opcao1
        fragmento.btnPerguntaResposta1.setOnClickListener {
            if ( checarResposta( 1 ) )
            {
                it.setBackgroundColor( getColor(activity as JogoActivity, R.color.respostaCerta) )
                statusResposta(1)
            }
            else
            {
                it.setBackgroundColor( getColor(activity as JogoActivity, R.color.respostaErrada) )
                statusResposta(2)
            }
        }

        fragmento.btnPerguntaResposta2.text = pergunta!!.opcao2
        fragmento.btnPerguntaResposta2.setOnClickListener {
            if ( checarResposta( 2 ) )
            {
                it.setBackgroundColor( getColor(activity as JogoActivity, R.color.respostaCerta) )
                statusResposta(1)
            }
            else
            {
                it.setBackgroundColor( getColor(activity as JogoActivity, R.color.respostaErrada) )
                statusResposta(2)
            }
        }

        fragmento.btnPerguntaResposta3.text = pergunta!!.opcao3
        fragmento.btnPerguntaResposta3.setOnClickListener {
            if ( checarResposta( 3 ) )
            {
                it.setBackgroundColor( getColor(activity as JogoActivity, R.color.respostaCerta) )
                statusResposta(1)
            }
            else
            {
                it.setBackgroundColor( getColor(activity as JogoActivity, R.color.respostaErrada) )
                statusResposta(2)
            }
        }

        fragmento.btnPerguntaResposta4.text = pergunta!!.opcao4
        fragmento.btnPerguntaResposta4.setOnClickListener {
            if ( checarResposta( 4 ) )
            {
                it.setBackgroundColor( getColor(activity as JogoActivity, R.color.respostaCerta) )
                statusResposta(1)
            }
            else
            {
                it.setBackgroundColor( getColor(activity as JogoActivity, R.color.respostaErrada) )
                statusResposta(2)
            }
        }

        fragmento.btnPerguntaResposta5.text = pergunta!!.opcao5
        fragmento.btnPerguntaResposta5.setOnClickListener {
            if ( checarResposta( 5 ) )
            {
                it.setBackgroundColor( getColor(activity as JogoActivity, R.color.respostaCerta) )
                statusResposta(1)
            }
            else
            {
                it.setBackgroundColor( getColor(activity as JogoActivity, R.color.respostaErrada) )
                statusResposta(2)
            }
        }

        fragmento.btnPerguntaProxima.setOnClickListener {

            if ( (activity as JogoActivity).perguntaAtual!!.ordem == 5 )
            {
                (activity as JogoActivity).exibeResultado()
            }
            else
            {
                (activity as JogoActivity).carregarProximaPergunta()
            }
        }

        fragmento.btnPerguntaPular.setOnClickListener {
            pularPergunta()
        }

        fragmento.btnPerguntaAjuda.setOnClickListener {
            ajuda()
        }

        fragmento.btnPerguntaDesistir.setOnClickListener {
            (activity as JogoActivity).desistir()
        }

        return fragmento
    }

    fun checarResposta( resp: Int ): Boolean
    {
        return pergunta!!.resposta == resp
    }

    fun statusResposta( tipo: Int )
    {
        if( tipo == 1 || tipo == 3 )
        {
            val somarPlacar =   (activity as JogoActivity).placar + 1
            (activity as JogoActivity).placar = somarPlacar
        }

        if ( tipo == 1 )
        {
            this.tvPerguntaStatus.text = "Certa a Resposta =D!!"
        }

        if ( tipo == 2 )
        {
            this.tvPerguntaStatus.text = "Resposta Errada... ):"
        }

        if ( tipo == 3 )
        {
            this.tvPerguntaStatus.text = "Pulou! =O"
        }

        desabilitarOpcoes()
        this.btnPerguntaProxima.isEnabled = true
    }

    fun pularPergunta()
    {
        if ( (activity as JogoActivity).limitePular > 0 )
        {
            statusResposta(3)
            val pulos = (activity as JogoActivity).limitePular - 1
            (activity as JogoActivity).limitePular = pulos
            this.tvPerguntaQtdPulos.text = "Pulos: ${pulos}"

            desabilitarOpcoes()
        }
        else
        {
            alert("Quiz", "Sem pulos disponíveis!", activity as JogoActivity)
        }

        this.tvPerguntaQtdPulos.text = "Pulos: " + (activity as JogoActivity).limitePular.toString()
    }

    fun ajuda()
    {
        // Elimina uma resposta errada
        if ( (activity as JogoActivity).limiteAjuda > 1 )
        {
            val respErrada = (activity as JogoActivity).perguntaAtual!!.umaRespostaErrada

            when(respErrada)
            {
                1 -> this.btnPerguntaResposta1.isEnabled = false
                2 -> this.btnPerguntaResposta2.isEnabled = false
                3 -> this.btnPerguntaResposta3.isEnabled = false
                4 -> this.btnPerguntaResposta4.isEnabled = false
                5 -> this.btnPerguntaResposta5.isEnabled = false
            }

           avisoPosAcao(activity as JogoActivity, "Uma opção incorreta foi desabilitada!")
        }

        // Exibe dica
        if ( (activity as JogoActivity).limiteAjuda == 1 )
        {
            this.tvPerguntaStatus.text = "Dica: " + (activity as JogoActivity).perguntaAtual!!.ajudaDica
        }

        if ( (activity as JogoActivity).limiteAjuda == 0 )
        {
            alert("Quiz", "Sem ajuda disponível!", activity as JogoActivity)
        }
        else
        {
            val ajudas = (activity as JogoActivity).limiteAjuda - 1
            (activity as JogoActivity).limiteAjuda = ajudas
            this.btnPerguntaAjuda.isEnabled = false
        }

        this.tvPerguntaQtdAjuda.text = "Ajudas: " +  (activity as JogoActivity).limiteAjuda.toString()
    }

    fun desabilitarOpcoes()
    {
        this.btnPerguntaResposta1.isEnabled = false
        this.btnPerguntaResposta2.isEnabled = false
        this.btnPerguntaResposta3.isEnabled = false
        this.btnPerguntaResposta4.isEnabled = false
        this.btnPerguntaResposta5.isEnabled = false
        this.btnPerguntaAjuda.isEnabled = false
        this.btnPerguntaPular.isEnabled = false
    }

    companion object {
        @JvmStatic
        fun newInstance() = PerguntaFragment()
    }
}