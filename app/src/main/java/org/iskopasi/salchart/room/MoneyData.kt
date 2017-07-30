package org.iskopasi.salchart.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by cora32 on 25.07.2017.
 */
@Entity(tableName = AppDatabase.MONEY_TABLE)
data class MoneyData(@PrimaryKey val id: Long,
                     val date: String,
                     val value: Float)