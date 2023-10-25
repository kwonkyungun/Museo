package com.classic.museo.itemPage.Community

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
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

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
        val museum = binding.communityPlusMuseum.text.toString()
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm ", Locale.KOREA).format(currentDate)
        val uid : String? = auth?.uid

        val post = hashMapOf(
            "title" to title,
            "text" to text,
            "date" to dateFormat,
            "museum" to museum,
            "uid" to uid,
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