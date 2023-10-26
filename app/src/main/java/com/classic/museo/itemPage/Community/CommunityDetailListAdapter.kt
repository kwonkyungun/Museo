package com.classic.museo.itemPage.Community

import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.classic.museo.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CommunityDetailListAdapter(val itemList: ArrayList<CommunityDetailDataClass>): RecyclerView.Adapter<CommunityDetailListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityDetailListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_community_detail_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CommunityDetailListAdapter.ViewHolder, position: Int) {
        holder.text.text = itemList[position].text
        holder.date.text = itemList[position].date
        //holder.time.text = itemList[position].time
        holder.dropdown.setOnClickListener {
            Log.d("드롭다운버튼 테스트","")
//            val items = arrayOf("수정","삭제")
//            var checked = 0
//            MaterialAlertDialogBuilder(CommunityDetailActivity())
//                .setNeutralButton("취소") { dialog, which ->
//                }
//                .setPositiveButton("확인") { dialog, which ->
//                    Log.d("test", items[checked])
//                }
//                .setSingleChoiceItems(items, checked) { dialog, which ->
//                    checked = which
//                }
//                .show()
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.CommunityDetailSub)
        val date: TextView = itemView.findViewById(R.id.CommunityDetailDate)
        //val time: TextView = itemView.findViewById(R.id.CommunityDetailTime)
        val dropdown: ImageView = itemView.findViewById(R.id.CommunityDetailDropmenu)
    }
}