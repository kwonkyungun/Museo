package com.classic.museo.ItemPage.Community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.classic.museo.ItemPage.MypageInnerActivity.DummyItem
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
    }
}