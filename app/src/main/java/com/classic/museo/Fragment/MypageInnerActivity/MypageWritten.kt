package com.classic.museo.Fragment.MypageInnerActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.classic.museo.R
import com.classic.museo.databinding.ActivityMypageLikeBinding
import com.classic.museo.databinding.ActivityMypageWrittenBinding

class MypageWritten : AppCompatActivity() {
    lateinit var binding: ActivityMypageWrittenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageWrittenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.MypageLikeBack.setOnClickListener {
            finish()
        }
    }
}