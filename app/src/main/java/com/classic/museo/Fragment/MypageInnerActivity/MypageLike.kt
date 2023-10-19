package com.classic.museo.Fragment.MypageInnerActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.classic.museo.databinding.ActivityMypageLikeBinding

class MypageLike : AppCompatActivity() {
    lateinit var binding: ActivityMypageLikeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageLikeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //더미데이터
        val dummy = mutableListOf<DummyItem>()
        dummy.add(DummyItem("Like 테스트 제목1","테스트 내용1"))
        dummy.add(DummyItem("Like 테스트 제목2","테스트 내용2"))
        dummy.add(DummyItem("Like 테스트 제목3","테스트 내용3"))
        dummy.add(DummyItem("Like 테스트 제목4","테스트 내용4"))
        binding.MypageInnerView.adapter = LikeAdapter(this,dummy)

        binding.MypageInnerBack.setOnClickListener {
            finish()
        }
    }
}