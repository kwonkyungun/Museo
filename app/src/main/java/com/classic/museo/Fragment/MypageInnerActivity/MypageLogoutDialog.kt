package com.classic.museo.Fragment.MypageInnerActivity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.classic.museo.LoginActivity
import com.classic.museo.R
import com.classic.museo.databinding.ActivityMypageLogoutDialogBinding
import com.google.firebase.auth.FirebaseAuth

class MypageLogoutDialog : DialogFragment() {
    private var _binding: ActivityMypageLogoutDialogBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ShowToast")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ActivityMypageLogoutDialogBinding.inflate(inflater, container, false)
        val view = binding.root

        //취소
        binding.MypageLogoutCancel.setOnClickListener {
            dismiss()
        }
        //확인
        binding.MypageLogoutConfirm.setOnClickListener {
            //파이어베이스 로그아웃버튼
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(activity,"로그아웃 성공!",Toast.LENGTH_SHORT).show()
            val backtomain = Intent(activity,LoginActivity::class.java)
            startActivity(backtomain)
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}