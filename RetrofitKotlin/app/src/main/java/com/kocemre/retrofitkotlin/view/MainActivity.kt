package com.kocemre.retrofitkotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kocemre.retrofitkotlin.R
import com.kocemre.retrofitkotlin.adapter.CryptoAdapter
import com.kocemre.retrofitkotlin.databinding.ActivityMainBinding
import com.kocemre.retrofitkotlin.model.CryptoModel
import com.kocemre.retrofitkotlin.service.CryptoAPI
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.*
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private final val BASE_URL: String = "https://raw.githubusercontent.com/"
    private var cryptoModels: ArrayList<CryptoModel>? = null
    private lateinit var cryptoAdapter: CryptoAdapter
    private lateinit var binding : ActivityMainBinding
    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        compositeDisposable = CompositeDisposable()

        loadData()

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager



    }

    private fun loadData(){

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build().create(CryptoAPI::class.java)


        compositeDisposable?.add(retrofit.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse))

    }

    private fun handleResponse(cryptoList: List<CryptoModel>){
        cryptoModels = ArrayList(cryptoList)

        cryptoModels?.let{
            cryptoAdapter = CryptoAdapter(it)
            binding.recyclerView.adapter = cryptoAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}