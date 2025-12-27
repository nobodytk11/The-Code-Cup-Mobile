package com.example.thecodecup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RedeemAdapter(
    private val items: List<Coffee>,
    private val onRedeemClick: (Coffee, Int) -> Unit
) : RecyclerView.Adapter<RedeemAdapter.RedeemViewHolder>() {

    class RedeemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.ivRedeemImage)
        val name: TextView = view.findViewById(R.id.tvRedeemName)
        val validity: TextView = view.findViewById(R.id.tvRedeemValidity)
        val btnPoints: Button = view.findViewById(R.id.btnRedeemPoints)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RedeemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_redeem, parent, false)
        return RedeemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RedeemViewHolder, position: Int) {
        val coffee = items[position]
        val pointsNeeded = 1340 // Fixed as per design
        
        holder.name.text = coffee.name
        holder.image.setImageResource(coffee.imageResId)
        
        // Calculate current date + 3 days
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 3)
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val formattedDate = sdf.format(calendar.time)
        
        holder.validity.text = "Valid until $formattedDate"
        holder.btnPoints.text = "$pointsNeeded pts"
        
        holder.btnPoints.setOnClickListener {
            onRedeemClick(coffee, pointsNeeded)
        }
    }

    override fun getItemCount() = items.size
}