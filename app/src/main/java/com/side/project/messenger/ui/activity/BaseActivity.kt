package com.side.project.messenger.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    lateinit var mActivity: BaseActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level <= TRIM_MEMORY_BACKGROUND)
            System.gc()
    }

    fun start(next: Class<*>, bundle: Bundle?, finished: Boolean) {
        Intent(
            applicationContext,
            next
        ).also { intent ->
            // 傳值
            if (bundle == null)
                intent.putExtras(Bundle())
            else
                intent.putExtras(bundle)
            // 跳頁
            startActivity(intent)
            // 關閉
            if (finished)
                this.finish()
        }
    }

    fun start(next: Class<*>) {
        this.start(next, null, false)
    }

    fun start(next: Class<*>, bundle: Bundle?) {
        this.start(next, bundle, false)
    }

    fun start(next: Class<*>, finished: Boolean) {
        this.start(next, null, finished)
    }
}