package br.emerson.quizgamesunderground.Service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.emerson.quizgamesunderground.Model.Pergunta

@Dao
interface PerguntaDao
{
    @Insert
    fun inserir(pergunta: Pergunta)

    @Query(value = "select * from Pergunta")
    fun listar(): List<Pergunta>

    @Query(value = "select * from Pergunta where ordem = :ordemPergunta")
    fun carregarPergunta( ordemPergunta: Int? ): Pergunta
}