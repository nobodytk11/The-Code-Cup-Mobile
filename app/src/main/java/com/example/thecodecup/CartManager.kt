package com.example.thecodecup

object CartManager {
    val items: MutableList<CartItem>
        get() = UserManager.currentUser?.cartItems ?: mutableListOf()

    fun addItem(item: CartItem) {
        items.add(0, item)
    }

    fun getCartCount(): Int = items.sumOf { it.quantity }
    
    fun getTotalCartPrice(): Double = items.sumOf { it.totalPrice }
}