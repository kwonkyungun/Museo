package com.classic.museo.itemPage.MypageInnerActivity

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.classic.museo.Login.LoginActivity
import com.classic.museo.databinding.ActivityMypageLogoutDialogBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient

class MypageLogoutDialog : DialogFragment() {
    private var _binding: ActivityMypageLogoutDialogBinding? = null
    private val binding get() = _binding!!
    private var auth : FirebaseAuth?=null

    @SuppressLint("ShowToast")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ActivityMypageLogoutDialogBinding.inflate(inflater, container, false)
        val view = binding.root

        auth= Firebase.auth

        //취소
        binding.MypageLogoutCancel.setOnClickListener {
            dismiss()
        }
        //확인
        binding.MypageLogoutConfirm.setOnClickListener {
            Log.e("유아이디","${auth!!.uid}")
            if(auth!!.uid!=null){
                //파이어베이스 로그아웃버튼
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(activity,"회원 로그아웃 성공!",Toast.LENGTH_SHORT).show()
                val backtomain = Intent(activity, LoginActivity::class.java)
                backtomain.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(backtomain)
            }else {
                // 로그아웃
                UserApiClient.instance.logout { error ->
                    if (error != null) {
                        Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                    }
                    else {
                        Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                        val backtomain = Intent(activity, LoginActivity::class.java)
                        backtomain.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(backtomain)
                        Toast.makeText(context,"회원(카카오)로그아웃 성공!",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}