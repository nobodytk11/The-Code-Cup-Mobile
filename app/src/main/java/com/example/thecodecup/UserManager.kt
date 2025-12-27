package com.example.thecodecup

object UserManager {
    var fullName: String = ""
    var phoneNumber: String = ""
    var email: String = ""
    var address: String = ""
    var password: String = ""
    var isLoggedIn: Boolean = false
    var availableVouchers: Int = 0

    // Simple user storage for this project (one user at a time for simplicity)
    fun register(name: String, phone: String, mail: String, pass: String) {
        fullName = name
        phoneNumber = phone
        email = mail
        password = pass
        isLoggedIn = true
    }

    fun login(mail: String, pass: String): Boolean {
        return if (email == mail && password == pass) {
            isLoggedIn = true
            true
        } else {
            false
        }
    }

    fun logout() {
        isLoggedIn = false
    }
}