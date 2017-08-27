package org.iskopasi.salchart.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Created by cora32 on 27.08.2017.
 */
/**
 * Receives all SMS and applies custom regexp to form data for MoneyData object.
 */
class SMSReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, intent: Intent) {
        Log.e("SMSReceiver", " " + intent)

        when (intent.action) {
            "android.provider.Telephony.SMS_RECEIVED" -> {
                val bundle = intent.extras
                Log.e("SMSReceiver", " SMS_RECEIVED:" + bundle)

            }
        }
    }
}