package com.sparta.museo.itemPage.MypageInnerActivity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sparta.museo.data.LikeDTO
import com.sparta.museo.data.Recording
import com.sparta.museo.databinding.ActivityMypageLikeItemBinding
import com.sparta.museo.itemPage.DetailActivity

class LikeAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mItems= mutableListOf<LikeDTO>()
    var museoItem= mutableListOf<Recording>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ActivityMypageLikeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return LikeViewHolder(binding)
    }

    fun clearItem() {
        mItems.clear()
        museoItem.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LikeViewHolder).bind(position)
    }


    inner class LikeViewHolder(private val binding: ActivityMypageLikeItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        var detailPage = binding.root

        init {
            detailPage.setOnClickListener(this)
        }

        fun bind(position: Int) {
            binding.likeTitle.text = mItems[position].title
            binding.likeAddress.text = mItems[position].address
            binding.likeCategory.text = mItems[position].category
            binding.likeMuseum.text = mItems[position].museum
        }

        override fun onClick(v: View?) {
            val position =
                bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            val intent = Intent(context, DetailActivity::class.java)
            intent.apply {
                putExtra("museumData", museoItem[position])
                putExtra("museoId", museoItem[position].museoId)
            }
            context.startActivity(intent)
        }

    }
}