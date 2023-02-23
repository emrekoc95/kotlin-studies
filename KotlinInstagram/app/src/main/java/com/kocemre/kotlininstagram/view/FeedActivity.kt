package com.kocemre.kotlininstagram.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayoutStates
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kocemre.kotlininstagram.R
import com.kocemre.kotlininstagram.adapter.FeedRecyclerAdapter
import com.kocemre.kotlininstagram.databinding.ActivityFeedBinding
import com.kocemre.kotlininstagram.model.Post

class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var postArrayList : ArrayList<Post>
    private lateinit var feedRecyclerAdapter: FeedRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(binding.root)

        auth = Firebase.auth
        firestore = Firebase.firestore

        postArrayList = ArrayList<Post>()

        getData()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        feedRecyclerAdapter = FeedRecyclerAdapter(postArrayList)
        binding.recyclerView.adapter = feedRecyclerAdapter


    }

    private fun getData(){

        firestore.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if(error !=null){
                Toast.makeText(this, error.localizedMessage,Toast.LENGTH_LONG).show()
            }else if(value!=null){
                if(!value.isEmpty){

                    val documents = value.documents

                    postArrayList.clear()
                    for(document in documents){
                        val userEmail = document.get("userEmail") as String
                        val comment = document.get("comment") as String
                        val downloadUrl = document.get("downloadUrl") as String

                        println(comment)

                        val post = Post(userEmail,comment,downloadUrl)
                        postArrayList.add(post)


                    }

                    feedRecyclerAdapter.notifyDataSetChanged()

                }

            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.insta_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.add_post){
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }else if(item.itemId == R.id.sign_out){
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}