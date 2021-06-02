package com.team13.dealmymeal.core

import com.team13.dealmymeal.data.Category
import com.team13.dealmymeal.data.Meal

class PlanGenerator {

    //4 states possible:
    //NONSPECIAL VEGGIE
    //NONSPECIAL MEAT
    //SPECIAL VEGGIE
    //SPECIAL MEAT

    companion object {

        private fun getPossibleCombinations(allMeals: List<Meal>, requestVeggie: Int, requestMeat: Int, requestSpecial: Int): List<Plan> {
            val allPossiblePlans = arrayListOf<Plan>()
            val specialVeggieMeals = allMeals.filter { it.categories!!.contains(Category.VEGGIE.category) && it.categories!!.contains(Category.SPECIAL.category) }
            val specialMeatMeals = allMeals.filter { it.categories!!.contains(Category.MEAT.category) && it.categories!!.contains(Category.SPECIAL.category) }
            val nonSpecialVeggieMeals = allMeals.filter { it.categories!!.contains(Category.VEGGIE.category) && !it.categories!!.contains(Category.SPECIAL.category) }
            val nonSpecialMeatMeals = allMeals.filter { it.categories!!.contains(Category.MEAT.category) && !it.categories!!.contains(Category.SPECIAL.category) }

            val specialVeggieCount = specialVeggieMeals.size
            val specialMeatCount = specialMeatMeals.size
            val nonSpecialVeggieCount = nonSpecialVeggieMeals.size
            val nonSpecialMeatCount = nonSpecialMeatMeals.size

            for (specialVeggie in 0 .. requestSpecial) {
                val specialMeat = requestSpecial - specialVeggie
                val nonSpecialVeggie = requestVeggie - specialVeggie
                val nonSpecialMeat = requestMeat - specialMeat
                if(specialVeggieCount >= specialVeggie && specialMeatCount >= specialMeat &&
                    nonSpecialVeggieCount >= nonSpecialVeggie && nonSpecialMeatCount >= nonSpecialMeat) {

                    val randomSpecialVeggieMeals = specialVeggieMeals.shuffled().take(specialVeggie)
                    val randomSpecialMeatMeals = specialMeatMeals.shuffled().take(specialMeat)
                    val randomNonSpecialVeggieMeals = nonSpecialVeggieMeals.shuffled().take(nonSpecialVeggie)
                    val randomNonSpecialMeatMeals = nonSpecialMeatMeals.shuffled().take(nonSpecialMeat)
                    val randomList = (randomNonSpecialVeggieMeals + randomNonSpecialMeatMeals +
                            randomSpecialVeggieMeals + randomSpecialMeatMeals).shuffled()

                    val plan = Plan(randomList)
                    allPossiblePlans.add(plan)
                }
            }

            return allPossiblePlans
        }

        /**
         * generates a random plan based on the parameters
         */
        fun generatePlan(allMeals: List<Meal>, meat: Int, veggie: Int, special: Int): Plan {
            assert(meat + veggie >= special)

            val combinations = getPossibleCombinations(allMeals, veggie, meat, special)

            if (combinations.isEmpty()) {
                throw NotEnoughMealsException()
            }

            return combinations.random()
        }
    }
}