package com.team13.dealmymeal.ui.home

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.team13.dealmymeal.MainActivity
import com.team13.dealmymeal.Meal
import com.team13.dealmymeal.MealDao
import com.team13.dealmymeal.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


class AddMealFragment : Fragment() {

    private lateinit var addMealViewModel: AddMealViewModel
    private val fragmentTag = "AddMealFragment"

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        addMealViewModel =
                ViewModelProvider(this).get(AddMealViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_addmeal, container, false)


        val btn = root.findViewById<ImageButton>(R.id.btnmenu)
        val menu = root.findViewById<NavigationView>(R.id.nav_bar_id)
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




        AppCompatActivity()
        //val view = inflater.inflate(R.layout.fragment_addmeal, container, true)


        val form_textview = root.findViewById<TextView>(R.id.form_showEntry)
        val save_button = root.findViewById<Button>(R.id.form_save)
        val form_editText = root.findViewById<EditText>(R.id.form_edit)
        val text = form_editText.text
        save_button.setOnClickListener() {

            val stars = 0
            val type = 0
            var meal = Meal(text.toString(), stars, type)

            val db = (activity as MainActivity).db
            val mealDao = db?.mealDao()

            if (mealDao == null) {
                Log.v(fragmentTag, "Meal could not be added")
            } else {
                GlobalScope.async {
                    mealDao.insertAll(meal)
                }
            }

            val toast: Toast =
                Toast.makeText(context, "Meal " + text + " added!", Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 0, 250)
            toast.show()

//            GlobalScope.async {
//                val test = mealDao?.getAll()
//                println(test)
//                val i = 0
//            }

            form_textview.text = text

        }

        return root
    }

}




