package org.iskopasi.salchart.room

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

/**
 * Created by cora32 on 30.07.2017.
 */
@Dao
interface MoneyDao {
    @Query("SELECT * FROM " + AppDatabase.MONEY_TABLE)
    fun getAll(): LiveData<List<MoneyData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: MoneyData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(translations: List<MoneyData>)
}