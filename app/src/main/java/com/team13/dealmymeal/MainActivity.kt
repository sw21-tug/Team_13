package com.team13.dealmymeal

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<ImageButton>(R.id.btnmenu)
        val menu = findViewById<NavigationView>(R.id.nav_bar_id)

        btn.setOnClickListener()
        {
            if(menu.visibility == View.VISIBLE)
            {
                menu.visibility = View.INVISIBLE
            }
            else
            {
                menu.visibility = View.VISIBLE
            }

        }





    }


}

