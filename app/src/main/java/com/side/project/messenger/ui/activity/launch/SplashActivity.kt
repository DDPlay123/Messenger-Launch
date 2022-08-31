package com.side.project.messenger.ui.activity.launch

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class SplashActivity: AppCompatActivity() {
    private lateinit var splashScreen: SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen = installSplashScreen()

        // 跳頁
        splashScreen.setKeepOnScreenCondition{ true }
        startActivity(Intent(applicationContext, SignInActivity::class.java))
        finish()
    }
}