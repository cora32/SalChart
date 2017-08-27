package org.iskopasi.salchart.room

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

/**
 * Created by cora32 on 30.07.2017.
 */

/**
 * Represents custom DAO for Room ORM.
 */
@Dao
interface MoneyDao {
    /**
     * The query to get all data from db.
     */
    @Query("SELECT * FROM " + AppDatabase.MONEY_TABLE)
    fun getAll(): LiveData<List<MoneyData>>

    /**
     * Saves multiple MoneyData objects to DB.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg moneyData: MoneyData)

    /**
     * Saves list of MoneyData objects to DB.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(moneyDataList: List<MoneyData>)
}