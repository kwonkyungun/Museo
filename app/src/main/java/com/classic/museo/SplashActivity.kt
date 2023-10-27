package com.classic.museo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.classic.museo.Login.LoginActivity

class SplashActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_image)

        var handler= Handler()
        handler.postDelayed({
            val intent= Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        },2000)

    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}