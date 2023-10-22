package com.classic.museo.itemPage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.classic.museo.itemPage.Community.CommunityPlusActivity
import com.classic.museo.databinding.FragmentCommunityBinding


class CommunityFragment : Fragment() {

    private val binding by lazy { FragmentCommunityBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.communityBtnPlus.setOnClickListener{
            val intent = Intent(context, CommunityPlusActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }



}