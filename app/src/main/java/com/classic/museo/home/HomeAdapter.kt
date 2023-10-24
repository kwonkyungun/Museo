package com.classic.museo.home

import android.content.ClipData.Item
import android.content.ContentValues
import android.content.Context
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.classic.museo.data.Record
import com.classic.museo.databinding.RecyclerviewItem1Binding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import okhttp3.internal.notify

class HomeAdapter(
    private val hContext: Context,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var item = mutableListOf<Record>()
    var item2 = mutableListOf<Record>()
    var item3 = mutableListOf<Record>()
    var item4 = mutableListOf<Record>()
    var item5 = mutableListOf<Record>()
    private var db = Firebase.firestore
    private var gson = GsonBuilder().create()
    private lateinit var adapter: HomeAdapter2
    private lateinit var fAdapter: FreeAdapter

    var data = mutableMapOf(
        "인기" to item,
        "이색 박물관" to item3,
        "생물 박물관" to item4,
        "과학 박물관" to item5,
        "무료 박물관/미술관" to item2,
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            RecyclerviewItem1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Item01(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.e("테스트", "${position}")
        (holder as Item01).bind(position)
    }

    fun regionDB(text: String) {
        item2.clear()
        fAdapter.clearItem()
        db.collection("museoInfo")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var region = document.get("rdnmadr").toString()
                    if (region.contains(text)) {
                        val value = gson.toJson(document.data)
                        val result = gson.fromJson(value, Record::class.java)
                        item2.add(result)
                    }
                }
                data.put("무료 박물관/미술관", item2)
                notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    inner class Item01(private val binding: RecyclerviewItem1Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pos: Int) {
            binding.subject.text = data.keys.elementAt(pos)

            if (binding.subject.text == "무료 박물관/미술관") {
                binding.filter2.visibility = View.VISIBLE
                binding.filter2.setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
//                    regionDB(text)
                }
            }

            adapter = HomeAdapter2(binding.subject.text.toString(), hContext)
            fAdapter = FreeAdapter(hContext)
            if (pos == 4) {
                Log.e("테스트용","${item2}")
                fAdapter.freeData = data.values.elementAt(pos)
                binding.recyclerHome2.adapter = fAdapter
            } else {
                adapter.museoData = data.values.elementAt(pos)
                binding.recyclerHome2.adapter = adapter
            }
            binding.recyclerHome2.layoutManager = LinearLayoutManager(
                binding.recyclerHome2.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding.recyclerHome2.setHasFixedSize(true)
        }
    }
}