package edu.uw.ischool.newart.httpapp

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors


class HttpApplication : Application() {
    val executor = Executors.newFixedThreadPool(5)
}

class MainActivity : AppCompatActivity() {
    lateinit var button : Button
    lateinit var textview : TextView
    lateinit var edittext : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        textview = findViewById(R.id.textView)
        edittext = findViewById(R.id.editText)

        button.setOnClickListener {
            fetch()
        }
    }

    fun fetch() {
        val url = edittext.text.toString()
        Log.v("MainActivity", "Fetching ${url}")

        (application as HttpApplication).executor.execute {
            val urlConnection = URL(url).openConnection() as HttpURLConnection
            val responseStream = ByteArrayOutputStream()
            try {
                // There's all sorts of better ways to read the response
                // from the HTTP request; this is just the simplest way.
                val incoming = BufferedInputStream(urlConnection.inputStream).bufferedReader()
                incoming.forEachLine {
                    responseStream.write(it.toByteArray())
                }
            } finally {
                urlConnection.disconnect()
            }

            // Now that we have the complete response, show it in the UI
            this.runOnUiThread {
                textview.text = responseStream.toString()
            }
        }
    }
}