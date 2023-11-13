package com.sparta.museo.home

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sparta.museo.data.Recording
import com.sparta.museo.databinding.FragmentHomeBinding
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var hContext: Context
    private lateinit var hAdapter: HomeAdapter
    private var gson = GsonBuilder().create()


    private var items = mutableListOf<Recording>()  //인기 박물관/미술관 목록
    private var items2 = mutableListOf<Recording>() //무료 박물관/미술관 목록
    private var items3 = mutableListOf<Recording>() //이색 박물관/미술관 목록
    private var items4 = mutableListOf<Recording>() //해양박물관 목록
    private var items5 = mutableListOf<Recording>() //과학박물관 목록

    private var returnData: Map<String, String> = HashMap<String, String>() //참고용
    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hContext = context

    }

    override fun onResume() {
        super.onResume()
    }

    private fun homeSetting() {
        hAdapter = HomeAdapter(hContext)
        binding.recyclerHome1.adapter = hAdapter
        binding.recyclerHome1.layoutManager = LinearLayoutManager(hContext)
    }

    private fun freeItem() {
        items2.clear()
        hAdapter.clearItem()
        val query = db.collection("museoInfo").whereEqualTo("adultChrge","0")
            .whereEqualTo("etcChrgeInfo","무료").limit(20)
        query.get().addOnSuccessListener { result ->
            for (document in result) {
                val value = gson.toJson(document.data)
                val result = gson.fromJson(value, Recording::class.java)
                result.museoId  = document.id
                items2.add(result)
            }
            hAdapter.data.put("무료 박물관/미술관", items2)
            hAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Log.w(ContentValues.TAG, "Error getting documents.", exception)
        }
    }

    private fun biologyItem() {
        items4.clear()
        val query = db.collection("museoInfo").where(
            Filter.or(
                Filter.equalTo("fcltyNm", "고성공룡박물관"),
                Filter.equalTo("fcltyNm", "국립해양생물자원관 씨큐리움"),
                Filter.equalTo("fcltyNm", "땅끝해양자연사박물관"),
                Filter.equalTo("fcltyNm", "양평곤충박물관"),
                Filter.equalTo("fcltyNm", "영월곤충박물관"),
                Filter.equalTo("fcltyNm", "울산해양박물관"),
                Filter.equalTo("fcltyNm", "제주해양동물박물관"),
                Filter.equalTo("fcltyNm", "한국자연사박물관"),
                Filter.equalTo("fcltyNm", "한남대학교 자연사박물관"),
            )
        )
        query.get().addOnSuccessListener { result ->
            for (document in result) {
                val value = gson.toJson(document.data)
                val result = gson.fromJson(value, Recording::class.java)
                result.museoId  = document.id
                items4.add(result)
            }
            hAdapter.data.put("생물 박물관", items4)
            hAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Log.w(ContentValues.TAG, "Error getting documents.", exception)
        }
    }

    private fun scienceItem() {
        items5.clear()
        val query = db.collection("museoInfo").where(
            Filter.or(
                Filter.equalTo("fcltyNm", "경상북도산림과학박물관"),
                Filter.equalTo("fcltyNm", "경주세계자동차박물관"),
                Filter.equalTo("fcltyNm", "넥슨컴퓨터박물관"),
                Filter.equalTo("fcltyNm", "에디슨과학박물관"),
                Filter.equalTo("fcltyNm", "제주항공우주박물관"),
                Filter.equalTo("fcltyNm", "제주해양과학관 (아쿠아플라넷)"),
                Filter.equalTo("fcltyNm", "충주고구려천문과학관"),
                Filter.equalTo("fcltyNm", "한국지질자원연구원 지질박물관"),
            )
        )
        query.get().addOnSuccessListener { result ->
            for (document in result) {
                val value = gson.toJson(document.data)
                val result = gson.fromJson(value, Recording::class.java)
                result.museoId  = document.id
                items5.add(result)
            }
            hAdapter.data.put("과학 박물관", items5)
            hAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Log.w(ContentValues.TAG, "Error getting documents.", exception)
        }
    }

    private fun popularItem() {
        items.clear()
        val query = db.collection("museoInfo").where(
            Filter.or(
                Filter.equalTo("fcltyNm", "국립경주박물관"),
                Filter.equalTo("fcltyNm", "국립고궁박물관"),
                Filter.equalTo("fcltyNm", "국립민속박물관"),
                Filter.equalTo("fcltyNm", "국립중앙박물관"),
                Filter.equalTo("fcltyNm", "대림미술관"),
                Filter.equalTo("fcltyNm", "리움미술관"),
                Filter.equalTo("fcltyNm", "부산시립박물관"),
                Filter.equalTo("fcltyNm", "서울시립미술관"),
                Filter.equalTo("fcltyNm", "제주유리박물관"),
                Filter.equalTo("fcltyNm", "미메시스아트뮤지엄"),
            )
        )
        query.get().addOnSuccessListener { result ->
            for (document in result) {
                val value = gson.toJson(document.data)
                val result = gson.fromJson(value, Recording::class.java)
                Log.d("asd",document.id)
                result.museoId  = document.id
                items.add(result)
            }
            hAdapter.data.put("인기", items)
            hAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Log.w(ContentValues.TAG, "Error getting documents.", exception)
        }
    }

    private fun uniqueItem() {
        items3.clear()
        val query = db.collection("museoInfo").where(
            Filter.or(
                Filter.equalTo("fcltyNm", "헬로키티아일랜드"),
                Filter.equalTo("fcltyNm", "한국영화박물관"),
                Filter.equalTo("fcltyNm", "한국만화박물관"),
                Filter.equalTo("fcltyNm", "대한민국술테마박물관"),
                Filter.equalTo("fcltyNm", "그리스신화박물관"),
                Filter.equalTo("fcltyNm", "무비인더박스"),
                Filter.equalTo("fcltyNm", "한국대중음악박물관"),
                Filter.equalTo("fcltyNm", "제주유리박물관"),
                Filter.equalTo("fcltyNm", "박물관은살아있다"),
            )
        )
        query.get().addOnSuccessListener { result ->
            for (document in result) {
                val value = gson.toJson(document.data)
                val result = gson.fromJson(value, Recording::class.java)
                result.museoId  = document.id
                items3.add(result)
            }
            hAdapter.data.put("이색 박물관", items3)
            hAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Log.w(ContentValues.TAG, "Error getting documents.", exception)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeSetting()
        freeItem()
        popularItem()
        uniqueItem()
        biologyItem()
        scienceItem()
    }
}