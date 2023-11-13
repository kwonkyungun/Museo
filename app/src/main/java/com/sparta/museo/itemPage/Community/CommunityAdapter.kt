package com.sparta.museo.itemPage.Community

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sparta.museo.data.CommunityDTO
import com.sparta.museo.databinding.CommunityImageBinding
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommunityAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var review = mutableListOf<CommunityDTO>()
    var documentID = mutableListOf<String>()
    val db = Firebase.firestore
    var intent = Intent()


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
            val fullid = review[pos].UserId
            val blind = "*****"
            val path =
                bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            val query = db.collection("post").document(documentID[path]).collection("comment")
            val countQuery = query.count()
            countQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val snapshot = task.result
                    val count = snapshot.count.toString()
                    binding.textCommunityTitle.text = review[pos].title
                    binding.textCommunitySubcount.text = "[$count]"
                } else {
                    Log.d(TAG, "Count failed: ", task.getException())
                }
            }
            binding.communityNickname.text = review[pos].NickName
            if (fullid != null) {
                binding.communityId.text = fullid.substring(0,fullid.indexOf("@"))+"@"+blind
            }
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