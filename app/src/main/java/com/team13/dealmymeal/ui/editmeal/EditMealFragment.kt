package com.team13.dealmymeal.ui.editmeal

import com.team13.dealmymeal.ui.addmeal.AddMealViewModel


import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.team13.dealmymeal.*
import com.team13.dealmymeal.data.Category
import com.team13.dealmymeal.data.Meal
import com.team13.dealmymeal.data.MealViewModel
import com.team13.dealmymeal.data.MealViewModelFactory

import kotlinx.coroutines.runBlocking


class EditMealFragment : Fragment() {
    private val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory((activity?.application as MealApplication).repository)
    }
    private lateinit var editMealViewModel: AddMealViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editMealViewModel =
            ViewModelProvider(this).get(AddMealViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_editmeal, container, false)

        val btnSave = root.findViewById<Button>(R.id.form_save)
        val editTitle = root.findViewById<EditText>(R.id.form_edit)
        val switchMeat = root.findViewById<SwitchMaterial>(R.id.check_meat)
        val switchVeggie = root.findViewById<SwitchMaterial>(R.id.check_veggie)
        btnSave.isEnabled = false
        btnSave.getBackground().setAlpha(51)
        switchMeat.setOnCheckedChangeListener {_, isChecked ->
            if (switchMeat.isChecked)
            {
                btnSave.isEnabled = true
                btnSave.getBackground().setAlpha(200)
                switchVeggie.setEnabled(false)
            }else
            {
                btnSave.isEnabled = false
                btnSave.getBackground().setAlpha(51)
                switchVeggie.setEnabled(true)
            }
        }

        switchVeggie.setOnCheckedChangeListener {_ , isChecked ->
            if (switchVeggie.isChecked)
            {
                btnSave.isEnabled = true
                btnSave.getBackground().setAlpha(200)
                switchMeat.setEnabled(false)
            }else
            {
                btnSave.isEnabled = false
                btnSave.getBackground().setAlpha(51)
                switchMeat.setEnabled(true)
            }
        }

        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSave = view.findViewById<Button>(R.id.form_save)
        val editTitle = view.findViewById<EditText>(R.id.form_edit)
        val ratingBar = view.findViewById<RatingBar>(R.id.form_ratingBar)
        val switchMeat = view.findViewById<SwitchMaterial>(R.id.check_meat)
        val switchVeggie = view.findViewById<SwitchMaterial>(R.id.check_veggie)
        val switchSpecial = view.findViewById<SwitchMaterial>(R.id.check_special)
        val meal = arguments?.getParcelable<Meal>("Meal")
        if (meal != null) {
            editTitle.setText(meal.title)
            ratingBar.rating = meal.rating!!
            val isMeat = meal.categories!!.contains(0)
            val isSpecial = meal.categories!!.contains(1)
            val isVeggie = meal.categories!!.contains(2)

            switchMeat.isChecked = isMeat
            switchVeggie.isChecked = isVeggie
           switchSpecial.isChecked = isSpecial

            btnSave.setOnClickListener {
                val type = ArrayList<Int>()
                if (switchMeat.isChecked)
                    type.add(Category.MEAT.category)
                if (switchVeggie.isChecked)
                    type.add(Category.VEGGIE.category)
                if (switchSpecial.isChecked)
                    type.add(Category.SPECIAL.category)

                // check if meal name already exits
                var canUpdate = true
                if(editTitle.text.toString() != meal.title) {
                    var count: Int
                    runBlocking {
                        count = mealViewModel.getCount(editTitle.text.toString())
                    }

                    if (count > 0) {
                        val toast: Toast =
                            Toast.makeText(context, getString(R.string.toast_add_duplicate_error), Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.TOP, 0, 250)
                        toast.show()
                        canUpdate = false
                    }
                }

                meal.title = editTitle.text.toString()
                meal.rating = ratingBar.rating
                meal.categories = type

                if (meal.title != "") {
                    if (canUpdate) {
                        mealViewModel.updateMeal(meal)
                        activity?.onBackPressed()
                    }
                } else {
                    val toast: Toast =
                        Toast.makeText(context, getString(R.string.toastEmptyMeal), Toast.LENGTH_LONG)
                    toast.setGravity(Gravity.TOP, 0, 250)
                    toast.show()
                }
            }
        }

    }

}





