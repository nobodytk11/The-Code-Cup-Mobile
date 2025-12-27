package com.example.thecodecup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CoffeeAdapter(
    private val coffeeList: List<Coffee>,
    private val onItemClick: (Coffee) -> Unit
) : RecyclerView.Adapter<CoffeeAdapter.CoffeeViewHolder>() {

    class CoffeeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.coffeeImage)
        val name: TextView = view.findViewById(R.id.coffeeName)
        val rating: TextView = view.findViewById(R.id.coffeeRating)
        val ratingCount: TextView = view.findViewById(R.id.coffeeRatingCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coffee, parent, false)
        return CoffeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoffeeViewHolder, position: Int) {
        val coffee = coffeeList[position]
        holder.name.text = coffee.name
        holder.image.setImageResource(coffee.imageResId)
        
        // Load real-time rating data
        val ratingData = RatingManager.getRating(coffee.name)
        holder.rating.text = String.format("%.1f", ratingData.average)
        holder.ratingCount.text = "(${ratingData.count}+)"
        
        holder.itemView.setOnClickListener { onItemClick(coffee) }
    }

    override fun getItemCount() = coffeeList.size
}