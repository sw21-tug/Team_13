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



        supportFragmentManager.setFragmentResultListener("requestKey", this) { requestKey, bundle ->
            val result = bundle.getString("bundleKey")
            form_button.visibility = View.VISIBLE
        }

        form_button.setOnClickListener() { v ->
            val a = supportFragmentManager.beginTransaction()
            val b = AddEntry()
            a.replace(R.id.main, b)
            a.addToBackStack(null)
            form_button.visibility = View.GONE
            a.commit()


        }


        if (supportFragmentManager.findFragmentById(R.id.fragment1)?.childFragmentManager?.isDestroyed == true) {
            form_button.visibility = View.VISIBLE
        }

}
}





