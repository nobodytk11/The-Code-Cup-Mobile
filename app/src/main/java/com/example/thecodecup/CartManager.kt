package com.example.thecodecup

object CartManager {
    val items = mutableListOf<CartItem>()

    fun addItem(item: CartItem) {
        // Add to the beginning of the list so newest added items appear at the top
        items.add(0, item)
    }

    fun getCartCount(): Int = items.sumOf { it.quantity }
    
    fun getTotalCartPrice(): Double = items.sumOf { it.totalPrice }
}