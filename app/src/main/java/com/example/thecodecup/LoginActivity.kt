package com.example.thecodecup

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etLoginEmail)
        val etPassword = findViewById<EditText>(R.id.etLoginPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnGuestLogin = findViewById<Button>(R.id.btnGuestLogin)
        val btnGoToRegister = findViewById<TextView>(R.id.btnGoToRegister)

        setupRegisterText(btnGoToRegister)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (UserManager.login(email, password)) {
                onLoginSuccess()
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

        btnGuestLogin.setOnClickListener {
            UserManager.loginAsGuest()
            RewardManager.totalPoints = 10000
            UserManager.currentUser?.totalPoints = 10000
            onLoginSuccess()
        }

        btnGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setupRegisterText(textView: TextView) {
        val fullText = "New member? Register now"
        val spannable = SpannableString(fullText)
        
        val startIndex = fullText.indexOf("Register now")
        val endIndex = fullText.length
        
        val colorPrimary = ContextCompat.getColor(this, R.color.primary_dark)
        
        spannable.setSpan(ForegroundColorSpan(colorPrimary), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        
        textView.text = spannable
    }

    private fun onLoginSuccess() {
        PersistenceManager.saveData()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}