package com.classic.museo.itemPage.Community

import android.annotation.SuppressLint
import android.content.ContentValues
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
import com.classic.museo.itemPage.MypageInnerActivity.DummyItem
import com.classic.museo.databinding.ActivityCommunityDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CommunityDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommunityDetailBinding
    var firestore: FirebaseFirestore? = null
    val itemList = arrayListOf<CommunityDetailDataClass>()
    val adapter = CommunityDetailListAdapter(itemList)
    private var auth : FirebaseAuth? = null
    val db = Firebase.firestore
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.recyclerView3.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView3.adapter = adapter

        firestore = FirebaseFirestore.getInstance()

        //등록버튼 클릭리스너
        binding.communityDetailSave.setOnClickListener {

            val plusTime: LocalDateTime? = LocalDateTime.now()
            val formatterDate = DateTimeFormatter.ISO_DATE
            val formattedDate = plusTime?.format(formatterDate)
            val formatterTime = DateTimeFormatter.ISO_LOCAL_TIME
            val formattedTime = plusTime?.format(formatterTime)
            val text = binding.communityDetailComment.text.toString()

            val test = hashMapOf(
                "text" to text,
                "date" to formattedDate
            )



//            //데이터 저장하기
//            db.collection("Comment")
//                .add(test)
//                .addOnSuccessListener { documentReference ->
//                    Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//                }
//                .addOnFailureListener { e ->
//                    Log.w(ContentValues.TAG, "Error adding document", e)
//                }
//
//            //데이터 가져오기
//            db.collection("Comment")
//                .get()
//                .addOnSuccessListener { result ->
//                    //중복출력 방지용 리사이클러뷰 초기화
//                    itemList.clear()
//                    for (document in result) {
//                        Log.d(ContentValues.TAG, "receive ${document.id} => ${document.data}")
//                        val item = CommunityDetailDataClass(document["text"] as String, document["date"] as String)
//                        itemList.add(item)
//                    }
//                    adapter.notifyDataSetChanged()
//                }
//                .addOnFailureListener { exception ->
//                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
//                }
        }
        // community recyclerview 아이템 클릭시 보낸 값 받아오기

        val comm=intent.getParcelableExtra<CommunityDTO>("communityData")!!
        val documentDelete = intent.getStringExtra("documentId")
        Log.e("community", "sj communityID : $documentDelete")

        val title = comm.title
        val content = comm.text
        val museum = comm.museum
        val NickName = comm.NickName
        val date = comm.date

        binding.communityDetailTitle.text = title
        binding.communityDetailText.text = content
        binding.communityDetailMuseum.text = museum
        binding.communityDetailName.text = NickName
        binding.communityDetailDate.text = date

        //수정버튼
        binding.btnCommunityDetailDelete.setOnClickListener{

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

            btnEdit.setOnClickListener{
                binding.editPageText.visibility = View.VISIBLE
                binding.communityPlusLogo.visibility = View.INVISIBLE
                binding.communityDetailBack.visibility = View.INVISIBLE
                binding.btnCommunityDetailFinish.visibility = View.VISIBLE
                binding.btnCommunityDetailDelete.visibility = View.INVISIBLE
                binding.communityDetailTitle.visibility = View.INVISIBLE
                binding.communityDetailTitleEdit.visibility = View.VISIBLE
                binding.communityDetailTitleEdit.setText(binding.communityDetailTitle.text)
                binding.communityDetailMuseum.visibility = View.INVISIBLE
                binding.communityDetailMuseumEdit.visibility = View.VISIBLE
                binding.communityDetailMuseumEdit.setText(binding.communityDetailMuseum.text)
                binding.communityDetailText.visibility = View.INVISIBLE
                binding.communityDetailTextEdit.visibility = View.VISIBLE
                binding.communityDetailTextEdit.setText(binding.communityDetailText.text)
                alertDialog.dismiss()

                //수정완료버튼 클릭
                binding.btnCommunityDetailFinish.setOnClickListener{
                    val documentEdit = intent.getStringExtra("documentID")
                    Log.d("community","sj documentID : $documentEdit")
                    binding.editPageText.visibility = View.INVISIBLE
                    binding.communityPlusLogo.visibility = View.VISIBLE
                    binding.communityDetailBack.visibility = View.VISIBLE
                    binding.btnCommunityDetailFinish.visibility = View.INVISIBLE
                    binding.btnCommunityDetailDelete.visibility = View.VISIBLE
                    binding.communityDetailTitle.visibility = View.VISIBLE
                    binding.communityDetailTitleEdit.visibility = View.INVISIBLE
                    binding.communityDetailTitle.setText(binding.communityDetailTitleEdit.text)
                    binding.communityDetailMuseum.visibility = View.VISIBLE
                    binding.communityDetailMuseumEdit.visibility = View.INVISIBLE
                    binding.communityDetailMuseum.setText(binding.communityDetailMuseumEdit.text)
                    binding.communityDetailText.visibility = View.VISIBLE
                    binding.communityDetailText.setText(binding.communityDetailTextEdit.text)
                    binding.communityDetailTextEdit.visibility = View.INVISIBLE

                    db.collection("post").document("$documentEdit")
                        .update("title", "${binding.communityDetailTitleEdit.text}")
                        .addOnSuccessListener { Log.d("CommunityDetail", "DocumentSnapshot successfully updated!") }
                        .addOnFailureListener { e -> Log.w("CommunityDetail", "Error updating document", e) }

                    db.collection("post").document("$documentEdit")
                        .update("text", "${binding.communityDetailTextEdit.text}")
                        .addOnSuccessListener { Log.d("CommunityDetail", "DocumentSnapshot successfully updated!") }
                        .addOnFailureListener { e -> Log.w("CommunityDetail", "Error updating document", e) }

                    db.collection("post").document("$documentEdit")
                        .update("museum", "${binding.communityDetailMuseumEdit.text}")
                        .addOnSuccessListener { Log.d("CommunityDetail", "DocumentSnapshot successfully updated!") }
                        .addOnFailureListener { e -> Log.w("CommunityDetail", "Error updating document", e) }
                }
            }
            btnDelete.setOnClickListener{
                val deleteBuilder =AlertDialog.Builder(this)
                deleteBuilder.setTitle("게시글 삭제")
                deleteBuilder.setMessage("정말로 삭제하시겠습니까?")
                deleteBuilder.setIcon(R.drawable.coment)

                deleteBuilder.setPositiveButton("확인"){ dialog ,_ ->
                    db.collection("post").document(documentDelete!!)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this,"게시물이 삭제되었습니다.",Toast.LENGTH_SHORT).show()
                            Log.d("CommunityDetail", "DocumentSnapshot successfully deleted!") }
                        .addOnFailureListener { e -> Log.w("CommunityDetail", "Error deleting document", e) }
                    adapter.notifyDataSetChanged()
                    finish()
                }
                deleteBuilder.setNegativeButton("취소"){ dialog ,_ ->
                    dialog.dismiss()
                }
                deleteBuilder.show()
                alertDialog.dismiss()
            }
            btnCancel.setOnClickListener{
                alertDialog.dismiss()
            }
            alertDialog.show()
        }

        //로그인한 uid와 작성자 uid가 같으면 수정 삭제 버튼 활성화
        btnEdit()

        // 뒤로가기 버튼
        binding.communityDetailBack.setOnClickListener{
            finish()
        }
    }

    fun btnEdit(){
        val comm=intent.getParcelableExtra<CommunityDTO>("communityData")!!
        auth = Firebase.auth
        val UID = comm.uid
        Log.d("한글","$UID")
        val currentUser = auth?.currentUser?.uid
        Log.d("communityDetail","sj $currentUser")

        if(UID == currentUser){
            binding.btnCommunityDetailDelete.visibility = View.VISIBLE
        }
        else {
            binding.btnCommunityDetailDelete.visibility = View.INVISIBLE
        }
    }
}