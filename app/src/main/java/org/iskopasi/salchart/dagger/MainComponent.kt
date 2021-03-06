package org.iskopasi.salchart.dagger

import dagger.Component
import org.iskopasi.salchart.SalaryRepository
import org.iskopasi.salchart.SalaryViewModel
import org.iskopasi.salchart.SettingsActivity
import javax.inject.Singleton

/**
 * Created by cora32 on 30.07.2017.
 */
@Singleton
@Component
interface MainComponent {
    fun inject(salaryViewModel: SalaryViewModel)
    fun inject(salaryRepository: SalaryRepository)
    fun inject(settingsActivity: SettingsActivity)
}