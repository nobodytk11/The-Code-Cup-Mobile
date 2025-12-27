package com.example.thecodecup

import android.app.Application

class TheCodeCupApp : Application() {
    var isDataLoaded = false

    override fun onCreate() {
        super.onCreate()
        // Start background data loading immediately
        PersistenceManager.init(this) {
            isDataLoaded = true
        }
    }
}