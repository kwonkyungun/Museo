package com.sparta.museo.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.sparta.museo.data.Recording
import com.sparta.museo.databinding.ImageViewpagerItemBinding
import com.sparta.museo.itemPage.DetailActivity
import com.google.firebase.storage.FirebaseStorage

class ImageViewPagerAdapter(var pContext: Context, var item:MutableList<Recording>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val glide: RequestManager = Glide.with(pContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view=ImageViewpagerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewPagerHolder(view)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewPagerHolder).bind(position)
    }

    inner class ViewPagerHolder(val binding:ImageViewpagerItemBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        var pagerView=binding.root

        init {
            pagerView.setOnClickListener(this)
        }

        fun bind(pos:Int) {
            binding.textView3.text=item[pos].fcltyNm
            imageLoad(item[pos].fcltyNm)
        }

        override fun onClick(p0: View?) {
            val position=bindingAdapterPosition.takeIf { it!=RecyclerView.NO_POSITION } ?:return
            val intent= Intent(pContext,DetailActivity::class.java)
            intent.apply {
                putExtra("museumData",item[position])
                putExtra("museoId",item[position].museoId)
            }
            pContext.startActivity(intent)
        }

        private fun imageLoad(text:String) {
            val storage= FirebaseStorage.getInstance()
            val storageRef=storage.reference
            val storageReference = storageRef.child("Popular/${text}.jpg")

            storageReference.downloadUrl.addOnSuccessListener { uri ->
                val imageURL = uri!!
                glide
                    .load(imageURL)
                    .into(binding.imageView3)
            }.addOnFailureListener { exception ->
                Log.w("오류", "${exception}")
            }
        }
    }
}