package com.sparta.museo.itemPage.announcement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sparta.museo.data.AnnouncementDTO
import com.sparta.museo.databinding.ActivityAnnouncementDetailBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AnnouncementDetailActivity : AppCompatActivity() {
    lateinit var binding : ActivityAnnouncementDetailBinding
    var firestore: FirebaseFirestore? = null
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnnouncementDetailBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        //뒤로가기
        binding.announcementDtBack.setOnClickListener{
            finish()
        }
        firestore = FirebaseFirestore.getInstance()



        //데이터 받기
        val com = intent.getParcelableExtra<AnnouncementDTO>("announcementData")!!

        val title = com.title
        val text = com.text
        val date = com.date

        //텍스트 항목에 맞게 뿌려주기
        binding.announcementDtTitle.text = title
        binding.announcementDtText.text = text
        binding.announcementDtDate.text = date

        //파이어스토어에서 가지고온 텍스트를 줄바꿈
        binding.announcementDtTitle.text = title!!.replace("\\n", "\n")
        binding.announcementDtText.text = text!!.replace("\\n", "\n")


    }
}