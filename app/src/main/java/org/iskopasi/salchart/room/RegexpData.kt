package org.iskopasi.salchart.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by cora32 on 29.10.2017.
 */
@Entity(tableName = AppDatabase.REGEX_TABLE)
data class RegexpData(@PrimaryKey val id: Long,
                      val name: String,
                      val value: String)