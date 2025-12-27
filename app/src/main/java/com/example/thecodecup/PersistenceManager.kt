package com.example.thecodecup

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.concurrent.Executors

object PersistenceManager {
    private const val PREF_NAME = "TheCodeCupPrefs"
    private lateinit var prefs: SharedPreferences
    private val gson = Gson()
    private val executor = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())

    fun init(context: Context, onComplete: () -> Unit = {}) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        // Load data in background to prevent UI freeze
        executor.execute {
            loadData()
            mainHandler.post { onComplete() }
        }
    }

    private fun loadData() {
        val isFirstRun = prefs.getBoolean("is_first_run", true)

        if (isFirstRun) {
            UserManager.register("Anderson", "+60134589525", "Anderson@email.com", "1234")
            UserManager.isLoggedIn = false
            UserManager.availableVouchers = 0
            prefs.edit().putBoolean("is_first_run", false).apply()
            saveDataInternal() // Sync save for the very first run is okay
        } else {
            UserManager.fullName = prefs.getString("user_name", "Anderson") ?: "Anderson"
            UserManager.phoneNumber = prefs.getString("user_phone", "+60134589525") ?: "+60134589525"
            UserManager.email = prefs.getString("user_email", "Anderson@email.com") ?: "Anderson@email.com"
            UserManager.address = prefs.getString("user_address", "3 Addersion Court Chino Hills, HO56824, United State") ?: "3 Addersion Court Chino Hills, HO56824, United State"
            UserManager.password = prefs.getString("user_password", "1234") ?: "1234"
            UserManager.isLoggedIn = prefs.getBoolean("is_logged_in", false)
            UserManager.availableVouchers = prefs.getInt("user_vouchers", 0)

            RewardManager.stampCount = prefs.getInt("reward_stamps", 0)
            RewardManager.totalPoints = prefs.getInt("reward_points", 0)
            
            // Background JSON parsing
            prefs.getString("reward_history", null)?.let {
                if (it.isNotBlank()) {
                    val type = object : TypeToken<MutableList<RewardItem>>() {}.type
                    val loaded: MutableList<RewardItem> = gson.fromJson(it, type)
                    RewardManager.rewardHistory.clear()
                    RewardManager.rewardHistory.addAll(loaded)
                }
            }

            prefs.getString("orders", null)?.let {
                if (it.isNotBlank()) {
                    val type = object : TypeToken<MutableList<Order>>() {}.type
                    val loaded: MutableList<Order> = gson.fromJson(it, type)
                    OrderManager.orders.clear()
                    OrderManager.orders.addAll(loaded)
                }
            }

            prefs.getString("cart_items", null)?.let {
                if (it.isNotBlank()) {
                    val type = object : TypeToken<MutableList<CartItem>>() {}.type
                    val loaded: MutableList<CartItem> = gson.fromJson(it, type)
                    CartManager.items.clear()
                    CartManager.items.addAll(loaded)
                }
            }
        }
    }

    fun saveData() {
        // Always save in background thread
        executor.execute {
            saveDataInternal()
        }
    }

    private fun saveDataInternal() {
        prefs.edit().apply {
            putString("user_name", UserManager.fullName)
            putString("user_phone", UserManager.phoneNumber)
            putString("user_email", UserManager.email)
            putString("user_address", UserManager.address)
            putString("user_password", UserManager.password)
            putBoolean("is_logged_in", UserManager.isLoggedIn)
            putInt("user_vouchers", UserManager.availableVouchers)
            putInt("reward_stamps", RewardManager.stampCount)
            putInt("reward_points", RewardManager.totalPoints)
            putString("reward_history", gson.toJson(RewardManager.rewardHistory))
            putString("orders", gson.toJson(OrderManager.orders))
            putString("cart_items", gson.toJson(CartManager.items))
            apply()
        }
    }
}