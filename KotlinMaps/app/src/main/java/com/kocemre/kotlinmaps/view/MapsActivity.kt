package com.kocemre.kotlinmaps.view

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.room.Room

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.kocemre.kotlinmaps.R
import com.kocemre.kotlinmaps.databinding.ActivityMapsBinding
import com.kocemre.kotlinmaps.model.Place
import com.kocemre.kotlinmaps.roomdb.PlaceDao
import com.kocemre.kotlinmaps.roomdb.PlaceDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var sharedPreferences: SharedPreferences
    private var trackBoolean: Boolean? = null
    var selectedLatitude: Double? = null
    var selectedLongitude: Double? = null
    private lateinit var db: PlaceDatabase
    private lateinit var placeDao: PlaceDao
    val compositeDisposable = CompositeDisposable()
    var placeFromMain : Place? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        db = Room.databaseBuilder(applicationContext,PlaceDatabase::class.java,"Places")
            //.allowMainThreadQueries()
            .build()
        placeDao = db.placeDao()

        binding.saveButton.isEnabled = false


        registerLauncher()

        trackBoolean = false
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapLongClickListener(this)

        val intent = intent
        val info = intent.getStringExtra("info")

        selectedLatitude = 0.0
        selectedLongitude = 0.0

        if(info == "new"){

            binding.saveButton.visibility = View.VISIBLE
            binding.deleteButton.visibility = View.GONE

            locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager



            locationListener = object : LocationListener{
                override fun onLocationChanged(p0: Location) {
                    if(!trackBoolean!!){
                        val userLocation = LatLng(p0.latitude,p0.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,16f))
                        trackBoolean = true

                    }

                }
            }

            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                    Snackbar.make(binding.root,"Permission needed for location",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",
                        View.OnClickListener {
                            //permission needed
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }).show()

                }else {
                    //permission needed
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }else{
                //permission granted
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)

                val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if(lastKnownLocation!=null){
                    val lastKnownLatLng = LatLng(lastKnownLocation.latitude,lastKnownLocation.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLatLng,16f))
                }

                mMap.isMyLocationEnabled = true

            }

        }else{
            mMap.clear()
            mMap.isMyLocationEnabled = true

            binding.saveButton.visibility = View.GONE
            binding.deleteButton.visibility = View.VISIBLE

            placeFromMain = intent.getSerializableExtra("selectedPlace") as? Place

            placeFromMain?.let {
                val latLngFromMain = LatLng(it.latitude,it.longitude)
                mMap.addMarker(MarkerOptions().position(latLngFromMain).title(it.name))
                binding.placeText.setText(it.name)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngFromMain,16f))

            }



        }




    }


    private fun registerLauncher(){
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result->
            if(result){
                if(ContextCompat.checkSelfPermission(this@MapsActivity,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)

                    val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if(lastKnownLocation!=null){
                        val lastKnownLatLng = LatLng(lastKnownLocation.latitude,lastKnownLocation.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLatLng,16f))
                    }

                    mMap.isMyLocationEnabled = true

                }
            }else{
                Toast.makeText(this@MapsActivity,"Please give permission",Toast.LENGTH_LONG).show()
            }
        }


    }

    override fun onMapLongClick(p0: LatLng) {
        binding.saveButton.isEnabled = true
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(p0))
        selectedLatitude = p0.latitude
        selectedLongitude = p0.longitude


    }

    fun save(view: View){

        if(selectedLatitude != null && selectedLongitude != null){
            val place = Place(binding.placeText.text.toString(),selectedLatitude!!,selectedLongitude!!)
            compositeDisposable.add(
                placeDao.insert(place)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponse)
            )
        }

    }

    fun delete(view: View){

        if(selectedLatitude != null && selectedLongitude != null){

            placeFromMain?.let {
                placeDao.delete(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponse)
            }?.let {
                compositeDisposable.add(
                    it

                )
            }


        }



    }

    fun handleResponse(){
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }



    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }


}