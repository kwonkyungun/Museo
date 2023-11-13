package com.sparta.museo.itemPage.search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sparta.museo.R
import com.sparta.museo.data.Recording
import com.sparta.museo.databinding.SearchImageBinding
import com.sparta.museo.itemPage.DetailActivity

class SearchAdapter(val sContext: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var searchItems = mutableListOf<Recording>()
    var museoData = mutableListOf<Recording>()
    private var intent= Intent()

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
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        var searchView=binding.root

        init {
            searchView.setOnClickListener(this)
        }
        fun bind(pos: Int) {
            binding.searchName.text = searchItems[pos].fcltyNm
            binding.reference.text="update : "+searchItems[pos].referenceDate
            if(binding.searchName.text.endsWith("미술관")){
                binding.searchImage.setImageResource(R.drawable.drawing)
            }else {
                binding.searchImage.setImageResource(R.drawable.museumicon)
            }
        }

        override fun onClick(v: View?) {
            val position=bindingAdapterPosition.takeIf { it!=RecyclerView.NO_POSITION } ?:return
            intent=Intent(sContext,DetailActivity::class.java)
            intent.apply {
                putExtra("museumData",searchItems[position])
                putExtra("museoId", searchItems[position].museoId)
            }
            sContext.startActivity(intent)
        }
    }

}