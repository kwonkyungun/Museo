package com.classic.museo.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.classic.museo.itemPage.DetailActivity
import com.classic.museo.R
import com.classic.museo.data.Recording
import com.classic.museo.databinding.RecyclerviewItem2BigBinding
import com.classic.museo.databinding.RecyclerviewItem2Binding
import com.google.firebase.storage.FirebaseStorage

class HomeAdapter2(var subject: String, val hContext: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var museoData = mutableListOf<Recording>()
    private var intent = Intent()
    private val glide: RequestManager = Glide.with(hContext)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            RecyclerviewItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Item02(view)

    }

    override fun getItemCount(): Int {
        return museoData.size
    }

    fun clearItem() {
        museoData.clear()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (museoData[position].viewType) {
            ViewType.MEDIUM -> {
                (holder as Item02).bind(position)
            }
        }
    }

    inner class Item02(private val binding: RecyclerviewItem2Binding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        var smallView = binding.root

        init {
            smallView.setOnClickListener(this)
        }

        fun bind(pos: Int) {
            binding.textView2.text = museoData[pos].fcltyNm
            binding.textView4.text="update : "+museoData[pos].referenceDate
            category(museoData[pos].fcltyNm)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            intent = Intent(hContext, DetailActivity::class.java)
            intent.apply {
                putExtra("museumData", museoData[position])
                putExtra("museoId", museoData[position].museoId)
            }

            hContext.startActivity(intent)
        }

        private fun category(text: String) {
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference
            var category = ""
            if (subject == "이색 박물관") {
                category = "unique"
            }else if (subject == "생물 박물관") {
                category = "Biology"
            }else if (subject == "과학 박물관") {
                category = "science"
            }
            val storageReference = storageRef.child("${category}/${text}.png")

            storageReference.downloadUrl.addOnSuccessListener { uri ->
                val imageURL = uri!!
                glide
                    .load(imageURL)
                    .into(binding.imageView2)
                Log.e("이미지", "${imageURL}")
            }.addOnFailureListener { exception ->
                Log.w("오류", "${exception}")
            }
        }
    }
}