package com.classic.museo.itemPage.MypageInnerActivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.classic.museo.R
import com.classic.museo.data.CommunityDTO
import com.classic.museo.databinding.MypostItemBinding
import com.classic.museo.itemPage.Community.CommunityDetailActivity
import com.classic.museo.itemPage.DetailActivity

class WrittenAdapter(val mItems: MutableList<CommunityDTO>,val context:Context,val postId:MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view= MypostItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyPost(view)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MyPost).bind(position)
    }

    inner class MyPost(private var binding : MypostItemBinding) : RecyclerView.ViewHolder(binding.root),View.OnClickListener {
        var myPostView=binding.root

        init {
            myPostView.setOnClickListener(this)
        }
        fun bind(pos:Int){
            binding.itemTitle.text=mItems[pos].title
            binding.itemSub.text=mItems[pos].date
        }

        override fun onClick(v : View?) {
            val position=bindingAdapterPosition.takeIf { it!=RecyclerView.NO_POSITION } ?:return
            val intent=Intent(context,CommunityDetailActivity::class.java)
            intent.apply {
                putExtra("communityData",mItems[position])
                putExtra("documentId",postId[position])
            }
            context.startActivity(intent)
        }

    }

}