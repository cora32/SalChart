package org.iskopasi.salchart.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Created by cora32 on 30.07.2017.
 */
@Database(entities = arrayOf(MoneyData::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val MONEY_TABLE = "MONEY_TABLE"
    }

    abstract fun userDao(): MoneyDao
}