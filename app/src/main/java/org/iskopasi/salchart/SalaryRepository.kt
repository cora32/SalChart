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
class SalaryRepository {
    init {
        MainActivity.daggerGraph.inject(this)
    }

    @Inject lateinit var db: SalaryDB

    fun getData() = db.getData()

    private fun generateChartData(): List<MoneyData> {
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        val random = Random(System.currentTimeMillis())
        val data = (0..200).map { MoneyData(it.toLong(), sdf.format(System.currentTimeMillis()), Math.round(it + random.nextFloat() * 100).toFloat()) }

        return data
    }

    fun saveData() = db.saveData(generateChartData())
}