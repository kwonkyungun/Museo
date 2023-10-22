package com.classic.museo

import android.content.ContentValues
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.classic.museo.Fragment.CommunityFragment
import com.classic.museo.home.HomeFragment
import com.classic.museo.Fragment.MypageFragment
import com.classic.museo.Fragment.SearchFragment
import com.classic.museo.data.Record
import com.classic.museo.ItemPage.CommunityFragment
import com.classic.museo.ItemPage.HomeFragment
import com.classic.museo.ItemPage.MypageFragment
import com.classic.museo.ItemPage.SearchFragment
import com.classic.museo.databinding.ActivityMainBinding
import com.example.museoapitest.retrofit.NetWorkClient
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch

//팀 노션 : https://teamsparta.notion.site/3-Museo-72e01c364bd64fa18324fa82dad2b300
//팀 깃허브 : https://github.com/ProjectMuseo/Museo
//팀 피그마 : https://www.figma.com/file/TSceoygyttCTT98BGW6Xyf/Final-Project?type=design&node-id=0-1&mode=design&t=Vg6NUqk8kGv7hxaM-0

class MainActivity : AppCompatActivity() {

    private lateinit var MainContext : Context
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainViewpager.run {
            isUserInputEnabled = false
        }


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
                1 -> tab.setIcon(R.drawable.community).text = "com/classic/museo/ItemPage/Community"
                2 -> tab.setIcon(R.drawable.mypage).text = "Mypage"
                3 -> tab.setIcon(R.drawable.search).text = "Search"
            }
        }.attach()
    }
}