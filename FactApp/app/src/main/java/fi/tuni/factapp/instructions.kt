package fi.tuni.factapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class instructions : AppCompatActivity() {

    var text : TextView? = null //Displays the name of the app
    var instructions : TextView? = null //Displays the insturctions
    var button : Button? = null //Button used to navigate to the main screen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructions)
        this.text = findViewById(R.id.text)
        this.button = findViewById(R.id.change)
        this.instructions = findViewById(R.id.instructions)
        instructions?.text = "This app provides your daily Chuck Norris-joke needs. You can either search for joke by using a keyword or search a random joke by not writing anything on the text field provided."

        //navigates back to the main activity
        button?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}