package com.classic.museo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.classic.museo.Login.LoginActivity
import com.classic.museo.databinding.ActivityMainBinding
import com.classic.museo.home.HomeFragment
import com.classic.museo.itemPage.MypageFragment
import com.classic.museo.itemPage.search.SearchFragment
import com.classic.museo.itemPage.CommunityFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient


//팀 노션 : https://teamsparta.notion.site/3-Museo-72e01c364bd64fa18324fa82dad2b300
//팀 깃허브 : https://github.com/ProjectMuseo/Museo
//팀 피그마 : https://www.figma.com/file/TSceoygyttCTT98BGW6Xyf/Final-Project?type=design&node-id=0-1&mode=design&t=Vg6NUqk8kGv7hxaM-0

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var auth : FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth= Firebase.auth

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

    override fun onDestroy() {
        super.onDestroy()
        if(auth!!.uid!=null){
            FirebaseAuth.getInstance().signOut()
            Log.w(TAG,"파이어베이스 로그아웃 완료")
        }
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            }
            else {
                Log.i(ContentValues.TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
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