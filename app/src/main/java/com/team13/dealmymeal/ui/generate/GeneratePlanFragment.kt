package com.team13.dealmymeal.ui.generate

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.team13.dealmymeal.*
import com.team13.dealmymeal.core.NotEnoughMealsException
import com.team13.dealmymeal.core.Plan
import com.team13.dealmymeal.core.PlanGenerator
import com.team13.dealmymeal.data.Category
import com.team13.dealmymeal.data.Meal
import com.team13.dealmymeal.data.MealViewModel
import com.team13.dealmymeal.data.MealViewModelFactory
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt


class GeneratePlanFragment : Fragment() {
    private val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory((activity?.application as MealApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_generate_plan, container, false)

        val ratingDays = root.findViewById<RatingBar>(R.id.ratingDays)
        val numberOfMeals = root.findViewById<RatingBar>(R.id.numberOfMeals)
        val numberOfSpecials = root.findViewById<RatingBar>(R.id.numberOfSpecials)
        val sliderRatio = root.findViewById<Slider>(R.id.sliderRatio)
        val buttonGenerate = root.findViewById<Button>(R.id.buttonGenerate)

        sliderRatio.valueTo = ratingDays.rating * numberOfMeals.rating
        sliderRatio.value = (sliderRatio.valueTo/2).roundToInt().toFloat()

        val meatCount: Int = (sliderRatio.valueTo - sliderRatio.value).roundToInt()
        val veggieCount: Int = (sliderRatio.value).roundToInt()
        /*
        buttonGenerate.isEnabled = mealViewModel.allMeals.value?.let { it1 ->
            meatCount <= it1.count { it.categories!!.contains(Category.MEAT.category)}
                    && veggieCount <= it1.count { it.categories!!.contains(Category.VEGGIE.category)}
                    && numberOfSpecials.rating.roundToInt() <= it1.count { it.categories!!.contains(Category.SPECIAL.category)}
        } == true*/

        sliderRatio.addOnChangeListener { slider, value, fromUser ->
            val meatCount: Int = (sliderRatio.valueTo - sliderRatio.value).roundToInt()
            val veggieCount: Int = (sliderRatio.value).roundToInt()
            // check if so many in db
            /*
            buttonGenerate.isEnabled = !(meatCount > mealViewModel.allMeals.value?.count { it.categories!!.contains(Category.MEAT.category) }!!
                    || veggieCount > mealViewModel.allMeals.value?.count { it.categories!!.contains(Category.VEGGIE.category) }!!)*/
            Log.e("hfsif", (mealViewModel.allMeals.value?.let { it1 ->
                meatCount <= it1.count { it.categories!!.contains(Category.MEAT.category)}
                        && veggieCount <= it1.count { it.categories!!.contains(Category.VEGGIE.category)}
            } == true).toString())
            Log.e("gdsi", "$meatCount $veggieCount")

            viewLifecycleOwner.lifecycleScope.launch {
                Log.e("gdsi", mealViewModel.getCount(0).toString())
            }
        }

        ratingDays.setOnRatingBarChangeListener { ratingBar, rating, user ->
            sliderRatio.valueTo = rating * numberOfMeals.rating.roundToInt()
        }

        numberOfMeals.setOnRatingBarChangeListener { ratingBar, rating, user ->
            sliderRatio.valueTo = rating * ratingDays.rating.roundToInt()
        }

        numberOfSpecials.setOnRatingBarChangeListener { ratingBar, rating, user ->
            // check
            buttonGenerate.isEnabled = rating.roundToInt() <= mealViewModel.allMeals.value?.count { it.categories!!.contains(Category.SPECIAL.category) }!!
        }

        buttonGenerate.setOnClickListener {
            val mealCount = ratingDays.rating.roundToInt() * numberOfMeals.rating.roundToInt()
            val meatCount: Int = (sliderRatio.valueTo - sliderRatio.value).roundToInt()
            val veggieCount: Int = (sliderRatio.value).roundToInt()
            //if (meatCount + veggieCount != mealCount)

            try {
                val plan = mealViewModel.allMeals.value?.let { it1 -> PlanGenerator.generatePlan(it1, meatCount, veggieCount, numberOfSpecials.rating.roundToInt()) }
                mealViewModel.insertPlan(Plan(ratingDays.rating.roundToInt(), numberOfMeals.rating.roundToInt(), "", plan))
                activity?.onBackPressed()
            } catch (e: NotEnoughMealsException) {
                Toast.makeText(context, "blblb", Toast.LENGTH_SHORT).show()
            }

        }

        return root
    }
}




