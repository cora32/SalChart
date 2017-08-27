package org.iskopasi.salchart

import org.iskopasi.salchart.room.MoneyData
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by cora32 on 30.07.2017.
 */
@Singleton
class SalaryRepository @Inject constructor() {
    init {
        MainActivity.daggerGraph.inject(this)
    }

    @Inject lateinit var db: SalaryDB

    fun getData() = db.getData()

    private fun generateChartData(): List<MoneyData> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -100)
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        val random = Random(System.currentTimeMillis())

        return (0..200).map {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            MoneyData(it.toLong(), sdf.format(calendar.timeInMillis), Math.round(it + random.nextFloat() * 100) - (if (it % 5 == 0) 100 else 0).toFloat())
        }
                .reversed()
    }

    fun saveData() = db.saveData(generateChartData())
}