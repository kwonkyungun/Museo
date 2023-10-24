package com.classic.museo.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.classic.museo.itemPage.DetailActivity
import com.classic.museo.R
import com.classic.museo.data.Record
import com.classic.museo.databinding.RecyclerviewItem2BigBinding
import com.classic.museo.databinding.RecyclerviewItem2Binding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class HomeAdapter2(var subject: String, val hContext: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var museoData = mutableListOf<Record>()
    private var intent = Intent()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            RecyclerviewItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        val bigView =
            RecyclerviewItem2BigBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return when (viewType) {
            0 -> Item02(view)
            else -> {
                ItemBig02(bigView)
            }
        }
    }

    override fun getItemCount(): Int {
        return museoData.size
    }

    fun clearItem() {
        museoData.clear()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (museoData[position].viewType) {
            ViewType.BIG -> {
                (holder as ItemBig02).bind(position)
            }

            ViewType.MEDIUM -> {
                (holder as Item02).bind(position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (subject == "인기") {
            museoData[position].viewType = 1
        } else {
            museoData[position].viewType = 0
        }
        return museoData[position].viewType
    }


    inner class Item02(private val binding: RecyclerviewItem2Binding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        var smallView = binding.root

        init {
            smallView.setOnClickListener(this)
        }

        fun bind(pos: Int) {
            binding.textView2.text = museoData[pos].fcltyNm
            category(museoData[pos].fcltyNm)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            intent = Intent(hContext, DetailActivity::class.java)
            intent.apply {
                putExtra("museumData", museoData[position])
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
                Glide.with(hContext)
                    .load(imageURL)
                    .into(binding.imageView2)
                Log.e("이미지", "${imageURL}")
            }.addOnFailureListener { exception ->
                Log.w("오류", "${exception}")
            }
        }
    }


    inner class ItemBig02(private val binding: RecyclerviewItem2BigBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        var popularView = binding.root

        init {
            popularView.setOnClickListener(this)
        }

        fun bind(pos: Int) {
            binding.textView3.text = museoData[pos].fcltyNm

            if (museoData[pos].fcltyNm == "국립경주박물관") {
                binding.imageView3.setImageResource(R.drawable.gyeongju)
            } else if (museoData[pos].fcltyNm == "국립고궁박물관") {
                binding.imageView3.setImageResource(R.drawable.palace)
            } else if (museoData[pos].fcltyNm == "국립민속박물관") {
                binding.imageView3.setImageResource(R.drawable.folk)
            } else if (museoData[pos].fcltyNm == "국립중앙박물관") {
                binding.imageView3.setImageResource(R.drawable.nationalmuseum)
            } else if (museoData[pos].fcltyNm == "대림미술관") {
                binding.imageView3.setImageResource(R.drawable.daelim)
            } else if (museoData[pos].fcltyNm == "서울시립미술관") {
                binding.imageView3.setImageResource(R.drawable.seoulmuseum)
            } else if (museoData[pos].fcltyNm == "부산시립박물관") {
                binding.imageView3.setImageResource(R.drawable.busan)
            } else if (museoData[pos].fcltyNm == "리움미술관") {
                binding.imageView3.setImageResource(R.drawable.leeum)
            } else if (museoData[pos].fcltyNm == "제주유리박물관") {
                binding.imageView3.setImageResource(R.drawable.jeju)
            } else if (museoData[pos].fcltyNm == "미메시스아트뮤지엄") {
                binding.imageView3.setImageResource(R.drawable.mime)
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            intent = Intent(hContext, DetailActivity::class.java)
            intent.apply {
                putExtra("museumData", museoData[position])
            }
            hContext.startActivity(intent)
        }
    }
}