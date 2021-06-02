package com.team13.dealmymeal.core

import com.team13.dealmymeal.data.Category
import com.team13.dealmymeal.data.Meal

class Plan(private var meal: List<Meal>) {
    fun getMeatCount(): Int {
        return meal.count { it.categories!!.contains(Category.MEAT.category) }
    }

    fun getVeggieCount(): Int {
        return meal.count { it.categories!!.contains(Category.VEGGIE.category) }
    }

    fun getSpecialCount(): Int {
        return meal.count { it.categories!!.contains(Category.SPECIAL.category) }
    }
}