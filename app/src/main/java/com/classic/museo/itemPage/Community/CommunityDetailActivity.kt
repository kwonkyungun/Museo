package com.classic.museo.itemPage.Community

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.classic.museo.Login.LoginActivity
import com.classic.museo.MainActivity
import com.classic.museo.R
import com.classic.museo.data.CommunityDTO
import com.classic.museo.data.CommentDTO
import com.classic.museo.data.KakaoUsers
import com.classic.museo.data.Users
import com.classic.museo.databinding.ActivityCommunityDetailBinding
import com.classic.museo.itemPage.MypageInnerActivity.MypageLike
import com.classic.museo.itemPage.MypageInnerActivity.WrittenAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CommunityDetailActivity() : AppCompatActivity() {
    lateinit var binding: ActivityCommunityDetailBinding
    var firestore: FirebaseFirestore? = null
    private var data = mutableMapOf<String, CommentDTO>()
    private lateinit var comm: CommunityDTO
    private var documentDelete: String? = null  //커뮤니티 디테일화면 documentId받기위한 변수
    private lateinit var adapter: CommunityDetailListAdapter
    private var auth: FirebaseAuth? = null
    private var user = Users()
    private var kakaoUser = KakaoUsers()
    val db = Firebase.firestore

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initSetting()

        firestore = FirebaseFirestore.getInstance()
        auth = Firebase.auth
        if (auth!!.uid != null) {
            val uid = auth!!.currentUser!!.uid
            //댓글 달 회원정보 가져오기
            val userRef = db.collection("users").document("${uid}")
            userRef.get()
                .addOnSuccessListener { document ->
                    val gson = GsonBuilder().create()
                    if (document != null) {
                        val value = gson.toJson(document.data)
                        user = gson.fromJson(value, Users::class.java)
                    } else {
                        Log.d(TAG, "no such document")
                    }
                }
        } else {
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e("에러", "사용자 정보 요청 실패")
                } else if (user != null) {
                    kakaoUser.UserId = user.kakaoAccount?.email!!
                    kakaoUser.NickName = user.kakaoAccount?.profile?.nickname!!
                    kakaoUser.UID = user.id.toString()
                }
            }
        }

        binding.communityDetailComment.setOnFocusChangeListener{_,hasFocus ->
            if(hasFocus){
                auth = Firebase.auth
                val currentUser = auth?.currentUser

                UserApiClient.instance.me { user, error ->

                    if (currentUser == null && user == null) {
                        val loginBuilder = AlertDialog.Builder(this)
                        loginBuilder.setTitle("로그인이 필요한 서비스입니다.")
                        loginBuilder.setMessage("로그인 하시겠습니까?")

                        loginBuilder.setPositiveButton("확인") { dialog, _ ->
                            val loginIntent = Intent(this, LoginActivity::class.java)
                            loginIntent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(loginIntent)
                        }
                        loginBuilder.setNegativeButton("취소") { _,_ ->
                            val mainTo = Intent(this, MainActivity::class.java)
                            startActivity(mainTo)
                        }
                        loginBuilder.setCancelable(false)
                        loginBuilder.show()
                    }
                }
            }
        }

        //등록버튼 클릭리스너
        binding.communityDetailSave.setOnClickListener {

            val documentId = intent.getStringExtra("documentId")!!
            comm = intent.getParcelableExtra<CommunityDTO>("communityData")!!

            val plusTime: LocalDateTime? = LocalDateTime.now()
            val formatterDate = DateTimeFormatter.ISO_DATE
            val formattedDate = plusTime?.format(formatterDate)
            val formatterTime = DateTimeFormatter.ISO_LOCAL_TIME
            val formattedTime = plusTime?.format(formatterTime)
            var text = binding.communityDetailComment.text.toString()

            if (text.isEmpty()) {
                Toast.makeText(this, "댓글을 입력해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                val cmtDelivery = hashMapOf(
                    "uid" to if (auth!!.uid != null) {
                        user.UID
                    } else {
                        kakaoUser.UID
                    },
                    "text" to text,
                    "date" to "${formattedDate!!}" + " ${formattedTime!!.substring(0 until 5)}",
                    "id" to if (auth!!.uid != null) {
                        user.UserId
                    } else {
                        kakaoUser.UserId
                    },
                    "nickname" to if (auth!!.uid != null) {
                        user.NickName
                    } else {
                        kakaoUser.NickName
                    }
                )
                //코멘트 DB저장
                db.collection("post")
                    .document(documentId)
                    .collection("comment")
                    .add(cmtDelivery)
                    .addOnSuccessListener { result ->
                        Log.d(
                            "댓글",
                            "DocumentSnapshot added with ID: ${result.id}"
                        )
                    }.addOnFailureListener { error ->
                        Log.w(TAG, "오류", error)
                    }

                data.clear()
                adapter.clearItem()
                //코멘트DB에서 불러오기

                db.collection("post")
                    .document(documentId)
                    .collection("comment")
                    .orderBy("date", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener { result ->
                        val gson = GsonBuilder().create()
                        for (document in result) {
                            val value = gson.toJson(document.data)
                            val v = gson.fromJson(value, CommentDTO::class.java)
                            data.put(document.id, v)
                        }
                        adapter.notifyDataSetChanged()
                    }.addOnFailureListener { error ->
                        Log.w(TAG, "오류", error)
                    }

                Toast.makeText(this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                //댓글 등록완료 하면 초기화
                binding.communityDetailComment.setText("")
            }
        }

        // community recyclerview 아이템 클릭시 보낸 값 받아오기

        comm = intent.getParcelableExtra<CommunityDTO>("communityData")!!
        documentDelete = intent.getStringExtra("documentId")
        Log.e("community", "sj communityID : $documentDelete")

        val title = comm.title
        val content = comm.text
        val museum = comm.museum
        val NickName = comm.NickName
        val date = comm.date

        //수정페이지로 데이터 보내기
        val editIntent = Intent(this, CommunityEditActivity::class.java)
        editIntent.putExtra("title", title)
        editIntent.putExtra("museum", museum)
        editIntent.putExtra("text", content)
        editIntent.putExtra("NickName", NickName)
        editIntent.putExtra("date", date)
        editIntent.putExtra("documentId", documentDelete)



        CoroutineScope(Dispatchers.IO).launch{
            downloadImage() // 이미지 다운로드
        }

        //수정버튼
        binding.btnCommunityDetailDelete.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.community_dialog, null)
            builder.setView(dialogView)

            val alertDialog = builder.create()
            alertDialog.window?.setGravity(Gravity.BOTTOM)
            alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            alertDialog.window?.setBackgroundDrawableResource(R.drawable.community_dialog_shape)

            val btnEdit = dialogView.findViewById<ConstraintLayout>(R.id.dialog_edit)
            val btnDelete = dialogView.findViewById<ConstraintLayout>(R.id.dialog_delete)
            val btnCancel = dialogView.findViewById<ConstraintLayout>(R.id.dialog_cancel)

            //다이얼로그 수정버튼 클릭
            btnEdit.setOnClickListener {
                startActivity(editIntent)
                alertDialog.dismiss()
            }

            //다이얼로그 삭제버튼 클릭
            btnDelete.setOnClickListener {
                val deleteBuilder = AlertDialog.Builder(this)
                deleteBuilder.setTitle("게시글 삭제")
                deleteBuilder.setMessage("정말로 삭제하시겠습니까?")
                deleteBuilder.setIcon(R.drawable.coment)

                deleteBuilder.setPositiveButton("확인") { dialog, _ ->
                    db.collection("post").document(documentDelete!!).delete().addOnSuccessListener {
                        Toast.makeText(this, "게시물이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        Log.d("CommunityDetail", "DocumentSnapshot successfully deleted!")
                    }.addOnFailureListener { e ->
                        Log.w(
                            "CommunityDetail", "Error deleting document", e
                        )
                    }
                    //이미지 삭제
                    val documentID = intent.getStringExtra("documentId")
                    val storageReference = Firebase.storage.reference
                    val desertImage = storageReference.child("postedImage/$documentID.jpg")
                    desertImage.delete().addOnCompleteListener{
                        // File deleted successfully
                    }.addOnFailureListener {
                        // Uh-oh, an error occurred!
                    }
                    finish()
                }
                deleteBuilder.setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
                deleteBuilder.show()
                alertDialog.dismiss()
            }
            //다이얼로그 취소버튼 클릭
            btnCancel.setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
        }

        //로그인한 uid와 작성자 uid가 같으면 수정 삭제 버튼 활성화
        btnEdit()

        // 뒤로가기 버튼
        binding.communityDetailBack.setOnClickListener {
            finish()
        }
    }

    fun btnEdit() {
        val comm = intent.getParcelableExtra<CommunityDTO>("communityData")!!
        auth = Firebase.auth
        val UID = comm.uid
        val currentUser = auth?.currentUser?.uid
        Log.d("communityDetail", "sj $currentUser")

        if (UID == currentUser) {
            binding.btnCommunityDetailDelete.visibility = View.VISIBLE
        } else {
            binding.btnCommunityDetailDelete.visibility = View.INVISIBLE
        }

        UserApiClient.instance.me { user, error ->
            if (user != null) {
                if (user.id.toString() == UID) {
                    binding.btnCommunityDetailDelete.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun setItem() {

        downloadImage() // 이미지 다운로드

        val documentID = intent.getStringExtra("documentId")
        var gson = GsonBuilder().create()
        db.collection("post").document("$documentID").get().addOnSuccessListener { document ->
            val value = gson.toJson(document.data)
            val result = gson.fromJson(value, CommunityDTO::class.java)
            binding.communityDetailTitle.text = result.title
            binding.communityDetailText.text = result.text
            binding.communityDetailMuseum.text = result.museum
            binding.communityDetailName.text = result.NickName
            binding.communityDetailDate.text = result.date
        }

    }

    override fun onStart() {
        data.clear()
        adapter.clearItem()
        super.onStart()
        //디테일액티비티 들어왔을때
        val documentId = intent.getStringExtra("documentId")!!
        db.collection("post")
            .document(documentId)
            .collection("comment")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val gson = GsonBuilder().create()
                for (document in result) {
                    val value = gson.toJson(document.data)
                    val v = gson.fromJson(value, CommentDTO::class.java)
                    data.put(document.id, v)
                }
                adapter.notifyDataSetChanged()
            }.addOnFailureListener { error ->
                Log.w(TAG, "오류", error)
            }
    }

    private fun initSetting() {
        var postId = ""
        binding.recyclerView3.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        postId = intent.getStringExtra("documentId")!!
        adapter = CommunityDetailListAdapter(data, this, postId)
        binding.recyclerView3.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        setItem()
    }

    private fun downloadImage() {
        val docID = intent.getStringExtra("documentId")
        val storageReference = Firebase.storage.reference.child("postedImage/$docID.png")
        val imageView = binding.communityDetailImage

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageView)
            }
            else {
                imageView.visibility = View.GONE
            }
        })
    }
}