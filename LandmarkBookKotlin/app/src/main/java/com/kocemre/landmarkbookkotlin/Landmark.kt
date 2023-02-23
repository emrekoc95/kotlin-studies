package com.kocemre.landmarkbookkotlin

import java.io.Serializable

class Landmark(name : String,country : String,image : Int) :Serializable {


    val image = image
    val name = name
    val country = country
}