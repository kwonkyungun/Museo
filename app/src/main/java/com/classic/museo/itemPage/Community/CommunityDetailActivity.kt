package com.classic.museo.itemPage.Community

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.classic.museo.R
import com.classic.museo.data.CommunityDTO
import com.classic.museo.data.Record
import com.classic.museo.data.Users
import com.classic.museo.itemPage.MypageInnerActivity.DummyItem
import com.classic.museo.databinding.ActivityCommunityDetailBinding
import com.classic.museo.itemPage.MypageInnerActivity.WrittenAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.kakao.sdk.user.UserApiClient
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CommunityDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommunityDetailBinding
    var firestore: FirebaseFirestore? = null
    private val itemList = arrayListOf<CommunityDetailDataClass>()
    private lateinit var comm: CommunityDTO
    private var documentDelete: String? = null
    val adapter = CommunityDetailListAdapter(itemList)
    private var auth: FirebaseAuth? = null
    private lateinit var user : Users
    val db = Firebase.firestore

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.recyclerView3.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView3.adapter = adapter

        firestore = FirebaseFirestore.getInstance()


        auth=Firebase.auth
        val uid=auth!!.currentUser!!.uid
        //댓글 단 회원정보 가져오기
        val userRef=db.collection("users").document("${uid}")
        userRef.get()
            .addOnSuccessListener { document ->
                val gson=GsonBuilder().create()
                if(document!=null){
                    val value=gson.toJson(document.data)
                    user=gson.fromJson(value,Users::class.java)
                }else {
                    Log.d(TAG,"no such document")
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
            val text = binding.communityDetailComment.text.toString()

            val cmtDelivery = hashMapOf(
                "text" to text,
                "date" to "${formattedDate!!}"+" ${formattedTime!!.substring(0 until 5)}",
                "id" to user.UserId,
                "nickname" to user.NickName
            )

            db.collection("post")
                .document(documentId)
                .collection("comment")
                .add(cmtDelivery)
                .addOnSuccessListener { result->
                    Log.d(
                        "댓글",
                        "DocumentSnapshot added with ID: ${result.id}")
                }.addOnFailureListener {

                }

            db.collection("post")
                .document(documentId)
                .collection("comment")
                .get()
                .addOnSuccessListener { result ->
                    for(document in result) {

                    }
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

        //수정버튼
        binding.btnCommunityDetailDelete.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.community_dialog, null)
            builder.setView(dialogView)

            val alertDialog = builder.create()
            alertDialog.window?.setGravity(Gravity.BOTTOM)
            alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            alertDialog.window?.setBackgroundDrawableResource(R.drawable.community_dialog_shape)

            val btnEdit = dialogView.findViewById<Button>(R.id.dialog_edit)
            val btnDelete = dialogView.findViewById<Button>(R.id.dialog_delete)
            val btnCancel = dialogView.findViewById<Button>(R.id.dialog_cancel)

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
        val documentID = intent.getStringExtra("documentId")
        var gson = GsonBuilder().create()
        db.collection("post").document("$documentID").get().addOnSuccessListener { document ->
            val value = gson.toJson(document.data)
            val result = gson.fromJson(value, CommunityDTO::class.java)
            val documentId = document.id
            binding.communityDetailTitle.text = result.title
            binding.communityDetailText.text = result.text
            binding.communityDetailMuseum.text = result.museum
            binding.communityDetailName.text = result.NickName
            binding.communityDetailDate.text = result.date
        }

    }

    override fun onResume() {
        super.onResume()
        setItem()
    }
}