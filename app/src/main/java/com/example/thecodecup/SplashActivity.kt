package com.example.thecodecup

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        checkDataAndNavigate()
    }

    private fun checkDataAndNavigate() {
        val app = application as TheCodeCupApp
        if (app.isDataLoaded) {
            // Data is ready, wait a minimum time then go
            handler.postDelayed({
                navigate()
            }, 1000)
        } else {
            // Data not ready yet, check again in 100ms
            handler.postDelayed({
                checkDataAndNavigate()
            }, 100)
        }
    }

    private fun navigate() {
        if (isFinishing) return
        
        val intent = if (UserManager.isLoggedIn) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }
}