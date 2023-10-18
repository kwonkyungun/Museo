package com.classic.museo.Fragment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.classic.museo.R
import com.classic.museo.databinding.ActivityDetailBinding
import com.classic.museo.databinding.ActivitySignupBinding

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뒤로가기 버튼
        binding.dtBack.setOnClickListener {
            finish()
        }
    }
}