package com.classic.museo.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.classic.museo.data.Record
import com.classic.museo.databinding.RecyclerviewItem2Binding
import com.classic.museo.itemPage.DetailActivity

class FreeAdapter(val hContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var freeData = mutableListOf<Record>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            RecyclerviewItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FreeItem02(view)
    }

    override fun getItemCount(): Int {
        return freeData.size
    }

    fun clearItem() {
        freeData.clear()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FreeItem02).bind(position)
    }

    inner class FreeItem02(private val binding: RecyclerviewItem2Binding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        var freeView = binding.root

        init {
            freeView.setOnClickListener(this)
        }

        fun bind(pos: Int) {
            binding.textView2.text = freeData[pos].fcltyNm
            binding.textView4.text = "update : "+freeData[pos].referenceDate

        }

        override fun onClick(v: View?) {
            val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            val intent = Intent(hContext, DetailActivity::class.java)
            intent.apply {
                putExtra("museumData", freeData[position])
            }
            hContext.startActivity(intent)
        }
    }
}