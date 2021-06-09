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
import kotlin.math.min
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
        numberOfSpecials.rating = 0.0f
        numberOfSpecials.numStars = min(5, ratingDays.rating.roundToInt() * numberOfMeals.rating.roundToInt())
        numberOfSpecials.stepSize = 1.0f

        ratingDays.setOnRatingBarChangeListener { ratingBar, rating, user ->
            sliderRatio.value = 0.0f
            sliderRatio.valueTo = rating * numberOfMeals.rating.roundToInt()
            sliderRatio.value = ((rating * numberOfMeals.rating)/2).roundToInt().toFloat()
            numberOfSpecials.rating = 0.0f
            numberOfSpecials.numStars = min(5, rating.roundToInt() * numberOfMeals.rating.roundToInt())
            numberOfSpecials.stepSize = 1.0f
        }

        numberOfMeals.setOnRatingBarChangeListener { ratingBar, rating, user ->
            sliderRatio.value = 0.0f
            sliderRatio.valueTo = rating * ratingDays.rating.roundToInt()
            sliderRatio.value = ((rating * ratingDays.rating)/2).roundToInt().toFloat()
            numberOfSpecials.rating = 0.0f
            numberOfSpecials.numStars = min(5, rating.roundToInt() * ratingDays.rating.roundToInt())
            numberOfSpecials.stepSize = 1.0f
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
                    val plan = PlanGenerator.generatePlan(data, meatCount, veggieCount, min(meatCount, min(veggieCount, numberOfSpecials.rating.roundToInt())))
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




