package com.example.thecodecup

object OrderManager {
    val orders = mutableListOf<Order>()
    private const val MAX_ORDERS = 12

    fun addOrder(order: Order) {
        orders.add(0, order)
        // Ensure the entire list is sorted by original checkout time
        orders.sortByDescending { it.timestamp }
        
        // Remove oldest if limit reached
        if (orders.size > MAX_ORDERS) {
            orders.removeAt(orders.size - 1)
        }
    }

    // Always returns orders sorted by original checkout time (newest first)
    fun getOngoingOrders() = orders.filter { it.status == OrderStatus.ONGOING }
        .sortedByDescending { it.timestamp }
        
    fun getHistoryOrders() = orders.filter { it.status == OrderStatus.HISTORY }
        .sortedByDescending { it.timestamp }

    fun completeOrder(orderId: String) {
        // Just change status, preserve the original order time
        orders.find { it.id == orderId }?.status = OrderStatus.HISTORY
    }
}