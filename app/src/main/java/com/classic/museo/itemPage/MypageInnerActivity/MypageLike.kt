package com.classic.museo.itemPage.MypageInnerActivity

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.classic.museo.data.AnnouncementDTO
import com.classic.museo.data.CommunityDTO
import com.classic.museo.data.LikeDTO
import com.classic.museo.databinding.ActivityMypageLikeBinding
import com.classic.museo.itemPage.announcement.AnnouncementAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

class MypageLike : AppCompatActivity() {

    private lateinit var binding: ActivityMypageLikeBinding
    private lateinit var adapter : LikeAdapter
    private lateinit var gridLayoutManager: StaggeredGridLayoutManager
    private var db = Firebase.firestore
    private val gson = GsonBuilder().create()
    private val IdItems = mutableListOf<String>()
    private val loadItem = mutableListOf<LikeDTO>()
    private var auth: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMypageLikeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance().currentUser?.uid
        Log.e("qq","$auth")

        //파이어스토어 인스턴스 초기화
        db = FirebaseFirestore.getInstance()

        //뒤로가기
        binding.MylikeBack.setOnClickListener {
            finish()
        }


        db.collection("users")
            .document("$auth")
            .collection("myLike")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var gson = GsonBuilder().create()
                    val value = gson.toJson(document.data)
                    val documentId = document.id
                    val result = gson.fromJson(value, LikeDTO::class.java)
                    IdItems.add(documentId)
                    loadItem.add(result)
                }
                if (loadItem.isEmpty()) {
                    Toast.makeText(this, "즐겨찾기가 없습니다.", Toast.LENGTH_SHORT).show()
                }
                binding.favoritesRv.adapter = LikeAdapter(loadItem, this, IdItems)
                binding.favoritesRv.layoutManager = LinearLayoutManager(this)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

}