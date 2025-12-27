package com.example.thecodecup

object UserManager {
    var allUsers = mutableListOf<UserAccount>()
    var currentUser: UserAccount? = null
    var isLoggedIn: Boolean = false

    var fullName: String
        get() = currentUser?.fullName ?: ""
        set(value) { currentUser?.fullName = value }

    var phoneNumber: String
        get() = currentUser?.phoneNumber ?: ""
        set(value) { currentUser?.phoneNumber = value }

    var email: String
        get() = currentUser?.email ?: ""
        set(value) { currentUser?.email = value }

    var address: String
        get() = currentUser?.address ?: ""
        set(value) { currentUser?.address = value }

    var password: String
        get() = currentUser?.password ?: ""
        set(value) { currentUser?.password = value }

    var availableVouchers: Int
        get() = currentUser?.availableVouchers ?: 0
        set(value) { currentUser?.availableVouchers = value }

    var isGuest: Boolean
        get() = currentUser?.isGuest ?: false
        set(value) { currentUser?.isGuest = value }

    fun isEmailRegistered(email: String): Boolean {
        return allUsers.any { it.email.lowercase() == email.lowercase() }
    }

    fun register(name: String, phone: String, mail: String, pass: String): Boolean {
        if (isEmailRegistered(mail)) return false
        
        val newUser = UserAccount(
            fullName = name,
            phoneNumber = phone,
            email = mail,
            password = pass,
            isGuest = false
        )
        allUsers.add(newUser)
        return true
    }

    fun login(mail: String, pass: String): Boolean {
        val user = allUsers.find { it.email.lowercase() == mail.lowercase() && it.password == pass }
        return if (user != null) {
            currentUser = user
            isLoggedIn = true
            return true
        } else {
            false
        }
    }

    fun loginAsGuest() {
        var guest = allUsers.find { it.email == "Anderson@email.com" }
        if (guest == null) {
            guest = UserAccount(
                fullName = "Anderson",
                phoneNumber = "+60134589525",
                email = "Anderson@email.com",
                password = "1234",
                address = "3 Addersion Court Chino Hills, HO56824, United State",
                isGuest = true
            )
            allUsers.add(guest)
        }
        currentUser = guest
        isGuest = true
        isLoggedIn = true
    }

    fun logout() {
        if (currentUser != null) {
            val user = currentUser!!
            user.cartItems.forEach { item ->
                user.availableVouchers += item.vouchersUsedCount
            }
            user.cartItems.clear()
        }
        isLoggedIn = false
        currentUser = null
    }

    fun clearAllData() {
        if (isGuest && currentUser != null) {
            allUsers.remove(currentUser)
        }
        logout()
    }
}