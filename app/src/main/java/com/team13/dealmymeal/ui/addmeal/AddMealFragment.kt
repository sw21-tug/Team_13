package com.team13.dealmymeal.ui.addmeal

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.team13.dealmymeal.*
import kotlinx.coroutines.runBlocking


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

        AppCompatActivity()
        //val view = inflater.inflate(R.layout.fragment_addmeal, container, true)

        val save_button = root.findViewById<Button>(R.id.form_save)
        val form_editText = root.findViewById<EditText>(R.id.form_edit)
        val form_ratingBar = root.findViewById<RatingBar>(R.id.form_ratingBar)
        val form_checkMeat = root.findViewById<Switch>(R.id.check_meat)
        val form_checkVeggie = root.findViewById<Switch>(R.id.check_veggie)
        val form_checkSpecial = root.findViewById<Switch>(R.id.check_special)
        val text = form_editText.text
        save_button.setOnClickListener() {

            val stars = form_ratingBar.rating
            val type = ArrayList<String>()
            if (form_checkMeat.isChecked)
                type.add(resources.getString(R.string.meat))
            if (form_checkVeggie.isChecked)
                type.add(resources.getString(R.string.vegetarian))
            if (form_checkSpecial.isChecked)
                type.add(resources.getString(R.string.special))
            val meal = Meal(text.toString(), type, stars)

            var count: Int
            runBlocking {
                count = mealViewModel.getCount(meal.title!!)
            }

            if(count == 0){
                mealViewModel.insert(meal)
                val toast: Toast =
                        Toast.makeText(context, "Meal " + text + " added!", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.TOP, 0, 250)
                toast.show()
            } else {
                val toast: Toast =
                        Toast.makeText(context, getString(R.string.toast_add_duplicate_error), Toast.LENGTH_LONG)
                toast.setGravity(Gravity.TOP, 0, 250)
                toast.show()
            }
        }

        return root
    }

}




