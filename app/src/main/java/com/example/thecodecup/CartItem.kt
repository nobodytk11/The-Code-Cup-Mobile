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
    var vouchersUsedCount: Int = 0 // Track exactly how many vouchers were applied to this item
) : Serializable