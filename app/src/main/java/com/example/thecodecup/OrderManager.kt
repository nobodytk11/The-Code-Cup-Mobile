package com.example.thecodecup

object OrderManager {
    private const val MAX_ORDERS = 12

    val orders: MutableList<Order>
        get() = UserManager.currentUser?.orders ?: mutableListOf()

    fun addOrder(order: Order) {
        orders.add(0, order)
        orders.sortByDescending { it.timestamp }
        if (orders.size > MAX_ORDERS) {
            orders.removeAt(orders.size - 1)
        }
    }

    fun getOngoingOrders() = orders.filter { it.status == OrderStatus.ONGOING }
        .sortedByDescending { it.timestamp }
        
    fun getHistoryOrders() = orders.filter { it.status == OrderStatus.HISTORY }
        .sortedByDescending { it.timestamp }

    fun completeOrder(orderId: String) {
        orders.find { it.id == orderId }?.status = OrderStatus.HISTORY
    }
}