package com.classic.museo.itemPage.Community

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.classic.museo.data.Users
import com.classic.museo.databinding.ActivityCommunityPlusBinding
import com.google.android.play.integrity.internal.aa
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

class CommunityPlusActivity : AppCompatActivity() {

    private var gson=GsonBuilder().create()
    private lateinit var user:Users
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

            if(binding.communityPlusTitle.text.isEmpty()){
                Toast.makeText(this,"ㅇㅇ",Toast.LENGTH_SHORT)
            }
            sendToData()
            finish()
        }

        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        userDBLoad(auth?.uid!!)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun userDBLoad(uid:String) {
        val userRef=db.collection("users").document("${uid}")
        userRef.get().addOnSuccessListener { document ->
            if(document!=null){
                val value=gson.toJson(document.data)
                val result=gson.fromJson(value, Users::class.java)!!
                user=result
            }else {
                Log.d(TAG, "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with ", exception)
        }
    }

    private fun sendToData(){
        val title = binding.communityPlusTitle.text.toString()
        val text = binding.communityPlusEdittext.text.toString()
        val museum = binding.communityPlusMuseum.text.toString()
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm ", Locale.KOREA).format(currentDate)
        val uid : String? = auth?.uid

        Log.e("검사용","${user}")

        val post = hashMapOf(
            "title" to title,
            "text" to text,
            "date" to dateFormat,
            "museum" to museum,
            "uid" to uid,
            "NickName" to user.NickName,
            "UserId" to user.UserId
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