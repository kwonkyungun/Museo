package com.classic.museo

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets
import android.widget.PopupMenu
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.classic.museo.databinding.ActivityMainBinding
import com.classic.museo.home.HomeFragment
import com.classic.museo.itemPage.MypageFragment
import com.classic.museo.itemPage.search.SearchFragment
import com.classic.museo.itemPage.CommunityFragment
import com.google.android.material.tabs.TabLayoutMediator


//팀 노션 : https://teamsparta.notion.site/3-Museo-72e01c364bd64fa18324fa82dad2b300
//팀 깃허브 : https://github.com/ProjectMuseo/Museo
//팀 피그마 : https://www.figma.com/file/TSceoygyttCTT98BGW6Xyf/Final-Project?type=design&node-id=0-1&mode=design&t=Vg6NUqk8kGv7hxaM-0

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //네이게이션 컨트롤러
        val fragment = supportFragmentManager.findFragmentById(R.id.main_fragment) as NavHostFragment
        navController = fragment.navController
        setupSmoothBottomMenu()

    }

    //네비게이션 옵션 추가
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    //바텀 네비게이션 기능
    @SuppressLint("ResourceType")
    private fun setupSmoothBottomMenu() {
        val menu = PopupMenu(this,null).apply { inflate(binding.bottomBar.itemMenuRes) }
        binding.bottomBar.setupWithNavController(menu.menu, navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()

    }
}