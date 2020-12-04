package br.emerson.quizgamesunderground

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_resultados.view.*

class ResultadosFragment : Fragment() {

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View?
    {
        val fragmento =  inflater.inflate(R.layout.fragment_resultados, container, false)

        fragmento.tvResultadoPontos.text = (activity as JogoActivity).placar.toString() + " de 5 acertadas!"

        val pontos = (activity as JogoActivity).placar

        if ( pontos == 5 )
        {
            fragmento.tvResultadoStatus.text = "Ganhou!!!! Acertou todas as perguntas!"
        }

        if (pontos in 3..4)
        {
            fragmento.tvResultadoStatus.text = "Quase ganhou!! Acertou ${pontos}."
        }

        if (pontos in 1..2)
        {
            fragmento.tvResultadoStatus.text = "Acertou Pouco! Acertou ${pontos}."
        }

        if (pontos == 0)
        {
            fragmento.tvResultadoStatus.text = "Nenhum acerto..."
        }

        fragmento.btnResultadoGravar.setOnClickListener {
            (activity as JogoActivity).gravarResultado()
        }

        fragmento.btnResultadoSair.setOnClickListener {
            (activity as JogoActivity).desistir()
        }


        return fragmento
    }

    companion object {
        @JvmStatic
        fun newInstance() = ResultadosFragment()
    }
}