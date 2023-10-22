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

class HomeAdapter(private val hContext : Context,private var item:MutableList<Record>, private var item2:MutableList<Record>
, private var item3:MutableList<Record>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var db= Firebase.firestore
    private var gson= GsonBuilder().create()
    private lateinit var adapter : HomeAdapter2

    var data= mutableMapOf(
        "인기" to item,
        "무료 박물관/미술관" to item2,
        "이색 박물관/미술관" to item3,
    )

    fun clearItem() {
        item2.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view=RecyclerviewItem1Binding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Item01(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.e("테스트","${position}")
        (holder as Item01).bind(position)
    }

    inner class Item01(private val binding : RecyclerviewItem1Binding) : RecyclerView.ViewHolder(binding.root){

        fun bind(pos:Int){
            binding.subject.text=data.keys.elementAt(pos)

            adapter=HomeAdapter2(binding.subject.text.toString(),hContext)
            adapter.museoData=data.values.elementAt(pos)
            binding.recyclerHome2.adapter=adapter
            binding.recyclerHome2.layoutManager = LinearLayoutManager(binding.recyclerHome2.context, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerHome2.setHasFixedSize(true)
        }
    }
}