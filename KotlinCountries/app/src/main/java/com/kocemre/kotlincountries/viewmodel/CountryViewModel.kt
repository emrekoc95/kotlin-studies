package com.kocemre.kotlincountries.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kocemre.kotlincountries.model.Country

class CountryViewModel : ViewModel() {

    var countryData = MutableLiveData<Country>()

    fun getDataFromRoom(){
        val country = Country("Turkey","Asia","Ankara","TRY","Turkey","www.ss.com")
        countryData.value = country
    }
}