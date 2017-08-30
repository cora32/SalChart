package org.iskopasi.salchart.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by cora32 on 30.08.2017.
 */
object PrefHelper {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    fun init(context: Context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = sharedPreferences.edit()
    }

    fun saveBoolean(name: String, value: Boolean) {
        editor.putBoolean(name, value)
        editor.apply()
    }

    fun getBool(name: String): Boolean {
        return sharedPreferences.getBoolean(name, false)
    }

    fun getBool(name: String, defaultVal: Boolean): Boolean {
        return sharedPreferences.getBoolean(name, defaultVal)
    }

    fun saveString(name: String, value: String) {
        editor.putString(name, value)
        editor.apply()
    }

    fun getString(name: String): String {
        return sharedPreferences.getString(name, "")
    }

    fun getString(name: String, defaultVal: String): String {
        return sharedPreferences.getString(name, defaultVal)
    }

    fun saveLong(name: String, value: Long) {
        editor.putLong(name, value)
        editor.apply()
    }

    fun getLong(name: String, defaultVal: Long): Long {
        try {
            return sharedPreferences.getLong(name, defaultVal)
        } catch (ex: ClassCastException) {
            ex.printStackTrace()
        }

        return defaultVal
    }

    fun saveInteger(name: String, value: Int) {
        editor.putInt(name, value)
        editor.apply()
    }

    fun getInt(name: String, defaultVal: Int): Int {
        try {
            return sharedPreferences.getInt(name, defaultVal)
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }

        return 0
    }
}