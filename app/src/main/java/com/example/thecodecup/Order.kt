package com.example.thecodecup

import java.io.Serializable

data class Order(
    val id: String,
    var date: String,
    var timestamp: Long,
    val items: List<CartItem>,
    val totalPrice: Double,
    val address: String,
    val userName: String,
    var status: OrderStatus = OrderStatus.ONGOING
) : Serializable

enum class OrderStatus {
    ONGOING, HISTORY
}