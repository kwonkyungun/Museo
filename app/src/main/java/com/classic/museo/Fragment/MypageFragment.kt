package com.classic.museo.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.classic.museo.MainActivity
import com.classic.museo.R
import com.classic.museo.databinding.ActivityMainBinding
import com.classic.museo.databinding.FragmentMypageBinding


class MypageFragment : Fragment() {
    lateinit var binding: FragmentMypageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater)
        //로그아웃
        binding.MypageLogoutButton.setOnClickListener {
            Log.d("버튼테스트","로그아웃버튼")
        }
        // Inflate the layout for this fragment
        return binding.root
    }
}