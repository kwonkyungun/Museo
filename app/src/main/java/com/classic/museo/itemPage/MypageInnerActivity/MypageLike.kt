package com.classic.museo.itemPage.MypageInnerActivity

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.classic.museo.data.LikeDTO
import com.classic.museo.databinding.ActivityMypageLikeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.classic.museo.data.Recording
import com.kakao.sdk.user.UserApiClient

class MypageLike : AppCompatActivity() {

    private lateinit var binding: ActivityMypageLikeBinding
    private lateinit var adapter: LikeAdapter
    private lateinit var gridLayoutManager: StaggeredGridLayoutManager
    private var db = Firebase.firestore
    private val gson = GsonBuilder().create()
    private val IdItems = mutableListOf<String>()
    private val loadItem = mutableListOf<LikeDTO>()
    private val museumIdItems = mutableListOf<String>()
    private val museoItems = mutableListOf<Recording>()
    private var auth: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMypageLikeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance().uid
        Log.e("qq", "$auth")

        //파이어스토어 인스턴스 초기화
        db = FirebaseFirestore.getInstance()

        //뒤로가기
        binding.MylikeBack.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()

        museoItems.clear()
        loadItem.clear()
        UserApiClient.instance.me { user, error ->

            db.collection("users")
                .document(
                    if (auth != null) {
                        "${auth}"
                    } else {
                        user!!.id.toString()
                    }
                )
                .collection("myLike")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        var gson = GsonBuilder().create()
                        val value = gson.toJson(document.data)
                        val documentId = document.id
                        val result = gson.fromJson(value, LikeDTO::class.java)

                        val museoRef = db.collection("museoInfo").document(result.museumId)

                        museoRef.get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    val gson = GsonBuilder().create()
                                    val value = gson.toJson(document.data)
                                    val result2 = gson.fromJson(value, Recording::class.java)
                                    result2.museoId = document.id
                                    museoItems.add(result2)

                                    loadItem.add(result)
                                }
                                Log.e("테스트원","${loadItem}")
                                if (loadItem.isEmpty()) {
                                    Toast.makeText(this, "즐겨찾기가 없습니다.", Toast.LENGTH_SHORT).show()
                                }
                                val adapterTo=LikeAdapter(loadItem, this, museoItems)
                                binding.favoritesRv.adapter =adapterTo
                                binding.favoritesRv.layoutManager = LinearLayoutManager(this)
                            }
                    }

                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                }

        }

    }

}