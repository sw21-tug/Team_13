package com.team13.dealmymeal.ui.generate

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.slider.Slider
import com.team13.dealmymeal.*
import com.team13.dealmymeal.core.NotEnoughMealsException
import com.team13.dealmymeal.core.Plan
import com.team13.dealmymeal.core.PlanGenerator
import com.team13.dealmymeal.data.Category
import com.team13.dealmymeal.data.Meal
import com.team13.dealmymeal.data.MealViewModel
import com.team13.dealmymeal.data.MealViewModelFactory
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

        var data: List<Meal> = listOf()
        mealViewModel.allMeals.observe(viewLifecycleOwner) { meals ->
            data = meals
        }

        buttonGenerate.setOnClickListener {
            //val mealCount = ratingDays.rating.roundToInt() * numberOfMeals.rating.roundToInt()
            val meatCount: Int = (sliderRatio.valueTo - sliderRatio.value).roundToInt()
            val veggieCount: Int = (sliderRatio.value).roundToInt()
            //if (meatCount + veggieCount != mealCount)

            if(data.isEmpty())
                Toast.makeText(context, getString(R.string.not_enough_meals), Toast.LENGTH_SHORT).show()
            else
                try {
                    val plan = PlanGenerator.generatePlan(data, meatCount, veggieCount, numberOfSpecials.rating.roundToInt())
                    mealViewModel.insertPlan(Plan(ratingDays.rating.roundToInt(), numberOfMeals.rating.roundToInt(), null, plan))
                    //val plan = mealViewModel.allMeals.value?.let { it1 -> PlanGenerator.generatePlan(it1, meatCount, veggieCount, numberOfSpecials.rating.roundToInt()) }
                    activity?.onBackPressed()
                } catch (e: NotEnoughMealsException) {
                    Toast.makeText(context, getString(R.string.not_enough_meals), Toast.LENGTH_SHORT).show()
                }
        }

        return root
    }
}




