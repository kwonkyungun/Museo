package com.classic.museo.ItemPage.MypageInnerActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.classic.museo.databinding.ActivityMypageWrittenBinding

class MypageWritten : AppCompatActivity() {
    lateinit var binding: ActivityMypageWrittenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageWrittenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //더미데이터
        val dummy = mutableListOf<DummyItem>()
        dummy.add(DummyItem("Written 테스트 제목1", "테스트 내용1"))
        dummy.add(DummyItem("Written 테스트 제목2", "테스트 내용2"))
        dummy.add(DummyItem("Written 테스트 제목3", "테스트 내용3"))
        dummy.add(DummyItem("Written 테스트 제목4", "테스트 내용4"))
        binding.MypageInnerView.adapter = WrittenAdapter(this,dummy)

        binding.MypageInnerBack.setOnClickListener {
            finish()
        }
    }
}