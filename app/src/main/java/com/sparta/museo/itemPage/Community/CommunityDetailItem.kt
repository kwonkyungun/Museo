package com.sparta.museo.itemPage.Community

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.sparta.museo.R
import com.sparta.museo.data.MyPostDTO

class CommunityDetailItem(val mContext: CommunityDetailActivity, val mItems: MutableList<MyPostDTO>) : BaseAdapter() {

    override fun getCount(): Int {
        return mItems.size
    }

    override fun getItem(position: Int): Any {
        return mItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        var convertView = convertView
        if (convertView == null) convertView = LayoutInflater.from(parent?.context)
            .inflate(R.layout.mypost_item, parent, false)

        val item: MyPostDTO = mItems[position]

        val tv_title = convertView?.findViewById<TextView>(R.id.itemTitle)
        val tv_sub = convertView?.findViewById<TextView>(R.id.itemSub)


        tv_title?.text = item.aTitle
        tv_sub?.text = item.aSub

        return convertView
    }
}