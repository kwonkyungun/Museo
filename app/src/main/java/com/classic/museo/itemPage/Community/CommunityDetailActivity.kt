package com.classic.museo.itemPage.Community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.classic.museo.itemPage.MypageInnerActivity.DummyItem
import com.classic.museo.databinding.ActivityCommunityDetailBinding

class CommunityDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommunityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //더미데이터
        val dummy = mutableListOf<DummyItem>()
        dummy.add(DummyItem("테스트 제목1","테스트 내용1"))
        dummy.add(DummyItem("테스트 제목2","테스트 내용2"))
        dummy.add(DummyItem("테스트 제목3","테스트 내용3"))
        dummy.add(DummyItem("테스트 제목4","테스트 내용4"))
        binding.recyclerView3.adapter = CommunityDetailItem(this,dummy)

        binding.communityDetailSave.setOnClickListener {
            Toast.makeText(this,"test",Toast.LENGTH_SHORT).show()
        }

        val title = intent.getStringExtra("title").toString()
        val text = intent.getStringExtra("text").toString()
        val museum = intent.getStringExtra("museum").toString()
        val NickName = intent.getBooleanArrayExtra("NickName").toString()

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
}