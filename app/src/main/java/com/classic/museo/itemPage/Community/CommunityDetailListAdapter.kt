package com.classic.museo.itemPage.Community

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.classic.museo.R
import com.classic.museo.data.CommentDTO
import com.classic.museo.databinding.ActivityCommunityDetailItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.user.UserApiClient

class CommunityDetailListAdapter(
    private var itemList: MutableMap<String, CommentDTO>, private val context: Context, private var postId:String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val db= Firebase.firestore
    private var auth=Firebase.auth
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ActivityCommunityDetailItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        Log.e("검사!","${itemList}")
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun clearItem() {
        itemList.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(position)
        holder.select.setOnClickListener {
            showDialog(itemList.keys.elementAt(position))
        }
    }

    inner class ViewHolder(private var binding: ActivityCommunityDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val select=binding.CommunityDetailDropmenu

            fun bind(pos:Int) {
                val fullid = itemList.values.elementAt(pos).id
                val blind = "*****"
                binding.CommunityDetailDate.text=itemList.values.elementAt(pos).date
                if (fullid != null) {
                    binding.CommunityDetailID.text=fullid.substring(0,fullid.indexOf("@"))+"@"+blind
                }
                binding.CommunityDetailName.text=itemList.values.elementAt(pos).nickname
                binding.CommunityDetailSub.text=itemList.values.elementAt(pos).text

                if(auth!!.uid==itemList.values.elementAt(pos).uid){
                    select.visibility=View.VISIBLE
                }else {
                    select.visibility=View.GONE
                }

                UserApiClient.instance.me { user, error ->
                    if(error!=null){
                        Log.e("에러","사용자 정보 요청 실패")
                    }else if(user!=null){
                        if(user.id.toString()==itemList.values.elementAt(pos).uid){
                            select.visibility=View.VISIBLE
                        }else {
                            select.visibility=View.GONE
                        }
                    }
                }
            }
    }

    private fun showDialog(documentId:String) {
        Log.e("테스트1","${postId}")
        val alertDialog=AlertDialog.Builder(context)
        alertDialog.setTitle("댓글 삭제")
        alertDialog.setMessage("댓글을 삭제하시겠습니까?")

        alertDialog.setPositiveButton("삭제") {_,_ ->
            itemList.remove(documentId)
            notifyDataSetChanged()
            db.collection("post")
                .document(postId)
                .collection("comment")
                .document(documentId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(context,"댓글이 삭제되었습니다.",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { error ->
                    Log.w(TAG,"에러",error)
                }
        }

        alertDialog.setNegativeButton("취소") {_,_ ->}

        alertDialog.show()
    }
}