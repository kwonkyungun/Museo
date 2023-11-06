package com.classic.museo.itemPage.search

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.classic.museo.R
import com.classic.museo.data.Record
import com.classic.museo.databinding.FragmentSearchBinding
import com.classic.museo.databinding.FragmentSearchRegionBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder


class RegionFragment : Fragment() {
    private lateinit var binding : FragmentSearchRegionBinding
    private lateinit var sContext: Context
    private lateinit var searchAdapter : SearchAdapter
    private var db = Firebase.firestore
    private var gson = GsonBuilder().create()
    private var items = mutableListOf<Record>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sContext=context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSearchRegionBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialSetUp()
        binding.searchSpinner.setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
            binding.searchImage.setOnClickListener{
                regionSearchDB(text)
            }
        }
    }

    private fun initialSetUp() {
        searchAdapter = SearchAdapter(sContext)
        searchAdapter.clearItem()
        binding.searchRecyclerview.adapter = searchAdapter
        binding.searchRecyclerview.layoutManager = LinearLayoutManager(sContext)
        binding.searchRecyclerview.setHasFixedSize(true)
    }

    private fun regionSearchDB(text: String) {
        items.clear()
        searchAdapter.clearItem()
        db.collection("museoInfo")
            .whereGreaterThanOrEqualTo("rdnmadr","${text}")
            .whereLessThanOrEqualTo("rdnmadr","${text}" + "\uf8ff").limit(45)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val value = gson.toJson(document.data)
                    val result = gson.fromJson(value, Record::class.java)
                    result.museoId  = document.id
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