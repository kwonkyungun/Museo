package com.classic.museo.Fragment.MypageInnerActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.classic.museo.R
import com.classic.museo.databinding.ActivityMypageLikeBinding
import com.classic.museo.databinding.FragmentMypageBinding

class MypageLike : AppCompatActivity() {
    lateinit var binding: ActivityMypageLikeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageLikeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        binding.MypageLikeBack.setOnClickListener {
//            finish()
//        }
    }
}