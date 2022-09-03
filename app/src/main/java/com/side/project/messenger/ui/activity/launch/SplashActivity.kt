package com.side.project.messenger.ui.activity.launch

import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.side.project.messenger.ui.activity.BaseActivity

class SplashActivity: BaseActivity() {
    private lateinit var splashScreen: SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen = installSplashScreen()

        // 跳頁
        splashScreen.setKeepOnScreenCondition{ true }
        start(SignInActivity::class.java, true)
    }
}