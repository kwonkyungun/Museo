package com.classic.museo.ItemPage.Community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.classic.museo.databinding.ActivityCommunityPlusBinding

class CommunityPlusActivity : AppCompatActivity() {

    lateinit var binding: ActivityCommunityPlusBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityPlusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.communityPlusBack.setOnClickListener{
            finish()
        }
        binding.communityPlusCancle.setOnClickListener{
            finish()
        }
    }
}