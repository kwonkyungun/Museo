package com.classic.museo.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.classic.museo.R
import com.classic.museo.data.Recording
import com.classic.museo.databinding.RecyclerviewFreeBinding
import com.classic.museo.itemPage.DetailActivity

class FreeAdapter(val hContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var freeData = mutableListOf<Recording>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            RecyclerviewFreeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    inner class FreeItem02(private val binding: RecyclerviewFreeBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        var freeView = binding.root

        init {
            freeView.setOnClickListener(this)
        }

        fun bind(pos: Int) {
            binding.textViewFree.text = freeData[pos].fcltyNm
            binding.textViewUpdate.text = "update : "+freeData[pos].referenceDate
            if(binding.textViewFree.text.endsWith("미술관")){
                binding.imageViewFree.setImageResource(R.drawable.drawing)
            }else {
                binding.imageViewFree.setImageResource(R.drawable.museumicon)
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            val intent = Intent(hContext, DetailActivity::class.java)
            intent.apply {
                putExtra("museumData", freeData[position])
                putExtra("museoId", freeData[position].museoId)
            }
            hContext.startActivity(intent)
        }
    }
}