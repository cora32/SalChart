package org.iskopasi.salchart

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import org.iskopasi.salchart.room.MoneyData
import org.jetbrains.anko.doAsync
import javax.inject.Inject

/**
 * Created by cora32 on 30.07.2017.
 */
class SalaryViewModel : ViewModel() {
    init {
        MainActivity.daggerGraph.inject(this)
    }

    @Inject lateinit var repository: SalaryRepository
    val data: LiveData<List<MoneyData>> by lazy { repository.getData() }

    fun saveData() = doAsync { repository.saveData() }
}