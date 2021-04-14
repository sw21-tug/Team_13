package com.team13.dealmymeal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val form_textview: TextView = findViewById(R.id.form_showEntry)
        val form_button: Button = findViewById(R.id.form_save)
        form_button.setOnClickListener() { v -> Toast.makeText(applicationContext, "Hello", Toast.LENGTH_LONG).show() }




    }


}


