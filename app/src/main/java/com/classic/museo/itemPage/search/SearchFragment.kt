package com.classic.museo.itemPage.search

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
import com.classic.museo.R
import com.classic.museo.data.Record
import com.classic.museo.databinding.FragmentSearchBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var sContext: Context
    private lateinit var searchAdapter: SearchAdapter
    private var db = Firebase.firestore
    private var gson = GsonBuilder().create()
    private var items = mutableListOf<Record>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialSetUp()
        binding.searchSpinner.setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
            regionSearchDB(text)
        }
        searchTry()
    }

    private fun searchTry() {
        binding.searchImage.setOnClickListener {
            var text = binding.editTextText2.text.toString()
            searchAdapter.clearItem()
            searchOne(text)
        }
    }

    private fun initialSetUp() {
        searchAdapter = SearchAdapter(sContext)
        binding.searchRecyclerview.adapter = searchAdapter
        binding.searchRecyclerview.layoutManager = LinearLayoutManager(sContext)
        binding.searchRecyclerview.setHasFixedSize(true)
    }

    private fun searchOne(text: String) {
        searchAdapter.clearItem()
        val query=db.collection("museoInfo").whereEqualTo("fcltyNm","${text}")

        query.get().addOnSuccessListener { result ->
                for (document in result) {
                        val value = gson.toJson(document.data)
                        val result = gson.fromJson(value, Record::class.java)
                        items.add(result)
                }
                if(items.isEmpty()){
                    Toast.makeText(sContext,"찾는 박물관/미술관이 없습니다.",Toast.LENGTH_SHORT).show()
                }
                searchAdapter.searchItems = items
                searchAdapter.notifyDataSetChanged()
            }.addOnFailureListener { exception ->
            Log.w(ContentValues.TAG, "Error getting documents.", exception)
        }
    }

    private fun regionSearchDB(text: String) {
        searchAdapter.clearItem()
        db.collection("museoInfo")
            .whereGreaterThanOrEqualTo("rdnmadr","${text}")
            .whereLessThanOrEqualTo("rdnmadr","${text}" + "\uf8ff").limit(20)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                        val value = gson.toJson(document.data)
                        val result = gson.fromJson(value, Record::class.java)
                        items.add(result)
                }
                searchAdapter.searchItems = items
                searchAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error", exception)
            }
    }


}