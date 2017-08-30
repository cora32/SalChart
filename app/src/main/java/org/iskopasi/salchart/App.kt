package org.iskopasi.salchart

import android.app.Application
import android.arch.persistence.room.Room
import org.iskopasi.salchart.room.AppDatabase
import org.iskopasi.salchart.utils.PrefHelper

/**
 * Created by cora32 on 27.08.2017.
 */
class App : Application() {
    /**
     * Static fields to be accessed from any part of application.
     * Also allows to remove Context dependency from data sources classes (SalaryDB).
     */
    companion object {
        private lateinit var db: AppDatabase
        fun getDB() = db
    }

    /**
     * Initializing Room Database object to be used as singleton.
     */
    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(this, AppDatabase::class.java, AppDatabase.MONEY_TABLE).build()

        PrefHelper.init(this)
    }
}