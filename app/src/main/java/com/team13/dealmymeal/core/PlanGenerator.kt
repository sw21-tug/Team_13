package com.team13.dealmymeal.core

import com.team13.dealmymeal.data.Category
import com.team13.dealmymeal.data.Meal

class PlanGenerator {
    companion object {
        fun generatePlan(allMeals: List<Meal>, days: Int, mealsPerDay: Int, meat: Int, veggie: Int, special: Int): List<Meal> {
            val countMeat = allMeals.count { it.categories!!.contains(Category.MEAT.category) }
            val countVeggie = allMeals.count { it.categories!!.contains(Category.VEGGIE.category) }
            val countSpecial = allMeals.count { it.categories!!.contains(Category.SPECIAL.category) }

            if (days * mealsPerDay > allMeals.size){
                throw NotEnoughMealsException()
            }

            if (special > countSpecial || veggie > countVeggie || meat > countMeat) {
                throw NotEnoughMealsException()
            }

            return emptyList()
        }
    }
}