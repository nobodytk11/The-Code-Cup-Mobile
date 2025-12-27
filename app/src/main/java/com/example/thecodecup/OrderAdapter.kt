package com.example.thecodecup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(
    private var orders: List<Order>,
    private val onOrderClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.findViewById(R.id.tvOrderDate)
        val price: TextView = view.findViewById(R.id.tvOrderPrice)
        val items: TextView = view.findViewById(R.id.tvOrderItems)
        val address: TextView = view.findViewById(R.id.tvOrderAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.date.text = order.date
        holder.price.text = "$${String.format("%.2f", order.totalPrice)}"
        holder.items.text = order.items.joinToString(", ") { it.coffee.name }
        holder.address.text = order.address
        
        holder.itemView.setOnClickListener { onOrderClick(order) }
    }

    override fun getItemCount() = orders.size

    fun updateData(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}