package com.classic.museo.itemPage.announcement

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.classic.museo.data.AnnouncementDTO
import com.classic.museo.databinding.AnnouncementItemBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AnnouncementAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder> () {

        var review = mutableListOf<AnnouncementDTO>()
        var documentID = mutableListOf<String>()
        val db = Firebase.firestore
        private var intent =Intent()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            AnnouncementItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return AnnouncementViewHolder(binding)
    }

    fun clearItem(){
        review.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount() = review.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AnnouncementViewHolder).bind(position)
    }

    inner class AnnouncementViewHolder(val binding: AnnouncementItemBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        var detailPage = binding.root

        init {
            detailPage.setOnClickListener(this)
        }

        fun bind(position: Int) {
            binding.annoItTi.text = review[position].title
            binding.annoItDate.text = review[position].date

            //파이어스토어에서 가지고온 텍스트를 줄바꿈
            binding.annoItTi.text = review[position].title!!.replace("\\n", "\n")
        }

        override fun onClick(v:View){
            val position = bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            intent = Intent(context, AnnouncementDetailActivity::class.java)
            intent.apply {
                //detail에 데이터 보내주기
                putExtra("announcementData",review[position])
                putExtra("documentID",documentID[position])
            }
            context.startActivity(intent)
        }
    }
}