package org.iskopasi.salchart.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query

/**
 * Created by cora32 on 29.10.2017.
 */
@Dao
interface RegexpDao {
    @Query("SELECT * FROM " + AppDatabase.REGEX_TABLE)
    fun getAll(): List<RegexpData>
}