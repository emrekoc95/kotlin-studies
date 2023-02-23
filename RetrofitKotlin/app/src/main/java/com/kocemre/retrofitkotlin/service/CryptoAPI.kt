package com.kocemre.retrofitkotlin.service

import android.database.Observable
import com.kocemre.retrofitkotlin.model.CryptoModel
import retrofit2.http.GET

interface CryptoAPI {

    @GET("atilsamancioglu/K21-JSONDataSet/master/crypto.json")
    fun getData(): io.reactivex.rxjava3.core.Observable<List<CryptoModel>>


}