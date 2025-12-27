package com.example.thecodecup

object RatingManager {
    // Simulated global coffee store
    private val coffeeData = mutableMapOf<String, CoffeeRating>()

    init {
        // Initial simulated ratings matching Home screen
        coffeeData["Americano"] = CoffeeRating(4.5, 120)
        coffeeData["Cappuccino"] = CoffeeRating(4.8, 85)
        coffeeData["Mocha"] = CoffeeRating(4.6, 92)
        coffeeData["Flat White"] = CoffeeRating(4.7, 74)
    }

    fun getRating(name: String): CoffeeRating {
        return coffeeData[name] ?: CoffeeRating(0.0, 0)
    }

    fun addRating(name: String, newStars: Int) {
        val current = getRating(name)
        val newTotalCount = current.count + 1
        val newAvg = ((current.average * current.count) + newStars) / newTotalCount
        coffeeData[name] = CoffeeRating(newAvg, newTotalCount)
    }
}

data class CoffeeRating(val average: Double, val count: Int)