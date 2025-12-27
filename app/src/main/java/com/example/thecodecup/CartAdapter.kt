package com.example.thecodecup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private val items: MutableList<CartItem>,
    private val onItemsChanged: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.cartItemImage)
        val name: TextView = view.findViewById(R.id.cartItemName)
        val details: TextView = view.findViewById(R.id.cartItemDetails)
        val quantity: TextView = view.findViewById(R.id.cartItemQuantity)
        val price: TextView = view.findViewById(R.id.cartItemPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.coffee.name
        holder.image.setImageResource(item.coffee.imageResId)
        holder.quantity.text = "x ${item.quantity}"
        holder.price.text = "$${String.format("%.2f", item.totalPrice)}"
        
        val detailsText = "${item.shot.lowercase()} | ${if (item.isIced) "iced" else "hot"} | ${item.size.lowercase()} | ${item.iceLevel.lowercase()} ice"
        holder.details.text = detailsText
    }

    override fun getItemCount() = items.size

    fun removeItem(position: Int) {
        CartManager.items.removeAt(position)
        notifyItemRemoved(position)
        onItemsChanged()
    }
}