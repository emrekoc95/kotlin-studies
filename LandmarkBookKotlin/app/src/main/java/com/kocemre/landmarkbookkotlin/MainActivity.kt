package com.kocemre.landmarkbookkotlin

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kocemre.landmarkbookkotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var landmarkList : ArrayList<Landmark>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val pisa = Landmark("Pisa","Italy",R.drawable.pisatower)
        val colosseum = Landmark("Colosseum","Italy",R.drawable.colosseum)
        val eiffel = Landmark("Eiffel","France",R.drawable.eiffeltower)
        val londonBridge = Landmark("London Bridge","UK",R.drawable.towerbridge)

        landmarkList = ArrayList<Landmark>()

        landmarkList.add(pisa)
        landmarkList.add(colosseum)
        landmarkList.add(eiffel)
        landmarkList.add(londonBridge)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val landmarkAdapter = LandmarkAdapter(landmarkList)
        binding.recyclerView.adapter = landmarkAdapter


    }
}