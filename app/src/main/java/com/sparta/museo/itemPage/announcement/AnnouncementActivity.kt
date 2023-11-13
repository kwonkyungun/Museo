package com.sparta.museo.itemPage.announcement


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sparta.museo.data.AnnouncementDTO
import com.sparta.museo.databinding.ActivityAnnouncementBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

class AnnouncementActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAnnouncementBinding
    private lateinit var adapter : AnnouncementAdapter
    private lateinit var gridLayoutManager: StaggeredGridLayoutManager
    private var db = Firebase.firestore
    private val gson = GsonBuilder().create()
    private val IdItems = mutableListOf<String>()
    private val loadItem = mutableListOf<AnnouncementDTO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnnouncementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뒤로가기 버튼
        binding.announcementBack.setOnClickListener {
            finish()
        }
        //파이어스토어 인스턴스 초기화
        db = FirebaseFirestore.getInstance()


        setupView()
        postingLoad()


    }
    private fun postingLoad(){
        adapter.clearItem()
        val query = db.collection("announcement")
        query.orderBy("date", Query.Direction.DESCENDING)
            query.get()
            .addOnSuccessListener { result ->
                for(document in result){
                    val value= gson.toJson(document.data)
                    val result = gson.fromJson(value, AnnouncementDTO::class.java)
                    val documentId = gson.toJson(document.id)!!
                    loadItem.add(result)
                    IdItems.add(documentId)
                }
                adapter.review = loadItem
                adapter.documentID = IdItems
                adapter.notifyDataSetChanged()
            }
    }
    private fun setupView(){
        adapter = AnnouncementAdapter(this)
        gridLayoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerview.layoutManager = gridLayoutManager
        binding.recyclerview.adapter = adapter
        binding.recyclerview.itemAnimator = null
    }

}