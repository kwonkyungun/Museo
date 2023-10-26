package com.classic.museo.itemPage.Community

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.classic.museo.itemPage.MypageInnerActivity.DummyItem
import com.classic.museo.databinding.ActivityCommunityDetailBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CommunityDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommunityDetailBinding
    var firestore: FirebaseFirestore? = null
    val itemList = arrayListOf<CommunityDetailDataClass>()
    val adapter = CommunityDetailListAdapter(itemList)
    val db = Firebase.firestore
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.recyclerView3.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView3.adapter = adapter

        firestore = FirebaseFirestore.getInstance()

        //등록버튼 클릭리스너
        binding.communityDetailSave.setOnClickListener {

            val plusTime: LocalDateTime? = LocalDateTime.now()
            val formatterDate = DateTimeFormatter.ISO_DATE
            val formattedDate = plusTime?.format(formatterDate)
            val formatterTime = DateTimeFormatter.ISO_LOCAL_TIME
            val formattedTime = plusTime?.format(formatterTime)
            val text = binding.communityDetailComment.text.toString()

            val test = hashMapOf(
                "text" to text,
                "date" to formattedDate
            )

            //데이터 저장하기
            db.collection("Comment")
                .add(test)
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }

            //데이터 가져오기
            db.collection("Comment")
                .get()
                .addOnSuccessListener { result ->
                    //중복출력 방지용 리사이클러뷰 초기화
                    itemList.clear()
                    for (document in result) {
                        Log.d(ContentValues.TAG, "receive ${document.id} => ${document.data}")
                        val item = CommunityDetailDataClass(document["text"] as String, document["date"] as String)
                        itemList.add(item)
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
                }
        }

        val title = intent.getStringExtra("title").toString()
        val text = intent.getStringExtra("text").toString()
        val museum = intent.getStringExtra("museum").toString()
        val NickName = intent.getStringExtra("NickName").toString()

        binding.communityDetailTitle.text = title
        binding.communityDetailText.text = text
        binding.communityDetailMuseum.text = museum
        binding.communityDetailName.text = NickName
        Log.d("communityDetail","sj 박물관 이름 : $museum")
        Log.d("communityDetail", "sj 닉네임 : $NickName")

        binding.communityDetailBack.setOnClickListener{
            finish()
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        db.collection("Comment")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    val item = CommunityDetailDataClass(document["text"] as String, document["date"] as String)
                    itemList.add(item)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }
}