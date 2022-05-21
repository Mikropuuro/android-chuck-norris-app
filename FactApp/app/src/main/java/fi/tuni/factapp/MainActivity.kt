package fi.tuni.factapp

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread
import kotlin.random.Random


@JsonIgnoreProperties(ignoreUnknown = true)
data class Joke(var result: MutableList<JokeItem>? = null)

@JsonIgnoreProperties(ignoreUnknown = true)
data class JokeItem(var value: String? = null)

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager

    var editText : EditText? = null //The textfield user can use to search jokes with a keyword
    var text : TextView? = null //Displays the name of the app. Also changes color depending on the device's position
    var button : Button? = null //Button used to get to the instructions-page
    var search : Button? = null //Button used to search jokes
    var data : TextView? = null //Displays the jokes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorStuff()
        this.editText = findViewById(R.id.field)
        this.text = findViewById(R.id.text)
        this.data = findViewById(R.id.result)
        this.search = findViewById(R.id.specific)
        this.button = findViewById(R.id.change)

        button?.setOnClickListener {
            val intent = Intent(this, instructions::class.java)
            startActivity(intent)
        }


        search?.setOnClickListener {
            val query = editText?.getText().toString()
            if (query == "") {
                thread() {
                    val mp = ObjectMapper()
                    val json: String? = getUrl("https://api.chucknorris.io/jokes/random")
                    val result: JokeItem? = mp.readValue(json, JokeItem::class.java)
                    println(result?.value)
                    runOnUiThread() { data?.text = (result?.value) }
                }
            } else {
                thread() {
                    val mp = ObjectMapper()
                    val json: String? =
                        getUrl("https://api.chucknorris.io/jokes/search?query=${query}")
                    val item: Joke? = mp.readValue(json, Joke::class.java)
                    val list = item?.result
                    val rand = list?.size?.let { Random.nextInt(it + 1) }
                    if (list?.isEmpty() !== true) {
                        if (list != null) {
                            for (item in list) {
                                runOnUiThread() { data?.text = list[rand!!].value }
                            }
                        }
                    } else {
                        runOnUiThread() { data?.text = "No jokes found with this keyword :(" }
                    }

                }
            }
        }

    }

    private fun sensorStuff() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(this,
                it,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(ContentValues.TAG, "onStart()")
    }

    override fun onResume() {
        super.onResume()
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
        sensorManager.unregisterListener(this)
        Log.d(ContentValues.TAG, "onDestroy()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(ContentValues.TAG, "onRestart()")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        var saved = data?.text.toString()
        outState.putString("keyd", saved)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val strValue : String? = savedInstanceState.getString("keyd")
        if (strValue !== null) {
            data?.text = strValue
        }
    }




    fun getUrl(url: String) : String? {
        var result : String? = null
        val sb = StringBuffer()
        var url = URL(url)
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

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val sides = event.values[0]
            val upDown = event.values[1]

            val color = if (upDown.toInt() == 0 && sides.toInt() == 0) Color.GREEN else Color.RED
            text?.setTextColor(color)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

}