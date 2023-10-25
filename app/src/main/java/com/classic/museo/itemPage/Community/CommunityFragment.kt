package com.classic.museo.itemPage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.classic.museo.itemPage.Community.CommunityPlusActivity
import com.classic.museo.data.CommunityDTO
import com.classic.museo.databinding.FragmentCommunityBinding
import com.classic.museo.itemPage.Community.CommunityAdapter
import com.classic.museo.itemPage.Community.CommunityDetailActivity


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
        adapter.usersFirestore()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)
        setupView()

        adapter.itemClick = object : CommunityAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, CommunityDetailActivity::class.java)
                startActivity(intent)
            }
        }

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