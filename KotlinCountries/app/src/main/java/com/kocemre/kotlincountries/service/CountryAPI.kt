package com.kocemre.kotlincountries.service

import com.kocemre.kotlincountries.model.Country
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface CountryAPI {

    //https://raw.githubusercontent.com/atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json

    @GET("atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json")
    suspend fun getCountries() : Response<List<Country>>
}