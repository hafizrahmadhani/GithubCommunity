package com.example.github

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val splashDuration = 4000
        Handler(Looper.getMainLooper()).postDelayed({
            val splashIntent = Intent(this@SplashScreen, MainActivity::class.java)
            this@SplashScreen.startActivity(splashIntent)
            finish()
        }, splashDuration.toLong())
    }
}
