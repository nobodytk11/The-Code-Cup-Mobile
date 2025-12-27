package com.example.thecodecup

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etName = findViewById<EditText>(R.id.etRegisterName)
        val etEmail = findViewById<EditText>(R.id.etRegisterEmail)
        val etPhone = findViewById<EditText>(R.id.etRegisterPhone)
        val etPassword = findViewById<EditText>(R.id.etRegisterPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        btnRegister.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val phone = etPhone.text.toString()
            val password = etPassword.text.toString()

            if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                if (UserManager.register(name, phone, email, password)) {
                    PersistenceManager.saveData()
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Email is already registered!", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}