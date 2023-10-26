package com.classic.museo.itemPage.Community

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.classic.museo.data.CommunityDTO
import com.classic.museo.databinding.CommunityImageBinding
import com.google.android.material.tabs.TabLayout.TabGravity
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
    private var documentID = mutableListOf<String>()
    val db = Firebase.firestore
    private var intent = Intent()

//    init {
//        uid = FirebaseAuth.getInstance().uid
//        firestore = FirebaseFirestore.getInstance()
//
//        firestore?.collection(uid!!)?.orderBy("post", Query.Direction.DESCENDING)
//            ?.addSnapshotListener{querySnapshot, firebaseFirestoreException ->
//                review.clear()
//                if (querySnapshot == null) return@addSnapshotListener
//
//                for (snapshot in querySnapshot!!.documents){
//                    var item = snapshot.toObject(CommunityDTO::class.java)
//                    review.add(item!!)
//                }
//                notifyDataSetChanged()
//            }
//    }

//    interface ItemClick{
//        fun onClick(view: View, position: Int)
//    }
//
//    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            CommunityImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun getItemCount() = review.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentReview = review[position]
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val holder = holder as ImageViewHolder

        holder.title.text = currentReview.title
        holder.nickname.text = currentReview.NickName
        holder.userID.text = currentReview.UserId
        holder.date.text = currentReview.date
        holder.museum.text = currentReview.museum
        Log.d("holder", "sj $currentReview")

    }

    fun postFirestore() {
        db.collection("post")
            .orderBy("date", Query.Direction.DESCENDING)
            .get().addOnSuccessListener {
                for (document in it){
                    documentID.add(document.id)
                }
                Log.d("document", "sj ${documentID.toString()}")
            }


        db.collection("post")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                Log.d("postFirestore", "sj postFirestore : $result")

                val newPostData = mutableListOf<CommunityDTO>()
                for (i in result) {
                    if (i.exists()) {
                        Log.d(
                            "postFirestore",
                            "sj postFirestore : ${newPostData.size} , $newPostData"
                        )
                        val postData = i.toObject(CommunityDTO::class.java)
                        newPostData.add(postData)
                    }
                    review.clear()
                    review.addAll(newPostData)
                }
                notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("postFirestore", "error : $e")
            }
    }

    fun usersFirestore() {
        val userFirestore = FirebaseFirestore.getInstance()
        userFirestore.collection("users").get()
            .addOnSuccessListener { result ->
                Log.d("usersFirestore", "sj usersFirestore : $result")

                val newData = mutableListOf<CommunityDTO>()
                for (i in result) {
                    if (i.exists()) {
                        Log.d("usersFirestore", "sj usersFirestore : ${newData.size} , $newData")
                        val userstData = i.toObject(CommunityDTO::class.java)
                        for(userData in review){
                            Log.d("usersFirestore","$userstData, $userData")
                            if(userstData.UID == userData.UID){
                                userData.UserId = userstData.UserId
                                userData.NickName = userstData.NickName
                                newData.add(userData)
                            }
                        }
                    }
                }
                review.clear()
                review.addAll(newData)
                notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("usersFirestore", "error : $e")
            }
    }



inner class ImageViewHolder(val binding: CommunityImageBinding) :
    RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    var title: TextView = binding.textCommunityTitle
    var nickname: TextView = binding.communityNickname
    var userID: TextView = binding.communityId
    var date: TextView = binding.dateCommunityImage
    var museum: TextView = binding.communityMuseumName
    var detailPage = binding.root

    init {
        detailPage.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        val position = absoluteAdapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
        intent = Intent(context, CommunityDetailActivity::class.java)
        intent.apply {
            putExtra("title", review[position].title)
            putExtra("text", review[position].text)
            putExtra("NickName", review[position].NickName)
            putExtra("museum",review[position].museum)
            putExtra("date", review[position].date)
            putExtra("UID",review[position].UID)
            putExtra("documentID",documentID[position])
        }
        context.startActivity(intent)
    }

}
}