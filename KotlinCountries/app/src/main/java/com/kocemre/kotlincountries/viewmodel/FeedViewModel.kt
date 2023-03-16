package com.kocemre.kotlincountries.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kocemre.kotlincountries.model.Country
import com.kocemre.kotlincountries.service.CountryAPIService
import kotlinx.coroutines.*
import retrofit2.Response


class FeedViewModel : ViewModel() {

    private val countryAPIService = CountryAPIService()
    private var job: Job? = null

    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        countryError.value = true
        countryLoading.value = false
    }

    private var response: Response<List<Country>>? = null


    fun refreshData() {
        getDataFromAPI()
    }

    private fun getDataFromAPI() {
        countryLoading.value = true
        job = CoroutineScope(viewModelScope.coroutineContext + Dispatchers.IO).launch {
            supervisorScope {
                response = countryAPIService.getData()

                launch(Dispatchers.Main + exceptionHandler) {


                    response?.let {
                        if(!it.isSuccessful){
                            countryLoading.value = false
                            countryError.value = true
                        }else{
                            countryLoading.value = false


                            it.body()?.let { countriesList ->
                                countries.value = countriesList
                            }
                        }

                    }

                }
            }


        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}

