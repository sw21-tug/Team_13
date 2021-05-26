package com.team13.dealmymeal.ui.editmeal

import com.team13.dealmymeal.ui.addmeal.AddMealViewModel


import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.team13.dealmymeal.*
import com.team13.dealmymeal.data.Category
import com.team13.dealmymeal.data.Meal
import com.team13.dealmymeal.data.MealViewModel
import com.team13.dealmymeal.data.MealViewModelFactory
import kotlinx.android.synthetic.main.fragment_addmeal.view.*
import kotlinx.android.synthetic.main.fragment_editmeal.view.*
import kotlinx.android.synthetic.main.fragment_editmeal.view.check_meat
import kotlinx.android.synthetic.main.fragment_editmeal.view.check_special
import kotlinx.android.synthetic.main.fragment_editmeal.view.check_veggie
import kotlinx.android.synthetic.main.fragment_editmeal.view.form_edit
import kotlinx.android.synthetic.main.fragment_editmeal.view.form_ratingBar
import kotlinx.android.synthetic.main.fragment_editmeal.view.form_save
import kotlinx.android.synthetic.main.fragment_meal_overview.view.*


class EditMealFragment : Fragment() {
    private val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory((activity?.application as MealApplication).repository)
    }
    private lateinit var EditMealViewModel: AddMealViewModel
    private val fragmentTag = "EditMealFragment"


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        EditMealViewModel =
                ViewModelProvider(this).get(AddMealViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_editmeal, container, false)
        //val menu = root.findViewById<NavigationView>(R.id.nav_bar_id)

        AppCompatActivity()
        //val view = inflater.inflate(R.layout.fragment_addmeal, container, true)


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val meal = arguments?.getParcelable<Meal>("Meal")
        if (meal != null) {
            view.form_edit.setText(meal.title)
            view.form_ratingBar.setRating(meal.rating?.toFloat()!!)
            val isMeat = meal.categories!!.contains(0)
            val isSpecial = meal.categories!!.contains(1)
            val isVeggie = meal.categories!!.contains(2)

            view.check_meat.isChecked = isMeat
            view.check_veggie.isChecked = isVeggie
            view.check_special.isChecked = isSpecial

            view.form_save.setOnClickListener {
                val type = ArrayList<Int>()
                if (view.check_meat.isChecked)
                    type.add(Category.MEAT.category)
                if (view.check_veggie.isChecked)
                    type.add(Category.VEGGIE.category)
                if (view.check_special.isChecked)
                    type.add(Category.SPECIAL.category)

                meal.title = view.form_edit.text.toString()

                meal.rating = view.form_ratingBar.rating
                meal.categories = type


                if (meal.title != "") {
                    mealViewModel.update(meal)
                    activity?.onBackPressed()
                }
                else {
                    val toast: Toast =
                        Toast.makeText(context, getString(R.string.toastEmptyMeal), Toast.LENGTH_LONG)
                    toast.setGravity(Gravity.TOP, 0, 250)
                    toast.show()
                }
            }
        }

    }

}





