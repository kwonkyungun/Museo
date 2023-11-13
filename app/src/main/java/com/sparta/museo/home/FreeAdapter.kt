package com.sparta.museo.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sparta.museo.R
import com.sparta.museo.data.Recording
import com.sparta.museo.databinding.RecyclerviewFreeBinding
import com.sparta.museo.itemPage.DetailActivity

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
            try {
                val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
                val intent = Intent(hContext, DetailActivity::class.java)
                intent.apply {
                    putExtra("museumData", freeData[position])
                    putExtra("museoId", freeData[position].museoId)
                }
                hContext.startActivity(intent)
            } catch (e:Exception){
                Log.e("오류","체크")
            }
        }
    }
}