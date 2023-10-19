package com.classic.museo.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.classic.museo.Fragment.MypageInnerActivity.MypageLike
import com.classic.museo.Fragment.MypageInnerActivity.MypageLogoutDialog
import com.classic.museo.Fragment.MypageInnerActivity.MypageWritten
import com.classic.museo.MainActivity
import com.classic.museo.databinding.FragmentMypageBinding


class MypageFragment : Fragment() {
    lateinit var binding: FragmentMypageBinding
    var mainActivity: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater)

        //작성글, 좋아요, 공지사항 버튼
        binding.MypageWrittenLayout.setOnClickListener {
            val tosstoWritten = Intent(activity,MypageWritten::class.java)
            startActivity(tosstoWritten)
        }
        binding.MypageLikeLayout.setOnClickListener {
            val tosstoLike = Intent(activity,MypageLike::class.java)
            startActivity(tosstoLike)
        }
        binding.MypageInfoLayout.setOnClickListener {
            //공지사항 페이지는 아직 없으므로 기능은 미구현
            Toast.makeText(activity,"공지사항 버튼클릭!",Toast.LENGTH_SHORT).show()
        }

        //로그아웃
        binding.MypageLogout.setOnClickListener {

            Log.d("버튼테스트","로그아웃버튼")
            val dialog = MypageLogoutDialog()
            dialog.show(requireActivity().supportFragmentManager, "DialogFragment")
        }
        // Inflate the layout for this fragment

        return binding.root
    }


}