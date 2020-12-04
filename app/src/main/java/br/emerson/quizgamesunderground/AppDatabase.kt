package br.emerson.quizgamesunderground

import androidx.room.Database
import androidx.room.RoomDatabase
import br.emerson.quizgamesunderground.Model.Pergunta
import br.emerson.quizgamesunderground.Service.PerguntaDao

@Database(entities = arrayOf(Pergunta::class), version = 3)
abstract class AppDatabase: RoomDatabase()
{
    abstract fun perguntaDao(): PerguntaDao
}