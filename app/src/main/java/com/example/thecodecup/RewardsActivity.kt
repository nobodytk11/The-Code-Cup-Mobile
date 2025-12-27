package com.example.thecodecup

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RewardsActivity : AppCompatActivity() {

    private lateinit var tvRewardsStampCount: TextView
    private lateinit var tvTotalPoints: TextView
    private lateinit var rCups: List<ImageView>
    private lateinit var adapter: RewardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewards)

        tvRewardsStampCount = findViewById(R.id.tvRewardsStampCount)
        tvTotalPoints = findViewById(R.id.tvTotalPoints)
        
        rCups = listOf(
            findViewById(R.id.rCup1), findViewById(R.id.rCup2),
            findViewById(R.id.rCup3), findViewById(R.id.rCup4),
            findViewById(R.id.rCup5), findViewById(R.id.rCup6),
            findViewById(R.id.rCup7), findViewById(R.id.rCup8)
        )

        val rvRewards = findViewById<RecyclerView>(R.id.rvRewards)
        rvRewards.layoutManager = LinearLayoutManager(this)
        adapter = RewardAdapter(RewardManager.getSortedHistory())
        rvRewards.adapter = adapter

        findViewById<Button>(R.id.btnRedeemDrinks).setOnClickListener {
            startActivity(Intent(this, RedeemActivity::class.java))
        }

        findViewById<android.view.View>(R.id.btnResetRewardsStamps).setOnClickListener {
            if (RewardManager.stampCount >= 8) {
                RewardManager.resetStamps()
                updateUI()
                Toast.makeText(this, "Congrats! Card reset! Enjoy your free coffee!", Toast.LENGTH_SHORT).show()
                PersistenceManager.saveData()
            } else {
                Toast.makeText(this, "Keep drinking! 8 stamps needed for a free coffee.", Toast.LENGTH_SHORT).show()
            }
        }

        setupBottomNav()
        updateUI()
    }

    private fun setupBottomNav() {
        val navHome = findViewById<ImageButton>(R.id.navHome)
        val navRewards = findViewById<ImageButton>(R.id.navRewards)
        val navOrders = findViewById<ImageButton>(R.id.navOrders)

        // Highlight rewards tab
        navRewards.setColorFilter(ContextCompat.getColor(this, R.color.primary_dark))
        navHome.setColorFilter(ContextCompat.getColor(this, R.color.nav_inactive))
        navOrders.setColorFilter(ContextCompat.getColor(this, R.color.nav_inactive))

        navHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
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

    override fun onResume() {
        super.onResume()
        updateUI()
        setupBottomNav()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }

    private fun updateUI() {
        tvRewardsStampCount.text = "${RewardManager.stampCount} / 8"
        tvTotalPoints.text = RewardManager.totalPoints.toString()

        // Update Reward History list
        adapter.updateData(RewardManager.getSortedHistory())

        // Logic: If count >= 8, all 8 cups are solid. Otherwise, show count.
        val visibleActiveStamps = if (RewardManager.stampCount >= 8) 8 else RewardManager.stampCount

        for (i in 0 until 8) {
            if (i < visibleActiveStamps) {
                rCups[i].alpha = 1.0f // Colored
            } else {
                rCups[i].alpha = 0.1f // Faded
            }
        }
    }
}