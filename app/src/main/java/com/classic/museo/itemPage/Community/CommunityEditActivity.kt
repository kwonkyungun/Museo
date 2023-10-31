package com.classic.museo.itemPage.Community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.classic.museo.R
import com.classic.museo.databinding.ActivityCommunityEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommunityEditActivity : AppCompatActivity() {
    lateinit var binding : ActivityCommunityEditBinding
    lateinit var adapter : CommunityAdapter
    var firestore : FirebaseFirestore? = null
    private var auth : FirebaseAuth? = null
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //커뮤니티 디테일에서 데이터 받아오기
        val title = intent.getStringExtra("title")
        val museum = intent.getStringExtra("museum")
        val text = intent.getStringExtra("text")
        val NickName = intent.getStringExtra("NickName")
        val date = intent.getStringExtra("date")

        binding.communityTitleEdit.setText("$title")
        binding.communityMuseumEdit.setText("$museum")
        binding.communityTextEdit.setText("$text")
        binding.communityEditName.text = NickName
        binding.communityEditDate.text = date

        //수정버튼
        binding.communityEdit.setOnClickListener{

            editContent()

            val intent = Intent(this, CommunityDetailActivity::class.java)
            startActivity(intent)
        }

        //뒤로가기버튼
        binding.communityEditBack.setOnClickListener{
            finish()
        }

        //취소버튼
        binding.communityEditCancle.setOnClickListener{
            finish()
        }

    }
    fun editContent(){
        val documentId = intent.getStringExtra("documentId")
        Log.d("communityEdit","sj documentID $documentId")

        db.collection("post").document("$documentId")
            .update("title", "수정")
            .addOnSuccessListener { Log.d("CommunitEdit", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w("CommunitEdit", "Error updating document", e) }

        db.collection("post").document("$documentId")
            .update("text", "${binding.communityTextEdit.text}")
            .addOnSuccessListener { Log.d("CommunitEdit", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w("CommunityEdit", "Error updating document", e) }

        db.collection("post").document("$documentId")
            .update("museum", "${binding.communityMuseumEdit.text}")
            .addOnSuccessListener { Log.d("CommunitEdit", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w("CommunitEdit", "Error updating document", e) }
    }
}