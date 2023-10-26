package com.classic.museo.itemPage.Community

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.classic.museo.data.CommunityDTO
import com.classic.museo.databinding.CommunityImageBinding
import com.google.android.material.tabs.TabLayout.TabGravity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class CommunityAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var review = mutableListOf<CommunityDTO>()
    var documentID = mutableListOf<String>()
    val db = Firebase.firestore
    private var intent = Intent()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            CommunityImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    fun clearItem() {
        review.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount() = review.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ImageViewHolder).bind(position)
    }

    inner class ImageViewHolder(val binding: CommunityImageBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        var detailPage = binding.root

        init {
            detailPage.setOnClickListener(this)
        }

        fun bind(pos: Int) {
            binding.textCommunityTitle.text = review[pos].title
            binding.communityNickname.text = review[pos].NickName
            binding.communityId.text = review[pos].UserId
            binding.dateCommunityImage.text = review[pos].date
            binding.communityMuseumName.text = review[pos].museum
        }

        override fun onClick(v: View?) {
            val position =
                bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            intent = Intent(context, CommunityDetailActivity::class.java)
            intent.apply {
                putExtra("communityData",review[position])
                putExtra("documentId",documentID[position])
            }
            context.startActivity(intent)
        }

    }
}