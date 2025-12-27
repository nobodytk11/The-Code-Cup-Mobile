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
    private val lock = Any()
    
    private val saveHandler = Handler(Looper.getMainLooper())
    private val saveRunnable = Runnable { performSave() }

    fun init(context: Context, onComplete: () -> Unit = {}) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        executor.execute {
            try {
                synchronized(lock) {
                    loadData()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                mainHandler.post { onComplete() }
            }
        }
    }

    private fun loadData() {
        val isFirstRun = prefs.getBoolean("is_first_run", true)

        if (isFirstRun) {
            UserManager.loginAsGuest()
            UserManager.isLoggedIn = false
            UserManager.availableVouchers = 0
            prefs.edit().putBoolean("is_first_run", false).apply()
            saveDataInternal()
        } else {
            val usersJson = prefs.getString("all_users", null)
            if (!usersJson.isNullOrBlank()) {
                val type = object : TypeToken<MutableList<UserAccount>>() {}.type
                val loadedUsers: MutableList<UserAccount> = gson.fromJson(usersJson, type)
                UserManager.allUsers.clear()
                UserManager.allUsers.addAll(loadedUsers)
            }

            val currentEmail = prefs.getString("current_user_email", null)
            UserManager.isLoggedIn = prefs.getBoolean("is_logged_in", false)
            
            if (UserManager.isLoggedIn && currentEmail != null) {
                UserManager.currentUser = UserManager.allUsers.find { it.email == currentEmail }
            }
        }
    }

    fun saveData() {
        saveHandler.removeCallbacks(saveRunnable)
        saveHandler.postDelayed(saveRunnable, 500)
    }

    private fun performSave() {
        executor.execute {
            synchronized(lock) {
                try {
                    saveDataInternal()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun saveDataInternal() {
        val usersSnapshot = ArrayList(UserManager.allUsers)
        val currentEmail = UserManager.currentUser?.email
        val loggedIn = UserManager.isLoggedIn

        prefs.edit()
            .putString("all_users", gson.toJson(usersSnapshot))
            .putString("current_user_email", currentEmail)
            .putBoolean("is_logged_in", loggedIn)
            .apply()
    }
}