package com.kocemre.kotlinsqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try{

            val myDatabase = this.openOrCreateDatabase("Musician", MODE_PRIVATE, null)
            //myDatabase.execSQL("CREATE TABLE IF NOT EXISTS musician (id INTEGER PRIMARY KEY, name VARCHAR, age INTEGER)")
            //myDatabase.execSQL("INSERT INTO musician (name,age) VALUES ('James',50)")
            //myDatabase.execSQL("INSERT INTO musician (name, age) VALUES ('Emre',27)")
            //myDatabase.execSQL("INSERT INTO musician (name,age) VALUES ('Kirk',55)")

            myDatabase.execSQL("UPDATE musician SET name = 'Suulo' WHERE name LIKE '%mr%'")

            myDatabase.execSQL("DELETE FROM musician WHERE id = 1")

            myDatabase.execSQL("INSERT INTO musician (name,age) VALUES ('asf',54)")

            val cursor = myDatabase.rawQuery("SELECT * FROM musician",null)
            val nameIx = cursor.getColumnIndex("name")
            val ageIx = cursor.getColumnIndex("age")
            val idIx = cursor.getColumnIndex("id")

            while(cursor.moveToNext()){
                println("Name: ${cursor.getString(nameIx)} ")
                println("Age: ${cursor.getInt(ageIx)}")
                println("Id: ${cursor.getInt(idIx)}")
            }

            myDatabase.close()

        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}