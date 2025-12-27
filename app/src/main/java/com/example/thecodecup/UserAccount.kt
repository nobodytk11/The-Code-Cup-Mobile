package com.example.thecodecup

import java.io.Serializable

data class UserAccount(
    var fullName: String = "",
    var phoneNumber: String = "",
    var email: String = "",
    var address: String = "",
    var password: String = "",
    var availableVouchers: Int = 0,
    var orders: MutableList<Order> = mutableListOf(),
    var rewardHistory: MutableList<RewardItem> = mutableListOf(),
    var cartItems: MutableList<CartItem> = mutableListOf(),
    var stampCount: Int = 0,
    var totalPoints: Int = 0,
    var isGuest: Boolean = false
) : Serializable