package com.example.thecodecup

import java.io.Serializable

data class Coffee(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageResId: Int
) : Serializable