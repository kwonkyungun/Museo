package com.classic.museo.itemPage.Community

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.colorSpace
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.classic.museo.data.CommunityDTO
import com.classic.museo.databinding.CommunityImageBinding
import com.google.android.material.tabs.TabLayout.TabGravity
import com.google.firebase.firestore.AggregateSource
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
                    // Count fetched successfully
                    val snapshot = task.result
                    Log.d("댓글수 테스트", "Count: ${snapshot.count}")
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