package com.classic.museo.itemPage.Community

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.classic.museo.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

interface ClickListener {
   fun clickDropDown(){}
}

class CommunityDetailListAdapter(val itemList: ArrayList<CommunityDetailDataClass>, val clickListener: ClickListener): RecyclerView.Adapter<CommunityDetailListAdapter.ViewHolder>() {
    var firestore: FirebaseFirestore? = null
    val db = Firebase.firestore

    private var intent = Intent()

    val documentDelete = intent.getStringExtra("documentUID")
    val documentID = documentDelete?.substring(1 until documentDelete?.lastIndex!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityDetailListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_community_detail_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CommunityDetailListAdapter.ViewHolder, position: Int) {
        val thiscontext = holder.itemView.context
        firestore = FirebaseFirestore.getInstance()

        holder.text.text = itemList[position].text
        holder.date.text = itemList[position].date
        //holder.time.text = itemList[position].time
        holder.dropdown.setOnClickListener {
            clickListener.clickDropDown()
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.CommunityDetailSub)
        val date: TextView = itemView.findViewById(R.id.CommunityDetailDate)
        //val time: TextView = itemView.findViewById(R.id.CommunityDetailTime)
        val dropdown: ImageView = itemView.findViewById(R.id.CommunityDetailDropmenu)
    }
}