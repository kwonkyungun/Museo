package com.classic.museo.itemPage.Community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.classic.museo.itemPage.MypageInnerActivity.DummyItem
import com.classic.museo.databinding.ActivityCommunityDetailBinding

class CommunityDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommunityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //더미데이터
        val dummy = mutableListOf<DummyItem>()
        dummy.add(DummyItem("테스트 제목1","테스트 내용1"))
        dummy.add(DummyItem("테스트 제목2","테스트 내용2"))
        dummy.add(DummyItem("테스트 제목3","테스트 내용3"))
        dummy.add(DummyItem("테스트 제목4","테스트 내용4"))
        binding.recyclerView3.adapter = CommunityDetailItem(this,dummy)

        firestore = FirebaseFirestore.getInstance()

        //등록버튼 클릭리스너
        binding.communityDetailSave.setOnClickListener {
            Toast.makeText(this,"test",Toast.LENGTH_SHORT).show()
        }

        // community recyclerview 아이템 클릭시 보낸 값 받아오기
        val title = intent.getStringExtra("title").toString()
        val text = intent.getStringExtra("text").toString()
        val museum = intent.getStringExtra("museum").toString()
        val NickName = intent.getStringExtra("NickName").toString()
        val date = intent.getStringExtra("date").toString()

        binding.communityDetailTitle.text = title
        binding.communityDetailText.text = text
        binding.communityDetailMuseum.text = museum
        binding.communityDetailName.text = NickName
        binding.communityDetailDate.text = date

        // 수정버튼 클릭
        binding.btnCommunityDetailEdit.setOnClickListener{

            binding.editPageText.visibility = View.VISIBLE
            binding.communityPlusLogo.visibility = View.INVISIBLE
            binding.communityDetailBack.visibility = View.INVISIBLE
            binding.btnCommunityDetailFinish.visibility = View.VISIBLE
            binding.btnCommunityDetailEdit.visibility = View.INVISIBLE
            binding.btnCommunityDetailDelete.visibility = View.INVISIBLE
            binding.communityDetailText.visibility = View.INVISIBLE
            binding.communityDetailEdit.visibility = View.VISIBLE
            binding.communityDetailEdit.setText(binding.communityDetailText.text)
        }

        //수정완료 버튼
        binding.btnCommunityDetailFinish.setOnClickListener{

            binding.editPageText.visibility = View.INVISIBLE
            binding.communityPlusLogo.visibility = View.VISIBLE
            binding.communityDetailBack.visibility = View.VISIBLE
            binding.btnCommunityDetailFinish.visibility = View.INVISIBLE
            binding.btnCommunityDetailEdit.visibility = View.VISIBLE
            binding.btnCommunityDetailDelete.visibility = View.VISIBLE
            binding.communityDetailText.visibility = View.VISIBLE
            binding.communityDetailText.setText(binding.communityDetailEdit.text)
            binding.communityDetailEdit.visibility = View.INVISIBLE
        }

        //삭제버튼 클릭
        binding.btnCommunityDetailDelete.setOnClickListener{

        }

        // 뒤로가기 버튼
        binding.communityDetailBack.setOnClickListener{
            finish()
        }

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

            //데이터 저장하기
            db.collection("Comment")
                .add(test)
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }

            //데이터 가져오기
            db.collection("Comment")
                .get()
                .addOnSuccessListener { result ->
                    //중복출력 방지용 리사이클러뷰 초기화
                    itemList.clear()
                    for (document in result) {
                        Log.d(ContentValues.TAG, "receive ${document.id} => ${document.data}")
                        val item = CommunityDetailDataClass(document["text"] as String, document["date"] as String)
                        itemList.add(item)
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
                }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        db.collection("Comment")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    val item = CommunityDetailDataClass(document["text"] as String, document["date"] as String)
                    itemList.add(item)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }
}