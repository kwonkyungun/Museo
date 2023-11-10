package com.classic.museo.itemPage

import android.content.ContentValues.TAG
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.classic.museo.Login.LoginActivity
import com.classic.museo.Login.SignupActivity
import com.classic.museo.MainActivity
import com.classic.museo.itemPage.Community.CommunityPlusActivity
import com.classic.museo.data.CommunityDTO
import com.classic.museo.data.KakaoUsers
import com.classic.museo.databinding.FragmentCommunityBinding
import com.classic.museo.itemPage.Community.CommunityAdapter
import com.classic.museo.itemPage.Community.CommunityDetailActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.user.UserApiClient
import org.checkerframework.checker.units.qual.C


class CommunityFragment : Fragment() {

    private lateinit var binding: FragmentCommunityBinding
    private lateinit var communityContext: Context
    private lateinit var adapter: CommunityAdapter
    private lateinit var gridLayoutManager: StaggeredGridLayoutManager
    private val db = Firebase.firestore
    private var items = mutableListOf<CommunityDTO>()
    private var idItems = mutableListOf<String>()
    private var gson = GsonBuilder().create()
    private var loadItems = mutableListOf<CommunityDTO>()
    private var auth: FirebaseAuth? = null
    private var kUsers = KakaoUsers()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communityContext = context
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.editText.text.clear()
        postingLoad()
    }

    private fun postingLoad() {
        loadItems.clear()
        idItems.clear()
        adapter.clearItem()
        val query = db.collection("post")
        query.orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val value = gson.toJson(document.data)
                    val result = gson.fromJson(value, CommunityDTO::class.java)
                    val documentId = document.id
                    Log.d("Community", documentId)
                    loadItems.add(result)
                    idItems.add(documentId)
                }
                adapter.review = loadItems
                adapter.documentID = idItems
                adapter.notifyDataSetChanged()
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchCategory()
    }

    private fun searchCategory() {
        binding.communitySpinner1.setOnSpinnerItemSelectedListener<String> { _, _, _, category ->
            val gson = GsonBuilder().create()
            if (category == "제목") {
                binding.communitySearch.setOnClickListener {
                    items.clear()
                    idItems.clear()
                    adapter.clearItem()
                    var searchText = binding.editText.text.toString()
                    db.collection("post")
                        .whereEqualTo("title", "$searchText")
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                val value = gson.toJson(document.data)
                                val result = gson.fromJson(value, CommunityDTO::class.java)!!
                                val documentId=document.id
                                items.add(result)
                                idItems.add(documentId)
                            }
                            if (items.isEmpty()) {
                                Toast.makeText(
                                    communityContext,
                                    "찾는 게시글이 없습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            adapter.review = items
                            adapter.documentID=idItems
                            adapter.notifyDataSetChanged()
                        }.addOnFailureListener { exception ->
                            Log.w(TAG, "Error getting documents: ", exception)
                        }
                }
            } else if (category == "장소") {
                binding.communitySearch.setOnClickListener {
                    items.clear()
                    idItems.clear()
                    adapter.clearItem()
                    var searchText = binding.editText.text.toString()
                    db.collection("post")
                        .whereEqualTo("museum", "$searchText")
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                val value = gson.toJson(document.data)
                                val result = gson.fromJson(value, CommunityDTO::class.java)!!
                                val documentId=document.id
                                items.add(result)
                                idItems.add(documentId)
                            }
                            if (items.isEmpty()) {
                                Toast.makeText(context, "찾는 게시글이 없습니다.", Toast.LENGTH_SHORT).show()
                            }
                            adapter.review = items
                            adapter.documentID=idItems
                            adapter.notifyDataSetChanged()
                        }.addOnFailureListener { exception ->
                            Log.w(TAG, "Error getting documents: ", exception)
                        }
                }
            } else if (category == "전체") {

                postingLoad()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)
        setupView()


        // +버튼 클릭시 새글추가 Activity로 이동
        binding.communityBtnPlus.setOnClickListener {
            val intent = Intent(context, CommunityPlusActivity::class.java)
            startActivity(intent)
        }

        //새글추가 버튼 맨 앞으로 보내기
        binding.communityBtnPlus.bringToFront()

        nonLogin()

        return binding.root
    }

    private fun setupView() {
        gridLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView2.layoutManager = gridLayoutManager
        adapter = CommunityAdapter(communityContext)
        binding.recyclerView2.adapter = adapter
        binding.recyclerView2.itemAnimator = null
    }

    private fun nonLogin() {
        auth = Firebase.auth
        val currentUser = auth?.currentUser

        UserApiClient.instance.me { user, error ->

            if (currentUser == null && user == null) {
                val loginBuilder = AlertDialog.Builder(communityContext)
                loginBuilder.setTitle("로그인이 필요한 서비스입니다.")
                loginBuilder.setMessage("로그인 하시겠습니까?")

                loginBuilder.setPositiveButton("확인") { dialog, _ ->
                    val loginIntent = Intent(communityContext, LoginActivity::class.java)
                    loginIntent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(loginIntent)
                }
                loginBuilder.setNegativeButton("취소") { dialog, _ ->
                    val cancelIntent = Intent(communityContext, MainActivity::class.java)
                    startActivity(cancelIntent)
                }
                loginBuilder.setCancelable(false)
                loginBuilder.show()
            }
        }
    }

}