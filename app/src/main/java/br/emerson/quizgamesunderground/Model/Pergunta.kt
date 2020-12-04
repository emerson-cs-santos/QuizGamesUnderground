package br.emerson.quizgamesunderground.Model

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pergunta(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    ,var ordem: Int
    ,var pergunta: String
    ,var ajudaDica: String
    ,var imagem: Int
    ,var resposta: Int
    ,var umaRespostaErrada: Int
    ,var opcao1: String
    ,var opcao2: String
    ,var opcao3: String
    ,var opcao4: String
    ,var opcao5: String
)