package com.classic.museo.itemPage.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.classic.museo.data.Record
import com.classic.museo.databinding.SearchImageBinding

class SearchAdapter(sContext: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var searchItems = mutableListOf<Record>()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SearchHolder).bind(position)
    }

    fun clearItem() {
        searchItems.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = SearchImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchHolder(view)
    }

    override fun getItemCount(): Int {
        return searchItems.size
    }

    inner class SearchHolder(private val binding: SearchImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pos: Int) {
            binding.searchName.text = searchItems[pos].fcltyNm
            Log.e("검사", "${searchItems[pos]}")
        }
    }

}