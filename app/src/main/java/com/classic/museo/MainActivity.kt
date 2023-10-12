package com.classic.museo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.classic.museo.Fragment.CommunityFragment
import com.classic.museo.Fragment.HomeFragment
import com.classic.museo.Fragment.MypageFragment
import com.classic.museo.Fragment.SearchFragment
import com.classic.museo.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var MainContext : Context
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewPager() // 뷰페이져 보여주기
    }

    //탭 레이아웃 뷰페이저 추가
    private fun initViewPager() {
        var viewPager2Adatper = ViewPager2Adapter(this)
        viewPager2Adatper.addFragment(HomeFragment())
        viewPager2Adatper.addFragment(CommunityFragment())
        viewPager2Adatper.addFragment(MypageFragment())
        viewPager2Adatper.addFragment(SearchFragment())

        binding.mainViewpager.apply {
            adapter = viewPager2Adatper

        }

        //탭 레이아웃 UI
        TabLayoutMediator(binding.mainTl, binding.mainViewpager) { tab, position ->
            when (position) {
                0 -> tab.setIcon(R.drawable.home).text = "Home"
                1 -> tab.setIcon(R.drawable.community).text = "Community"
                2 -> tab.setIcon(R.drawable.mypage).text = "Mypage"
                3 -> tab.setIcon(R.drawable.search).text = "Search"
            }
        }.attach()
    }
}