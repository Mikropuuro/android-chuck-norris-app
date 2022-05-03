package fi.tuni.factapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    var editText : EditText? = null
    var text : TextView? = null
    var button : Button? = null
    var data : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.editText = findViewById(R.id.field)
        this.text = findViewById(R.id.text)
        this.button = findViewById(R.id.button)
        var url = "https://api.api-ninjas.com/v1/facts?limit=3"
        button?.setOnClickListener {
            getUrl(url)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(ContentValues.TAG, "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d(ContentValues.TAG, "onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.d(ContentValues.TAG, "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d(ContentValues.TAG, "onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(ContentValues.TAG, "onDestroy()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(ContentValues.TAG, "onRestart()")
    }

    fun getUrl(url: String) : String? {
        var result : String? = null
        val sb = StringBuffer()
        var url = URL("https://api.api-ninjas.com/v1/facts?limit=3")
        var urlConnection : HttpURLConnection = url.openConnection() as HttpURLConnection
        urlConnection.setRequestProperty("accept", "application/json")

        val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
        reader.use {
            var line : String? = null

            do {
                line = it.readLine()
                sb.append(line)
            } while(line != null)

            result = sb.toString()
        }

        return result
        data?.text = result
    }

}