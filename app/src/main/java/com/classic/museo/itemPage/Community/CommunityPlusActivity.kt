package com.classic.museo

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.classic.museo.databinding.ActivityCommunityPlusBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommunityPlusActivity : AppCompatActivity() {

    lateinit var binding: ActivityCommunityPlusBinding
    private val db = Firebase.firestore
    private var auth : FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityPlusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityPlusBack.setOnClickListener{
            finish()
        }
        binding.communityPlusCancle.setOnClickListener{
            finish()
        }
        binding.communityPlus.setOnClickListener{
            sendToData()
        }

        auth = Firebase.auth
    }
    private fun sendToData(){
        val title = binding.communityPlusTitle.text.toString()
        val text = binding.communityPlusEdittext.text.toString()
        val uid : String? = auth?.uid

        val post = hashMapOf(
            "title" to title,
            "text" to text,
            "date" to java.util.Date().time,
            "uid" to uid
        )

        db.collection("post")
            .add(post)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }
}