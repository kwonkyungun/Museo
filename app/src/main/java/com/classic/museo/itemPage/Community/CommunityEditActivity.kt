package com.classic.museo.itemPage.Community

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.classic.museo.R
import com.classic.museo.databinding.ActivityCommunityEditBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class CommunityEditActivity : AppCompatActivity() {
    lateinit var binding : ActivityCommunityEditBinding
    lateinit var adapter : CommunityAdapter
    var firestore : FirebaseFirestore? = null
    private var auth : FirebaseAuth? = null
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //커뮤니티 디테일에서 데이터 받아오기
        val title = intent.getStringExtra("title")
        val museum = intent.getStringExtra("museum")
        val text = intent.getStringExtra("text")
        val NickName = intent.getStringExtra("NickName")
        val date = intent.getStringExtra("date")

        binding.communityTitleEdit.setText("$title")
        binding.communityMuseumEdit.setText("$museum")
        binding.communityTextEdit.setText("$text")
        binding.communityEditName.text = NickName
        binding.communityEditDate.text = date

        //이미지 띄워주기
        downloadImage()

        //수정버튼
        binding.communityEdit.setOnClickListener{

            editContent()

//            val intent = Intent(this, CommunityDetailActivity::class.java)
//            startActivity(intent)
        }

        // 이미지 클릭시 수정
        binding.communityEditImage.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery,100)
        }

        //뒤로가기버튼
        binding.communityEditBack.setOnClickListener{
            finish()
        }

        //취소버튼
        binding.communityEditCancle.setOnClickListener{
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100 ){
            binding.communityEditImage.setImageURI(data?.data)
        }
    }
    fun editContent(){
        val documentId = intent.getStringExtra("documentId")
        Log.d("communityEdit","sj documentID $documentId")

        db.collection("post").document("$documentId")
            .update("title" , "${binding.communityTitleEdit.text}",
            "text" , "${binding.communityTextEdit.text}",
            "museum" , "${binding.communityMuseumEdit.text}")
            .addOnSuccessListener {
                Toast.makeText(this,"게시물이 수정되었습니다.",Toast.LENGTH_SHORT).show()
                Log.d("CommunitEdit", "DocumentSnapshot successfully updated!")
                sendToImage()
                finish()
            }
            .addOnFailureListener { e -> Log.w("CommunitEdit", "Error updating document", e) }
    }

    private fun sendToImage(){
        val docID = intent.getStringExtra("documentId")
        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("postedImage/$docID.jpg")

        val imageView = binding.communityEditImage
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

    private fun downloadImage() {
        val docID = intent.getStringExtra("documentId")
        val storageReference = Firebase.storage.reference.child("postedImage/$docID.jpg")
        val imageView = binding.communityEditImage

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageView)
            }
        })
    }
}