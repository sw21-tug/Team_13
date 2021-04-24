package com.team13.dealmymeal

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val form_button: Button = findViewById(R.id.form_goAdd)
        //val cancel_button: Button = findViewById(R.id.form_cancel)



        form_button.setOnClickListener() { v ->
            var a = supportFragmentManager.beginTransaction()
            var b = AddEntry()
            a.replace(R.id.main, b)
            a.addToBackStack(null)
            form_button.setVisibility(View.GONE)
            a.commit()




        }
/*
        cancel_button.setOnClickListener() { v ->
            var a = supportFragmentManager.beginTransaction()
            var b = AddEntry()
            a.replace(R.id.main, b)
            form_button.setVisibility(View.GONE)
            a.commit()




        }

*/


    }


}


