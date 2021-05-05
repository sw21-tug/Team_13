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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.team13.dealmymeal.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


class AddMealFragment : Fragment() {
    private val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory((activity?.application as MealApplication).repository)
    }
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
        val form_ratingBar = root.findViewById<RatingBar>(R.id.form_ratingBar)
        val form_checkMeat = root.findViewById<CheckBox>(R.id.check_meat)
        val form_checkVeggie = root.findViewById<CheckBox>(R.id.check_veggie)
        val form_checkSpecial = root.findViewById<CheckBox>(R.id.check_special)
        val text = form_editText.text
        save_button.setOnClickListener() {

            val stars = form_ratingBar.rating
            val type = ArrayList<String>()
            if (form_checkMeat.isChecked)
                type.add("Meat")
            if (form_checkVeggie.isChecked)
                type.add("Veggie")
            if (form_checkSpecial.isChecked)
                type.add("Special")
            var meal = Meal(text.toString(), type, stars)

            val db = (activity as MainActivity).db
            val mealDao = db?.mealDao()

            if (mealDao == null) {
                Log.v(fragmentTag, "Meal could not be added")
            } else {
                //TODO probably better not to use GlobalScope??
                    /*
                GlobalScope.async {
                    mealDao.insertAll(meal)
                }*/
                mealViewModel.insert(meal)
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




