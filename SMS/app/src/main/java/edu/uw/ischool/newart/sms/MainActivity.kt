package edu.uw.ischool.newart.sms

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    lateinit var btnSend : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSend = findViewById(R.id.btnSend)
        btnSend.setOnClickListener {
            sendSMS()
        }
    }

    fun sendSMS() {
        // We can either
        // (a) use the built-in SMS app to send the text message
        /*
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.putExtra("Hello from an Android application!", "default content")
        sendIntent.setType("vnd.android-dir/mms-sms")
        startActivity(sendIntent)
         */

        // (b) use the SmsManager API to send it from our app directly
        //val smsManager = SmsManager.getDefault()   // deprecated
        // or use
        //val smsManager = getSystemService(SmsManager::class.java)
        // To be most backwards/forwards compatible...
        val smsManager =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                getSystemService(SmsManager::class.java)
            else
                SmsManager.getDefault()
        smsManager.sendTextMessage("+14255551212", null, "Hello!", null, null)
    }
}