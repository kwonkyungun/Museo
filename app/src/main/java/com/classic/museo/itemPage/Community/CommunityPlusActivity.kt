package com.classic.museo.itemPage.Community

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.classic.museo.data.KakaoUsers
import com.classic.museo.data.Users
import com.classic.museo.databinding.ActivityCommunityPlusBinding
import com.google.android.play.integrity.internal.aa
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.GsonBuilder
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

class CommunityPlusActivity : AppCompatActivity() {

    private var gson=GsonBuilder().create()
    private var kakaoUser=KakaoUsers()
    private var user=Users()
    lateinit var binding: ActivityCommunityPlusBinding
    private val db = Firebase.firestore
    private var auth : FirebaseAuth? = null

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
        //게시버튼
        binding.communityPlus.setOnClickListener{

            if(binding.communityPlusTitle.text.isEmpty()||binding.communityPlusEdittext.text.isEmpty()||
                    binding.communityPlusMuseum.text.isEmpty()){
                Toast.makeText(this,"빈칸을 채워주세요.",Toast.LENGTH_SHORT).show()
            }else {
                sendToData()
                finish()
            }
        }

        binding.communityPlusImage.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery,100)
        }

        auth = Firebase.auth
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100 ){
            binding.communityPlusImage.setImageURI(data?.data)
        }
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

    private fun sendToData(){
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

        val docID =  db.collection("post").document().id

        db.collection("post").document(docID)
            .set(post)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${docID}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }

        sendToImage(docID) // storage 이미지 업로드
    }

    private fun sendToImage(docID:String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("postedImage/$docID.jpg")

        val imageView = binding.communityPlusImage
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }
}