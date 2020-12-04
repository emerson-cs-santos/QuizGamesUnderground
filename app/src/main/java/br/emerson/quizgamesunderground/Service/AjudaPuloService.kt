package br.emerson.quizgamesunderground.Service


import br.emerson.quizgamesunderground.Model.AjudaPulo
import retrofit2.Call
import retrofit2.http.GET

interface AjudaPuloService
{
    @GET("/api/quiz")
   fun ajudaPulo(): Call<AjudaPulo>
}