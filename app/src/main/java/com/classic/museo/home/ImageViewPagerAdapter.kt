package com.classic.museo.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.classic.museo.R
import com.classic.museo.data.Recording
import com.classic.museo.databinding.ImageViewpagerItemBinding
import com.classic.museo.itemPage.DetailActivity

class ImageViewPagerAdapter(var pContext: Context, var item:MutableList<Recording>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


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


            if (item[pos].fcltyNm == "국립경주박물관") {
                binding.imageView3.setImageResource(R.drawable.gyeongju)
            } else if (item[pos].fcltyNm == "국립고궁박물관") {
                binding.imageView3.setImageResource(R.drawable.palace)
            } else if (item[pos].fcltyNm == "국립민속박물관") {
                binding.imageView3.setImageResource(R.drawable.folk)
            } else if (item[pos].fcltyNm == "국립중앙박물관") {
                binding.imageView3.setImageResource(R.drawable.nationalmuseum)
            } else if (item[pos].fcltyNm == "대림미술관") {
                binding.imageView3.setImageResource(R.drawable.daelim)
            } else if (item[pos].fcltyNm == "서울시립미술관") {
                binding.imageView3.setImageResource(R.drawable.seoulmuseum)
            } else if (item[pos].fcltyNm == "부산시립박물관") {
                binding.imageView3.setImageResource(R.drawable.busan)
            } else if (item[pos].fcltyNm == "리움미술관") {
                binding.imageView3.setImageResource(R.drawable.leeum)
            } else if (item[pos].fcltyNm == "제주유리박물관") {
                binding.imageView3.setImageResource(R.drawable.jeju)
            } else if (item[pos].fcltyNm == "미메시스아트뮤지엄") {
                binding.imageView3.setImageResource(R.drawable.mime)
            }
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
    }
}