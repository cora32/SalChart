package org.iskopasi.salchart

import org.iskopasi.salchart.room.MoneyData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by cora32 on 30.07.2017.
 */

/**
 * Class represents one of the sources entities. It provides data to/from database.
 */
@Singleton
class SalaryDB @Inject constructor() {
    /**
     * Returns all saved data in database.
     */
    fun getData() = App.getDB().moneyDao().getAll()

    /**
     * Accepts list of @MoneyData and saves it to database.
     */
    fun saveData(data: List<MoneyData>) {
        App.getDB().moneyDao().insertAll(data)
    }
}