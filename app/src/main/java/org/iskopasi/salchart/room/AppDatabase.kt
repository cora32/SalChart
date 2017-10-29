package org.iskopasi.salchart.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Created by cora32 on 30.07.2017.
 */
@Database(entities = arrayOf(MoneyData::class,
        RegexpData::class), version = 2)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val MONEY_TABLE = "MONEY_TABLE"
        const val REGEX_TABLE = "REGEX_TABLE"
    }

    abstract fun moneyDao(): MoneyDao
    abstract fun regexpDao(): RegexpDao
}