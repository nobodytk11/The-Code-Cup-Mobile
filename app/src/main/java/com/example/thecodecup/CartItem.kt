package com.example.thecodecup

import java.io.Serializable

data class CartItem(
    val coffee: Coffee,
    var quantity: Int,
    var shot: String,
    var isIced: Boolean,
    var size: String,
    var iceLevel: String,
    var totalPrice: Double,
    var isVoucherUsed: Boolean = false // Track if a voucher was applied to this specific item
) : Serializable