package com.example.thecodecup

import java.io.Serializable

data class Coffee(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageResId: Int,
    var averageRating: Double = 4.5, // Simulated average rating
    var ratingCount: Int = 100       // Simulated number of ratings
) : Serializable