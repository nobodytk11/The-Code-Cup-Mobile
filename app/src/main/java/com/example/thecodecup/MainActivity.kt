package com.example.thecodecup

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var tvHomeUserName: TextView
    private lateinit var tvHomeStampCount: TextView
    private lateinit var hCups: List<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvHomeUserName = findViewById(R.id.tvHomeUserName)
        tvHomeStampCount = findViewById(R.id.tvHomeStampCount)
        
        hCups = listOf(
            findViewById(R.id.hCup1), findViewById(R.id.hCup2),
            findViewById(R.id.hCup3), findViewById(R.id.hCup4),
            findViewById(R.id.hCup5), findViewById(R.id.hCup6),
            findViewById(R.id.hCup7), findViewById(R.id.hCup8)
        )

        val recyclerView = findViewById<RecyclerView>(R.id.coffeeRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val coffeeList = listOf(
            Coffee(1, "Americano", "Single shot of espresso with hot water", 3.0, R.drawable.img_americano),
            Coffee(2, "Cappuccino", "Espresso with steamed milk and foam", 3.5, R.drawable.img_cappuccino),
            Coffee(3, "Mocha", "Espresso with chocolate and steamed milk", 4.0, R.drawable.img_mocha),
            Coffee(4, "Flat White", "Espresso with microfoam", 3.7, R.drawable.img_flat_white)
        )

        val adapter = CoffeeAdapter(coffeeList) { coffee ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("coffee", coffee)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        findViewById<ImageButton>(R.id.btnProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnCart).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        findViewById<android.view.View>(R.id.btnResetHomeStamps).setOnClickListener {
            if (RewardManager.stampCount >= 8) {
                RewardManager.resetStamps()
                updateLoyaltyCard()
                Toast.makeText(this, "Congrats! Card reset! Enjoy your free coffee!", Toast.LENGTH_SHORT).show()
                PersistenceManager.saveData()
            } else {
                Toast.makeText(this, "Keep drinking! 8 stamps needed for a free coffee.", Toast.LENGTH_SHORT).show()
            }
        }

        setupBottomNav()
    }

    private fun setupBottomNav() {
        val navHome = findViewById<ImageButton>(R.id.navHome)
        val navRewards = findViewById<ImageButton>(R.id.navRewards)
        val navOrders = findViewById<ImageButton>(R.id.navOrders)

        navHome.setColorFilter(ContextCompat.getColor(this, R.color.primary_dark))
        navRewards.setColorFilter(ContextCompat.getColor(this, R.color.nav_inactive))
        navOrders.setColorFilter(ContextCompat.getColor(this, R.color.nav_inactive))

        navRewards.setOnClickListener {
            val intent = Intent(this, RewardsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        navOrders.setOnClickListener {
            val intent = Intent(this, OrdersActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }

    private fun updateLoyaltyCard() {
        tvHomeStampCount.text = "${RewardManager.stampCount} / 8"
        
        // Simplified logic: If count >= 8, all 8 cups are solid. Otherwise, show count.
        val visibleActiveStamps = if (RewardManager.stampCount >= 8) 8 else RewardManager.stampCount

        for (i in 0 until 8) {
            if (i < visibleActiveStamps) {
                hCups[i].alpha = 1.0f // Solid
            } else {
                hCups[i].alpha = 0.1f // Faded
            }
        }
    }

    override fun onResume() {
        super.onResume()
        tvHomeUserName.text = UserManager.fullName
        updateLoyaltyCard()
        setupBottomNav()
    }
    
    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}