package com.classic.museo.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.classic.museo.R
import com.classic.museo.databinding.FragmentHomeBinding
import com.example.museoapitest.retrofit.NetWorkClient
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
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
        val authKey = "0OhBU7ZCGIobDVKDeBJDpmDRqK3IRNF6jlf/JB2diFAf/fR2czYO9A4UTGcsOwppV6W2HVUeho/FPwXoL6DwqA=="

        return hashMapOf(
            "serviceKey" to authKey,
            "pageNo" to "1",
            "numOfRows" to "100",
            "type" to "json",
        )
    }

    private fun communicateNetWork(param: HashMap<String, String>) = lifecycleScope.launch() {
        val responseData = NetWorkClient.museoNetWork.getMuseo(param)
        Log.d("Parsing Museo ::", responseData.toString())
    }
}