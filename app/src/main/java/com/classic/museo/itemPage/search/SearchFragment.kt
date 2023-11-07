package com.classic.museo.itemPage.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.classic.museo.data.Recording
import com.classic.museo.databinding.FragmentSearchBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var sContext: Context
    private var db = Firebase.firestore
    private var gson = GsonBuilder().create()
    private var items = mutableListOf<Recording>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        initViewPager()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sContext = context
    }

    private fun initViewPager() {
        binding.searchViewpager.apply {
            adapter = ViewPagerAdapter(requireActivity())
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }

        TabLayoutMediator(binding.tabLayout, binding.searchViewpager) { tab, position ->
            when (position) {
                0 -> tab.text = "검색"
                1 -> tab.text = "지역별 검색"
            }
        }.attach()
    }


}