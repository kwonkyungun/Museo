package com.sparta.museo.itemPage.search

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.sparta.museo.data.Recording
import com.sparta.museo.databinding.FragmentSearchTabBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

class SearchTabFragment : Fragment() {
    private lateinit var binding: FragmentSearchTabBinding
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var sContext: Context
    private var db = Firebase.firestore
    private var gson = GsonBuilder().create()
    private var items = mutableListOf<Recording>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchTabBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sContext=context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialSetUp()
        searchTry()
    }

    private fun searchTry() {
        binding.searchImage.setOnClickListener {
            var text = binding.searchInput.text.toString()
            searchAdapter.clearItem()
            searchOne(text)
        }
    }

    private fun initialSetUp() {
        searchAdapter = SearchAdapter(sContext)
        searchAdapter.clearItem()
        binding.searchRecyclerview.adapter = searchAdapter
        binding.searchRecyclerview.layoutManager = LinearLayoutManager(sContext)
        binding.searchRecyclerview.setHasFixedSize(true)
    }

    private fun searchOne(text: String) {
        items.clear()
        searchAdapter.clearItem()
        val query=db.collection("museoInfo").whereEqualTo("fcltyNm","${text}")

        query.get().addOnSuccessListener { result ->
            for (document in result) {
                val value = gson.toJson(document.data)
                val result = gson.fromJson(value, Recording::class.java)
                result.museoId  = document.id
                items.add(result)
            }
            if(items.isEmpty()){
                Toast.makeText(sContext,"찾는 박물관/미술관이 없습니다.", Toast.LENGTH_SHORT).show()
            }
            searchAdapter.searchItems = items
            searchAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Log.w(ContentValues.TAG, "Error getting documents.", exception)
        }
    }

}