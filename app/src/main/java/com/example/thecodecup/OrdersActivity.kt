package com.example.thecodecup

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

class OrdersActivity : AppCompatActivity() {

    private lateinit var adapter: OrderAdapter
    private lateinit var rvOrders: RecyclerView
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        rvOrders = findViewById(R.id.rvOrders)
        tabLayout = findViewById(R.id.tabLayout)

        adapter = OrderAdapter(OrderManager.getOngoingOrders()) { order ->
            if (order.status == OrderStatus.ONGOING) {
                OrderManager.completeOrder(order.id)
                RewardManager.addStamps(1)
                val pointsEarned = (order.totalPrice * 4).toInt()
                val itemNames = order.items.joinToString(", ") { it.coffee.name }
                // Pass the order's ORIGINAL timestamp to the RewardManager
                RewardManager.addPoints(pointsEarned, itemNames, order.date, order.timestamp)
                
                PersistenceManager.saveData()
                
                refreshList(tabLayout.selectedTabPosition)
            }
        }
        rvOrders.layoutManager = LinearLayoutManager(this)
        rvOrders.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                refreshList(tab?.position ?: 0)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        setupBottomNav()
    }

    private fun setupBottomNav() {
        val navHome = findViewById<ImageButton>(R.id.navHome)
        val navRewards = findViewById<ImageButton>(R.id.navRewards)
        val navOrders = findViewById<ImageButton>(R.id.navOrders)

        navOrders.setColorFilter(ContextCompat.getColor(this, R.color.primary_dark))
        navHome.setColorFilter(ContextCompat.getColor(this, R.color.nav_inactive))
        navRewards.setColorFilter(ContextCompat.getColor(this, R.color.nav_inactive))

        navHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        navRewards.setOnClickListener {
            val intent = Intent(this, RewardsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }

    private fun refreshList(tabIndex: Int) {
        val filteredList = if (tabIndex == 0) {
            OrderManager.getOngoingOrders()
        } else {
            OrderManager.getHistoryOrders()
        }
        adapter.updateData(filteredList)
    }

    override fun onResume() {
        super.onResume()
        setupBottomNav()
        refreshList(tabLayout.selectedTabPosition)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}