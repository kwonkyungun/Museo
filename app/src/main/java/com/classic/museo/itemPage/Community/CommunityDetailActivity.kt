package com.classic.museo.itemPage.Community

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.classic.museo.R
import com.classic.museo.data.CommunityDTO
import com.classic.museo.databinding.ActivityCommunityDetailBinding
import com.classic.museo.itemPage.MypageInnerActivity.WrittenAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.kakao.sdk.user.UserApiClient
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CommunityDetailActivity : AppCompatActivity(),ClickListener {
    lateinit var binding: ActivityCommunityDetailBinding
    var firestore: FirebaseFirestore? = null
    val itemList = arrayListOf<CommunityDetailDataClass>()
    private lateinit var comm: CommunityDTO
    private var documentDelete: String? = null
    val adapter = CommunityDetailListAdapter(itemList, this)
    private var auth: FirebaseAuth? = null
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
        val documentID = intent.getStringExtra("documentId")
//        val documentIDCut = documentID?.substring(1 until documentDelete?.lastIndex!!)

        //등록버튼 클릭리스너
        binding.communityDetailSave.setOnClickListener {

            val plusTime: LocalDateTime? = LocalDateTime.now()
            val formatterDate = DateTimeFormatter.ISO_DATE
            val formattedDate = plusTime?.format(formatterDate)
            val formatterTime = DateTimeFormatter.ISO_LOCAL_TIME
            val formattedTime = plusTime?.format(formatterTime)
            val text = binding.communityDetailComment.text.toString()
            val uid: String? = auth?.uid

            val test = hashMapOf(
                "text" to text,
                "date" to formattedDate,
                "time" to formattedTime
            )

            //데이터 저장하기
            db.collection("post").document(documentID!!).collection("comment")
                .add(test)
                .addOnSuccessListener { documentReference ->
                    Log.d(
                        "CommunityDetail댓글",
                        "DocumentSnapshot added with ID: ${documentReference.id}"
                    )
                    val tester = documentReference.id
                }
                .addOnFailureListener { e ->
                    Log.w("CommunityDetail댓글", "Error adding document", e)
                }

            //데이터 가져오기
            itemList.clear()
            db.collection("post").document(documentID!!).collection("comment")
                .get()
                .addOnSuccessListener { result ->
                    //중복출력 방지용 리사이클러뷰 초기화
                    for (document in result) {
                        Log.d("CommunityDetail댓글", "receive ${document.id} => ${document.data}")
                        val item = CommunityDetailDataClass(
                            document["text"] as String,
                            document["date"] as String
                        )
                        itemList.add(item)
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.w("CommunityDetail댓글", "Error getting documents.", exception)
                }
        }
        //액티비티 실행시 댓글DB불러오기
        db.collection("post").document(documentID!!).collection("comment")
            .get()
            .addOnSuccessListener { result ->
                //중복출력 방지용 리사이클러뷰 초기화
                itemList.clear()
                for (document in result) {
                    Log.d("CommunityDetail댓글", "receive ${document.id} => ${document.data}")
                    val item = CommunityDetailDataClass(
                        document["text"] as String,
                        document["date"] as String
                    )
                    itemList.add(item)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("CommunityDetail댓글", "Error getting documents.", exception)
            }
        itemList.clear()
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
            if (user!=null) {
                if(user.id.toString()==UID){
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

    override fun onDestroy() {
        super.onDestroy()
        itemList.clear()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    override fun clickDropDown() {
        val documentDelete = intent.getStringExtra("documentId")

        //테스트구역 시작
//        //1차
//        db.collection("post").document(documentDelete!!).collection("comment").document(auth?.uid.toString())
//        .get()
//        .addOnCompleteListener {task ->
//            var commentuid = task.result?.toObject(CommunityDetailDataClass::class.java)
//            Log.d("Test", auth?.uid.toString())
//        }
//        //2차
//        db.collection("post").document(documentDelete!!).collection("comment")
//            .get()
//            .addOnSuccessListener {result->
//            val documentuid : MutableList<DocumentSnapshot> = result.documents
//                for(document in documentuid) {
//                    Log.d("테스트 로그", document.id)
//                }
//                Log.d("", documentuid.toString())
//            }
//        //3차
//        var documentID = mutableListOf<String>()
//        fun onClick(v: View?) {
//            val position = bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
//            intent.apply {
//                putExtra("documentId",documentID[position])
//            }
//        }
//        //4차
//        var gson= GsonBuilder().create()
//        db.collection("post").document("$documentDelete").collection("comment").document()
//            .get()
//            .addOnSuccessListener { document ->
//                val value = gson.toJson(document.data)
//                val result = gson.fromJson(value, CommunityDetailDataClass::class.java)
//                val documentId = document.id
//                Log.d("테스트 구역", result.toString())
//            }
//        //5차
//        val test = db.collection("post").document("$documentDelete").collection("comment").document()
//        test.get()
//            .addOnSuccessListener { document ->
//                Log.d("ID로 테스트", "DocumentSnapshot data: ${document.id}")
//                val tester = mutableListOf<String>()
//                tester.forEach { _ ->
//                    Log.d("data로 테스트", "DocumentSnapshot data: ${document.data}")
//                }
//            }
//        //6차
//        val plusTime: LocalDateTime? = LocalDateTime.now()
//        val formatterTime = DateTimeFormatter.ISO_LOCAL_TIME
//        val formattedTime = plusTime?.format(formatterTime)
//        db.collection("post").document(documentDelete!!).collection("comment")
//            .whereEqualTo("time", formattedTime)
//            .get()
//            .addOnSuccessListener {
//                Log.d("6차 테스트", "")
//            }
//            .addOnFailureListener { exception ->
//                Log.w("CommunityDetail댓글", "Error getting documents.", exception)
//            }

        //테스트구역 끝

        //다이얼로그 생성
        val items = arrayOf("수정", "삭제")
        var checked = 0
        MaterialAlertDialogBuilder(this)
            .setNeutralButton("취소") { dialog, which ->
            }
            .setPositiveButton("확인") { dialog, which ->
                if (checked == 0) {
                    //수정하기
                    Toast.makeText(this, documentDelete, Toast.LENGTH_SHORT).show()
                } else if (checked == 1) {
                    //삭제하기
                    db.collection("post").document(documentDelete!!)
                        .collection("comment")
                        .document("333")
                        .delete()
                }

                //처리하고 나서 새로고침하기
                db.collection("post").document(documentDelete!!).collection("comment")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val item = CommunityDetailDataClass(
                                document["text"] as String,
                                document["date"] as String
                            )
                            itemList.add(item)
                        }
                        adapter.notifyDataSetChanged()
                    }
                    .addOnFailureListener { exception ->
                        Log.w(
                            "CommunityDetail댓글",
                            "Error getting documents.",
                            exception
                        )
                    }
                itemList.clear()

            }
            .setSingleChoiceItems(items, checked) { dialog, which ->
                checked = which
            }
            .show()
    }
}