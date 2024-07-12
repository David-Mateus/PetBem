package com.example.petbem

import android.app.Application
import com.example.petbem.model.AppDataBase

class App : Application(){
    lateinit var db: AppDataBase
    override fun onCreate() {
        super.onCreate()
        db = AppDataBase.getDatabase(this)
    }
}