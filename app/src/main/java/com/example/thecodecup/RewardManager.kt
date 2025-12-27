package com.example.thecodecup

object RewardManager {
    var stampCount = 4
    var totalPoints = 2750
    private const val MAX_REWARDS = 12
    
    val rewardHistory = mutableListOf(
        RewardItem("Americano", "24 June | 12:30 PM", 12, 1624537800000L),
        RewardItem("Cafe Latte", "22 June | 08:30 AM", 12, 1624350600000L),
        RewardItem("Green Tea Latte", "16 June | 10:48 AM", 12, 1623840480000L),
        RewardItem("Flat White", "12 May | 11:25 AM", 12, 1620818700000L)
    )

    fun addStamps(count: Int) {
        stampCount += count
    }

    fun resetStamps(): Boolean {
        return if (stampCount >= 8) {
            stampCount -= 8
            UserManager.availableVouchers++
            true
        } else {
            false
        }
    }

    fun addPoints(points: Int, itemName: String, date: String) {
        totalPoints += points
        rewardHistory.add(0, RewardItem(itemName, date, points, System.currentTimeMillis()))
        
        if (rewardHistory.size > MAX_REWARDS) {
            rewardHistory.removeAt(rewardHistory.size - 1)
        }
    }

    fun getSortedHistory(): List<RewardItem> {
        return rewardHistory.sortedByDescending { it.timestamp }
    }

    fun redeemProduct(pointsNeeded: Int): Boolean {
        return if (totalPoints >= pointsNeeded) {
            totalPoints -= pointsNeeded
            true
        } else {
            false
        }
    }
}

data class RewardItem(
    val name: String,
    val date: String,
    val points: Int,
    val timestamp: Long
)