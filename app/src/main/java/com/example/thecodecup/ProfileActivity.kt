package com.example.thecodecup

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvFullName: TextView
    private lateinit var tvPhoneNumber: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvAddress: TextView
    
    private lateinit var btnEditName: ImageButton
    private lateinit var btnEditPhone: ImageButton
    private lateinit var btnEditEmail: ImageButton
    private lateinit var btnEditAddress: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tvFullName = findViewById(R.id.tvFullName)
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber)
        tvEmail = findViewById(R.id.tvEmail)
        tvAddress = findViewById(R.id.tvAddress)
        
        btnEditName = findViewById(R.id.btnEditName)
        btnEditPhone = findViewById(R.id.btnEditPhone)
        btnEditEmail = findViewById(R.id.btnEditEmail)
        btnEditAddress = findViewById(R.id.btnEditAddress)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        setupEditButtons()
        updateUI()
    }

    private fun setupEditButtons() {
        btnEditName.setOnClickListener {
            showEditDialog("Full Name", UserManager.fullName) { UserManager.fullName = it; onDataChanged() }
        }
        btnEditPhone.setOnClickListener {
            showEditDialog("Phone Number", UserManager.phoneNumber) { UserManager.phoneNumber = it; onDataChanged() }
        }
        btnEditEmail.setOnClickListener {
            showEditDialog("Email", UserManager.email) { UserManager.email = it; onDataChanged() }
        }
        btnEditAddress.setOnClickListener {
            showEditDialog("Address", UserManager.address) { UserManager.address = it; onDataChanged() }
        }
    }

    private fun showEditDialog(title: String, currentValue: String, onSave: (String) -> Unit) {
        val input = EditText(this)
        input.setText(currentValue)
        // Set cursor to the end of the text
        if (currentValue.isNotEmpty()) {
            input.setSelection(currentValue.length)
        }
        input.setPadding(64, 32, 64, 32)
        
        AlertDialog.Builder(this)
            .setTitle("Edit $title")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val newValue = input.text.toString()
                if (newValue.isNotBlank()) {
                    onSave(newValue)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun onDataChanged() {
        updateUI()
        PersistenceManager.saveData()
    }

    private fun updateUI() {
        tvFullName.text = UserManager.fullName
        tvPhoneNumber.text = UserManager.phoneNumber
        tvEmail.text = UserManager.email
        tvAddress.text = UserManager.address
    }
}