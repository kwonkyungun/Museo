package com.classic.museo.itemPage.MypageInnerActivity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.classic.museo.R
import com.classic.museo.data.AnnouncementDTO
import com.classic.museo.data.LikeDTO
import com.classic.museo.data.MyPostDTO
import com.classic.museo.databinding.ActivityMypageLikeItemBinding
import com.classic.museo.databinding.AnnouncementItemBinding
import com.classic.museo.itemPage.announcement.AnnouncementAdapter
import com.classic.museo.itemPage.announcement.AnnouncementDetailActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LikeAdapter(val mItems: MutableList<LikeDTO>,
                  val context: Context,val likeId:MutableList<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ActivityMypageLikeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return likeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as likeViewHolder).bind(position)
    }


    inner class likeViewHolder(private val binding: ActivityMypageLikeItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        var detailPage = binding.root

        init {
            detailPage.setOnClickListener(this)
        }

        fun bind(position: Int){
            binding.likeTitle.text = mItems[position].title
            binding.likeAddress.text = mItems[position].address
            binding.likeCategory.text = mItems[position].category
            binding.likeMuseum.text = mItems[position].museum
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            val intent = Intent(context, MypageLike::class.java)
            intent.apply {
                //detail에 데이터 보내주기
                putExtra("likeData",mItems[position])
                putExtra("documentID",likeId[position])
            }
            context.startActivity(intent)
        }

    }
}