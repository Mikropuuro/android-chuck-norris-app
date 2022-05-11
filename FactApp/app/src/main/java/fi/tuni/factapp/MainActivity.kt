package fi.tuni.factapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

@JsonIgnoreProperties(ignoreUnknown = true)
data class Joke(var result: MutableList<JokeItem>? = null)

@JsonIgnoreProperties(ignoreUnknown = true)
data class JokeItem(var value: String? = null)

class MainActivity : AppCompatActivity() {

    var editText : EditText? = null
    var text : TextView? = null
    var button : Button? = null
    var search : Button? = null
    var data : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.editText = findViewById(R.id.field)
        this.text = findViewById(R.id.text)
        this.data = findViewById(R.id.result)
        this.search = findViewById(R.id.specific)
        var key = "FU7dCpersox/7huua4T9lg==QJ81ImQfmgxRcYHY"
        var url = "https://api.api-ninjas.com/v1/facts?limit=3"


        search?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val query = editText?.getText().toString()
                if (query == "") {
                    thread() {
                        val mp = ObjectMapper()
                        val json: String? = getUrl("https://api.chucknorris.io/jokes/random")
                        val result: JokeItem? = mp.readValue(json, JokeItem::class.java)
                        println(result?.value)
                        runOnUiThread() {data?.text = (result?.value)}
                    }
                }

                else {
                    thread() {
                        val mp = ObjectMapper()
                        val json: String? = getUrl("https://api.chucknorris.io/jokes/search?query=${query}")
                        val item: Joke? = mp.readValue(json, Joke::class.java)
                        val list = item?.result
                        val rand = list?.size?.let { Random.nextInt(it) }
                        if (list !== null) {
                            for (item in list) {
                                runOnUiThread() {data?.text = list[rand!!].value}
                            }
                        }

                    }
                }

            }

        })

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
        Log.d(ContentValues.TAG, "onDestroy()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(ContentValues.TAG, "onRestart()")
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

}