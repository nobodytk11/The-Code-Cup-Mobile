package com.example.thecodecup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RewardAdapter(private var history: List<RewardItem>) : RecyclerView.Adapter<RewardAdapter.RewardViewHolder>() {

    class RewardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvRewardName)
        val date: TextView = view.findViewById(R.id.tvRewardDate)
        val points: TextView = view.findViewById(R.id.tvRewardPoints)
    }

    fun updateData(newHistory: List<RewardItem>) {
        history = newHistory
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reward, parent, false)
        return RewardViewHolder(view)
    }

    override fun onBindViewHolder(holder: RewardViewHolder, position: Int) {
        val item = history[position]
        holder.name.text = item.name
        holder.date.text = item.date
        holder.points.text = "+ ${item.points} Pts"
    }

    override fun getItemCount() = history.size
}