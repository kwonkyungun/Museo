package com.classic.museo.itemPage

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.classic.museo.R
import com.classic.museo.itemPage.MypageInnerActivity.MypageLike
import com.classic.museo.itemPage.MypageInnerActivity.MypageLogoutDialog
import com.classic.museo.itemPage.MypageInnerActivity.MypageWritten
import com.classic.museo.data.Users
import com.classic.museo.databinding.FragmentMypageBinding
import com.classic.museo.itemPage.Community.CommunityDetailActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient


class MypageFragment : Fragment() {

    lateinit var binding: FragmentMypageBinding
    private var db = Firebase.firestore
    private val UserList = mutableListOf<Users>()
    private var uid : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater)

        //내 게시물
        MyPost()

        //좋아요
        MyLike()

        //공지사항
        Info()

        //로그아웃
        Logout()

        //firestore에서 로그인한 회원 닉네임 보여주기
        NickName()

//        //커뮤니티 디테일 액티비티로 넘어가는 임시 코드
//        binding.tempbtn.setOnClickListener {
//            val TossToCommunityDetail = Intent(activity,CommunityDetailActivity::class.java)
//            startActivity(TossToCommunityDetail)
//        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            UserApiClient.instance.me { user, error ->
                if(error!=null){
                    Log.e("에러","사용자 정보 요청 실패",error)
                }else if(user!=null) {
                    Glide.with(this)
                        .load(user.kakaoAccount?.profile?.profileImageUrl)
                        .error(R.drawable.mypage_imagesample)
                        .into(binding.imageView2)
                }
            }
    }

    fun NickName() {
        uid = FirebaseAuth.getInstance().currentUser?.uid

        //사용자 누구인지 분별
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var UID = document.get("UID").toString()
                    if (uid == UID) {
                        var Nickname= document.get("NickName").toString()
                        binding.MyNickName.text = Nickname
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
        //카카오 로그인 시 닉네임 가져오기
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("에러", "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                binding.MyNickName.text=user.kakaoAccount?.profile?.nickname!!
            }
        }

    }
    //로그아웃
    fun Logout() {

        binding.MypageLogout.setOnClickListener {

            Log.d("버튼테스트","로그아웃버튼")
            val dialog = MypageLogoutDialog()
            dialog.show(requireActivity().supportFragmentManager, "DialogFragment")
        }
    }
    //내 게시물
    fun MyPost(){
        binding.MypageWrittenLayout.setOnClickListener {
            val tosstoWritten = Intent(activity,MypageWritten::class.java)
            startActivity(tosstoWritten)
        }
    }
    //좋아요
    fun MyLike(){
        binding.MypageLikeLayout.setOnClickListener {
            val tosstoLike = Intent(activity,MypageLike::class.java)
            startActivity(tosstoLike)
        }
    }
    //공지사항
    fun Info(){
        binding.MypageInfoLayout.setOnClickListener {
            //공지사항 페이지는 아직 없으므로 기능은 미구현
            Toast.makeText(activity,"공지사항 버튼클릭!",Toast.LENGTH_SHORT).show()
        }
    }
}
