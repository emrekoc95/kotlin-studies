package com.kocemre.kotlininstagram.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.kocemre.kotlininstagram.databinding.ActivityUploadBinding
import java.util.*
import kotlin.collections.HashMap

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var selectedUri: Uri? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(binding.root)

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

        registerLauncher()


    }

    fun upload(view: View) {

        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val imageReference = storage.reference.child("images").child(imageName)

        if (selectedUri != null) {

            imageReference.putFile(selectedUri!!).addOnSuccessListener() {
                imageReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()

                    if (auth.currentUser != null) {
                        val postMap = HashMap<String, Any>()
                        postMap.put("downloadUrl", downloadUrl)
                        postMap.put("userEmail", auth.currentUser!!.email.toString())
                        postMap.put("comment", binding.commentText.text.toString())
                        postMap.put("date", Timestamp.now())

                        firestore.collection("Posts").add(postMap).addOnSuccessListener {
                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(this@UploadActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
                        }

                    }
                }
            }.addOnFailureListener() {
                Toast.makeText(this@UploadActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun registerLauncher() {

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        selectedUri = intentFromResult.data
                        selectedUri.let {
                            binding.imageView.setImageURI(selectedUri)
                        }
                    }
                }
            }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                } else {
                    Toast.makeText(this, "Permission needed to select an image!", Toast.LENGTH_LONG)
                        .show()
                }
            }


    }

    fun selectImage(view: View) {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Snackbar.make(
                    view,
                    "Give permission to select an image!",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("Permission needed!") {
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()
            } else {
                //request permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            //permission granted, start activity for result
            val intentToGallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }


    }

}