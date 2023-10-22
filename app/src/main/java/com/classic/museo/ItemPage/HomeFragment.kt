
package com.classic.museo.ItemPage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.classic.museo.data.Record
import com.classic.museo.databinding.FragmentHomeBinding
import com.example.museoapitest.retrofit.NetWorkClient
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding

    private lateinit var database : DatabaseReference
    private var items= mutableListOf<Record>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            communicateNetWork(setUpMuseumParameter())
        }
    }

    private fun setUpMuseumParameter(): HashMap<String, String> {
        val authKey = "xGp/squZ8ZOC7m8iSZFc6sAlwRJrKkkPezlcdCwezR4DCKzM2hn4amec4CSXCMQYYU1hGPnKjUAndRsjgLUrZQ=="
        return hashMapOf(
            "serviceKey" to authKey,
            "pageNo" to "1",
            "numOfRows" to "2293",
            "type" to "json",
        )
    }

    private fun communicateNetWork(param: HashMap<String, String>) = lifecycleScope.launch() {
        val responseData = NetWorkClient.museoNetWork.getMuseo(param)
        Log.d("Parsing Museo ::", responseData.toString())

        database= Firebase.database.reference
        items=responseData.response.museoBody.museoItem!!

        items.forEach {
            database.child("museumInfo").child("지역별").child("전국").child(it.fcltyNm.replace('.','-')).setValue(it)
        }
        Log.e("dbTest","Success")
    }
}