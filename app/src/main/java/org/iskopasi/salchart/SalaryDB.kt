package org.iskopasi.salchart

import android.arch.persistence.room.Room
import android.content.Context
import org.iskopasi.salchart.room.AppDatabase
import org.iskopasi.salchart.room.MoneyData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by cora32 on 30.07.2017.
 */
@Singleton
class SalaryDB @Inject constructor(context: Context) {
    private val dbService: AppDatabase by lazy { Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.MONEY_TABLE).build() }
    fun getData() = dbService.moneyDao().getAll()
    fun saveData(data: List<MoneyData>) {
        dbService.moneyDao().insertAll(data)
    }
}