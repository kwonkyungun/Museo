package com.classic.museo.Fragment

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.classic.museo.CommunityAdapter
import com.classic.museo.CommunityPlusActivity
import com.classic.museo.R
import com.classic.museo.data.CommunityDTO
import com.classic.museo.databinding.FragmentCommunityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CommunityFragment : Fragment() {

    private lateinit var binding: FragmentCommunityBinding
    private lateinit var communityContext: Context
    private lateinit var adapter: CommunityAdapter
    private lateinit var gridLayoutManager: StaggeredGridLayoutManager

    private val list: List<CommunityDTO> = listOf()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        communityContext = context
    }

    override fun onResume() {
        super.onResume()

        adapter.postFirestore()
//        adapter.usersFirestore()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)
        setupView()

        // +버튼 클릭시 새글추가 Activity로 이동
        binding.communityBtnPlus.setOnClickListener {
            val intent = Intent(context, CommunityPlusActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun setupView() {
        gridLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView2.layoutManager = gridLayoutManager
        adapter = CommunityAdapter(communityContext)
        binding.recyclerView2.adapter = adapter
        binding.recyclerView2.itemAnimator = null
    }

}