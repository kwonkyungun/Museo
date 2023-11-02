package com.classic.museo.itemPage.Community

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.classic.museo.data.KakaoUsers
import com.classic.museo.data.Users
import com.classic.museo.databinding.ActivityCommunityPlusBinding
import com.google.android.play.integrity.internal.aa
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class CommunityPlusActivity : AppCompatActivity() {

    private var gson=GsonBuilder().create()
    private var kakaoUser=KakaoUsers()
    private var user=Users()
    lateinit var binding: ActivityCommunityPlusBinding
    val db = Firebase.firestore
    var auth : FirebaseAuth? = null

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
        binding.communityPlus.setOnClickListener{

            if(binding.communityPlusTitle.text.isEmpty()||binding.communityPlusEdittext.text.isEmpty()||
                    binding.communityPlusMuseum.text.isEmpty()){
                Toast.makeText(this,"빈칸을 채워주세요.",Toast.LENGTH_SHORT).show()
            }else {
                sendToData()
                finish()
            }
        }

        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        //if->파이어베이스 사용자정보, else->카카오 사용자정보
        if(auth!!.uid!=null){
            userDBLoad(auth?.uid!!)
        }else {
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e("에러", "사용자 정보 요청 실패", error)
                }
                else if (user != null) {
                    kakaoUser.UID=user.id.toString()
                    kakaoUser.Image=user.kakaoAccount?.profile?.profileImageUrl!!
                    kakaoUser.UserId=user.kakaoAccount?.email!!
                    kakaoUser.NickName=user.kakaoAccount?.profile?.nickname!!
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun userDBLoad(uid:String) {
        val userRef=db.collection("users").document("${uid}")
        userRef.get().addOnSuccessListener { document ->
            if(document!=null){
                val value=gson.toJson(document.data)
                val result=gson.fromJson(value, Users::class.java)!!
                user=result
            }else {
                Log.d(TAG, "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with ", exception)
        }
    }

    fun sendToData(){
        val title = binding.communityPlusTitle.text.toString()
        val text = binding.communityPlusEdittext.text.toString()
        val museum = binding.communityPlusMuseum.text.toString()
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm ", Locale.KOREA).format(currentDate)
        val uid : String? = auth?.uid

        val post = hashMapOf(
            "title" to title,
            "text" to text,
            "date" to dateFormat,
            "museum" to museum,
            "uid" to if (uid!=null){uid!!}else{kakaoUser.UID},
            "NickName" to if (uid!=null){user.NickName}else{kakaoUser.NickName},
            "UserId" to if (uid!=null){user.UserId}else{kakaoUser.UserId},
        )

        db.collection("post")
            .add(post)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }
}