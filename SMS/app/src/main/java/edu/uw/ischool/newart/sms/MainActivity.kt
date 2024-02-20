package edu.uw.ischool.newart.sms

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    private lateinit var btnSend : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSend = findViewById(R.id.btnSend)
        btnSend.setOnClickListener {
            sendSMS()
        }
        checkForSmsPermission()
    }
    private fun checkForSmsPermission() {
        // This will (probably) prompt only once, when first installed/run on the device.
        // Once obtained, the permission will be "sticky".
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) !=
            PackageManager.PERMISSION_GRANTED) {
            Log.d("MainActivity", "Permission not granted!")
            // Permission not yet granted. Use requestPermissions().
            // MY_PERMISSIONS_REQUEST_SEND_SMS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.SEND_SMS), 1)
            btnSend.isEnabled = false
        } else {
            // Permission already granted. Enable the message button.
            btnSend.isEnabled = true
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (permissions[0].equals(Manifest.permission.SEND_SMS, ignoreCase = true)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                } else {
                    // Permission denied.
                    Log.d("MainActivity", "Failed to obtain permission")
                    Toast.makeText(
                        this,
                        "Failed to obtain permission",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Disable the message button.
                    btnSend.isEnabled = false
                }
            }
        }
    }
    private fun sendSMS() {
        checkForSmsPermission()

        // We can either
        // (a) use the built-in SMS app to send the text message
        //*
        //val sendIntent = Intent(Intent.ACTION_VIEW)
        val defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this) // Need to change the build to API 19

        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.setType("text/plain")
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello there!")

        if (defaultSmsPackageName != null)
        // Can be null in case that there is no default,
        // then the user would be able to choose
        // any app that support this intent.
        {
            Log.i("MainActivity", "defaultSmsPackageName is $defaultSmsPackageName")
            sendIntent.setPackage(defaultSmsPackageName)
        }
        startActivity(sendIntent)
        // */

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