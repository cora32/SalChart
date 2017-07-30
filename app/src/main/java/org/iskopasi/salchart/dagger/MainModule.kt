package org.iskopasi.salchart.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import org.iskopasi.salchart.SalaryDB
import org.iskopasi.salchart.SalaryRepository
import javax.inject.Singleton

/**
 * Created by cora32 on 30.07.2017.
 */
@Module class MainModule(val context: Context) {
    @Provides @Singleton fun provideSalaryRepository() = SalaryRepository()
    @Provides @Singleton fun provideLoginDB() = SalaryDB(context)
}