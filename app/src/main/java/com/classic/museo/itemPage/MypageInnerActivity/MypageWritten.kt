package com.classic.museo.itemPage.MypageInnerActivity

import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.classic.museo.data.CommunityDTO
import com.classic.museo.databinding.ActivityMypageWrittenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.kakao.sdk.user.UserApiClient

class MypageWritten : AppCompatActivity() {
    private lateinit var binding: ActivityMypageWrittenBinding
    private var myPost = mutableListOf<CommunityDTO>()
    private var myPostId = mutableListOf<String>()
    private var db = Firebase.firestore
    private var auth: String? = null
    private var kUid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageWrittenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

    override fun onResume() {
        super.onResume()

        auth = FirebaseAuth.getInstance().currentUser?.uid

        myPost.clear()
        myPostId.clear()
        //카카오 로그인 유저 게시물
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("에러", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                kUid = user.id.toString()

                db.collection("post")
                    .whereEqualTo(
                        "uid", "${kUid}"
                    )
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            var gson = GsonBuilder().create()
                            val value = gson.toJson(document.data)
                            val documentId = document.id
                            val result = gson.fromJson(value, CommunityDTO::class.java)
                            myPostId.add(documentId)
                            myPost.add(result)
                        }
                        if (myPost.isEmpty()) {
                            Toast.makeText(this, "게시물이 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                        binding.myPageWritten.adapter = WrittenAdapter(myPost, this, myPostId)
                        binding.myPageWritten.layoutManager = LinearLayoutManager(this)
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents: ", exception)
                    }
            }
        }

        //일반 로그인 유저 게시물
        db.collection("post")
            .whereEqualTo(
                "uid", "${auth}"
            )
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var gson = GsonBuilder().create()
                    val value = gson.toJson(document.data)
                    val documentId = document.id
                    val result = gson.fromJson(value, CommunityDTO::class.java)
                    myPostId.add(documentId)
                    myPost.add(result)
                }
                if (myPost.isEmpty()) {
                    Toast.makeText(this, "게시물이 없습니다.", Toast.LENGTH_SHORT).show()
                }
                binding.myPageWritten.adapter = WrittenAdapter(myPost, this, myPostId)
                binding.myPageWritten.layoutManager = LinearLayoutManager(this)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        binding.myPageBack.setOnClickListener {
            finish()
        }
    }
}