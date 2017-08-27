package org.iskopasi.salchart

import android.app.Application
import android.arch.persistence.room.Room
import org.iskopasi.salchart.room.AppDatabase

/**
 * Created by cora32 on 27.08.2017.
 */
class App : Application() {
    companion object {
        private lateinit var db: AppDatabase
        fun getDB() = db
    }

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(this, AppDatabase::class.java, AppDatabase.MONEY_TABLE).build()
    }
}