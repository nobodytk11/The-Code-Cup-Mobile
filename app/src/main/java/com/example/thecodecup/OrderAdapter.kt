package com.example.thecodecup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
        val btnRate: Button = view.findViewById(R.id.btnRateOrder)
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
        
        // Show rate button only for HISTORY orders that haven't been rated yet
        if (order.status == OrderStatus.HISTORY && !order.isRated) {
            holder.btnRate.visibility = View.VISIBLE
            holder.btnRate.setOnClickListener {
                showRatingDialog(holder.itemView.context, order)
            }
        } else {
            holder.btnRate.visibility = View.GONE
        }
        
        holder.itemView.setOnClickListener { onOrderClick(order) }
    }

    private fun showRatingDialog(context: android.content.Context, order: Order) {
        val ratings = arrayOf("1 Star", "2 Stars", "3 Stars", "4 Stars", "5 Stars")
        AlertDialog.Builder(context)
            .setTitle("Rate your order")
            .setItems(ratings) { _, which ->
                val score = which + 1
                // Add rating for each coffee in the order
                order.items.forEach { cartItem ->
                    RatingManager.addRating(cartItem.coffee.name, score)
                }
                order.isRated = true
                PersistenceManager.saveData()
                notifyDataSetChanged()
            }
            .show()
    }

    override fun getItemCount() = orders.size

    fun updateData(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}