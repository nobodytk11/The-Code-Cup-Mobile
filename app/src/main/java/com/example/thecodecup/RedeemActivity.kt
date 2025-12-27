package com.example.thecodecup

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RedeemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redeem)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val rvRedeemItems = findViewById<RecyclerView>(R.id.rvRedeemItems)
        rvRedeemItems.layoutManager = LinearLayoutManager(this)

        val redeemableList = listOf(
            Coffee(1, "Americano", "", 0.0, R.drawable.img_americano),
            Coffee(2, "Flat White", "", 0.0, R.drawable.img_flat_white),
            Coffee(3, "Cappuccino", "", 0.0, R.drawable.img_cappuccino),
            Coffee(4, "Mocha", "", 0.0, R.drawable.img_mocha)
        )

        rvRedeemItems.adapter = RedeemAdapter(redeemableList) { coffee, points ->
            if (RewardManager.redeemProduct(points)) {
                PersistenceManager.saveData() // Persist points change
                Toast.makeText(this, "${coffee.name} redeemed successfully!", Toast.LENGTH_SHORT).show()
                finish() // Go back to rewards screen to see updated points
            } else {
                Toast.makeText(this, "Not enough points!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}