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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.classic.museo.itemPage.Community.CommunityPlusActivity
import com.classic.museo.data.CommunityDTO
import com.classic.museo.databinding.FragmentCommunityBinding
import com.classic.museo.itemPage.Community.CommunityAdapter
import com.classic.museo.itemPage.Community.CommunityDetailActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.kakao.sdk.user.UserApiClient


class CommunityFragment : Fragment() {

    private lateinit var binding: FragmentCommunityBinding
    private lateinit var communityContext: Context
    private lateinit var adapter: CommunityAdapter
    private lateinit var gridLayoutManager: StaggeredGridLayoutManager
    private val db=Firebase.firestore
    private var items= mutableListOf<CommunityDTO>()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        communityContext = context
    }

    override fun onResume() {
        super.onResume()
        adapter.postFirestore()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchCategory()
    }
   private fun searchCategory() {

       binding.communitySpinner1.setOnSpinnerItemSelectedListener<String> { _, _, _, category ->
           val gson=GsonBuilder().create()
           if(category=="title"){
               binding.communitySearch.setOnClickListener {
                   adapter.clearItem()
                   var searchText=binding.editText.text.toString()
                   db.collection("post")
                       .whereEqualTo("title","$searchText")
                       .get()
                       .addOnSuccessListener { result ->
                           for(document in result){
                               val value=gson.toJson(document.data)
                               val result=gson.fromJson(value,CommunityDTO::class.java)!!
                               items.add(result)
                           }
                           if(items.isEmpty()){
                               Toast.makeText(communityContext,"찾는 게시글이 없습니다.",Toast.LENGTH_SHORT).show()
                           }
                           adapter.review=items
                           adapter.notifyDataSetChanged()
                       }.addOnFailureListener { exception ->
                           Log.w(TAG, "Error getting documents: ", exception)
                       }
               }
           }else if(category=="museum"){
               binding.communitySearch.setOnClickListener {
                   adapter.clearItem()
                   var searchText=binding.editText.text.toString()
                   db.collection("post")
                       .whereEqualTo("museum","$searchText")
                       .get()
                       .addOnSuccessListener { result ->
                           for(document in result){
                               val value=gson.toJson(document.data)
                               val result=gson.fromJson(value,CommunityDTO::class.java)!!
                               items.add(result)
                           }
                           if(items.isEmpty()){
                               Toast.makeText(context,"찾는 게시글이 없습니다.",Toast.LENGTH_SHORT).show()
                           }
                           adapter.review=items
                           adapter.notifyDataSetChanged()
                       }.addOnFailureListener { exception ->
                           Log.w(TAG, "Error getting documents: ", exception)
                       }
               }
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

        return binding.root
    }

    private fun setupView() {
        gridLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView2.layoutManager = gridLayoutManager
        adapter = CommunityAdapter(communityContext)
        binding.recyclerView2.adapter = adapter
        binding.recyclerView2.itemAnimator = null
    }

}