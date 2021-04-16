package com.team13.dealmymeal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val form_textview: TextView = findViewById(R.id.form_showEntry)
        val form_button: Button = findViewById(R.id.form_save)
        val form_editText: EditText = findViewById(R.id.form_edit)
        val text = form_editText.text
        form_button.setOnClickListener() {
            v ->
            val toast: Toast = Toast.makeText(applicationContext, "Meal " + text + " added!", Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 0, 250)
            toast.show()

            form_textview.setText(text)

        }




    }


}


