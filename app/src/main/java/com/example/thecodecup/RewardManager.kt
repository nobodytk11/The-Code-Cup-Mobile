package com.example.thecodecup

import java.io.Serializable

data class RewardItem(
    val name: String,
    val date: String,
    val points: Int,
    val timestamp: Long
) : Serializable

object RewardManager {
    private const val MAX_REWARDS = 12

    var stampCount: Int
        get() = UserManager.currentUser?.stampCount ?: 0
        set(value) { UserManager.currentUser?.stampCount = value }

    var totalPoints: Int
        get() = UserManager.currentUser?.totalPoints ?: 0
        set(value) { UserManager.currentUser?.totalPoints = value }

    val rewardHistory: MutableList<RewardItem>
        get() = UserManager.currentUser?.rewardHistory ?: mutableListOf()

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

    fun addPoints(points: Int, itemName: String, date: String, timestamp: Long) {
        totalPoints += points
        rewardHistory.add(RewardItem(itemName, date, points, timestamp))
        rewardHistory.sortByDescending { it.timestamp }
        if (rewardHistory.size > MAX_REWARDS) {
            rewardHistory.removeAt(rewardHistory.size - 1)
        }
    }

    fun getSortedHistory(): List<RewardItem> {
        // Now just returns the list because it is guaranteed to be sorted on add
        return rewardHistory
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