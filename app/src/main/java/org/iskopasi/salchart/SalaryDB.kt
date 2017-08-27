package org.iskopasi.salchart

import org.iskopasi.salchart.room.MoneyData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by cora32 on 30.07.2017.
 */
@Singleton
class SalaryDB @Inject constructor() {
    fun getData() = App.getDB().moneyDao().getAll()
    fun saveData(data: List<MoneyData>) {
        App.getDB().moneyDao().insertAll(data)
    }
}