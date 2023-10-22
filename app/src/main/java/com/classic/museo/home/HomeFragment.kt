package com.classic.museo.home

import android.content.ContentValues
import android.content.Context
import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.provider.Settings.Global
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.classic.museo.data.Record
import com.classic.museo.databinding.FragmentHomeBinding
import com.example.museoapitest.retrofit.NetWorkClient
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var database : DatabaseReference
    private lateinit var hContext : Context
    private lateinit var hAdapter : HomeAdapter
    private var gson=GsonBuilder().create()

    private var items= mutableListOf<Record>()  //인기 박물관/미술관 목록
    private var items2= mutableListOf<Record>() //무료 박물관/미술관 목록
    private var items3= mutableListOf<Record>() //이색 박물관/미술관 목록
    private var returnData :Map<String, String> = HashMap<String, String>() //참고용
    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hContext=context
    }

    private fun loadDB() {
        db.collection("museoInfo")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var name=document.get("fcltyNm").toString()
                    var chrge=document.get("adultChrge").toString()
                    var etcChrge=document.get("etcChrgeInfo").toString()

                    //무료
                    if(chrge=="0"||etcChrge=="무료"){
                        val value=gson.toJson(document.data)
                        val result=gson.fromJson(value,Record::class.java)
                        items2.add(result)
                    }
                    //인기
                    if(name=="국립경주박물관"||name=="국립고궁박물관"||name=="국립민속박물관"||name=="국립중앙박물관"||name=="대림미술관"||name=="리움미술관"||
                        name=="부산시립박물관"||name=="서울시립미술관"||name=="제주유리박물관"||name=="미메시스아트뮤지엄"){
                        val value=gson.toJson(document.data)
                        val result=gson.fromJson(value,Record::class.java)
                        items.add(result)
                    }

                    //이색
                    if(name=="헬로키티아일랜드"||name=="한국영화박물관"||name=="한국만화박물관"||name=="대한민국술테마박물관"||name=="수도국산달동네박물관"||name=="둘리뮤지엄"||
                        name=="브이센터 더 라이브 뮤지엄"||name=="뮤지엄다"||name=="피규어 뮤지엄제주"||name=="한국차(tea)박물관"){
                        val value=gson.toJson(document.data)
                        val result=gson.fromJson(value,Record::class.java)
                        items3.add(result)
                    }

                }
                    hAdapter=HomeAdapter(hContext,items,items2,items3)
                    binding.recyclerHome1.adapter=hAdapter
                    binding.recyclerHome1.layoutManager=LinearLayoutManager(hContext)
                    binding.recyclerHome1.setHasFixedSize(true)

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            loadDB()

            binding.filter.setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
                regionDB(text)
            }
    }

    private fun regionDB(text:String) {
        items2.clear()
        hAdapter.clearItem()
        db.collection("museoInfo")
            .get()
            .addOnSuccessListener { result ->
                for(document in result){
                    var region=document.get("rdnmadr").toString()
                    if(region.contains(text)){
                        val value=gson.toJson(document.data)
                        val result=gson.fromJson(value,Record::class.java)
                        items2.add(result)
                    }
                }
                hAdapter.data.put("무료 박물관/미술관",items2)
                hAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }
}