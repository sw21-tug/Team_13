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

        private fun getPossibleCombinations(allMeals: List<Meal>, requestMeals: Int, requestVeggie: Int, requestMeat: Int, requestSpecial: Int, requestNonSpecial: Int): List<PlanRaw> {
            val allPossiblePlans = arrayListOf<PlanRaw>()
            val specialVeggieCount = allMeals.count { it.categories!!.contains(Category.VEGGIE.category) && it.categories!!.contains(Category.SPECIAL.category) }
            val specialMeatCount = allMeals.count { it.categories!!.contains(Category.MEAT.category) && it.categories!!.contains(Category.SPECIAL.category) }
            val nonSpecialVeggieCount = allMeals.count { it.categories!!.contains(Category.VEGGIE.category) && !it.categories!!.contains(Category.SPECIAL.category) }
            val nonSpecialMeatCount = allMeals.count { it.categories!!.contains(Category.MEAT.category) && !it.categories!!.contains(Category.SPECIAL.category) }

            for (special in 0..requestMeals){
                val nonSpecial = requestMeals - special
                for (specialVeggie in 0 .. special) {
                    for (nonSpecialVeggie in 0 .. nonSpecial) {
                        val specialMeat = special - specialVeggie
                        val nonSpecialMeat = nonSpecial - nonSpecialVeggie

                        if (specialVeggie + nonSpecialVeggie >= requestVeggie && specialMeat + nonSpecialMeat >= requestMeat && specialVeggie + specialMeat >= requestSpecial && nonSpecialVeggie + nonSpecialMeat >= requestNonSpecial) {
                            if(specialVeggieCount >= specialVeggie && specialMeatCount >= specialMeat && nonSpecialVeggieCount >= nonSpecialVeggie && nonSpecialMeatCount >= nonSpecialMeat) {
                                val plan = PlanRaw(arrayListOf())
                                for (count in 1..specialVeggie) {
                                    plan.addCategory(TypeCategory(TypeVM.VEGGIE, TypeS.SPECIAL))
                                }
                                for (count in 1..nonSpecialVeggie) {
                                    plan.addCategory(TypeCategory(TypeVM.VEGGIE, TypeS.NON_SPECIAL))
                                }
                                for (count in 1..specialMeat) {
                                    plan.addCategory(TypeCategory(TypeVM.MEAT, TypeS.SPECIAL))
                                }
                                for (count in 1..nonSpecialMeat) {
                                    plan.addCategory(TypeCategory(TypeVM.MEAT, TypeS.NON_SPECIAL))
                                }
                                allPossiblePlans.add(plan)
                            }
                        }
                    }
                }
            }

            return allPossiblePlans
        }

//        //8 3 2 5 2
//        private fun getPossibleCombinations(requestVeggie: Int, requestMeat: Int, requestSpecial: Int, requestNonSpecial: Int): List<Plan> {
//
//            if (requestSpecial + requestNonSpecial > requestVeggie + requestMeat) {
//                for (special_veggie in 0 .. requestSpecial) {
//                    for (non_special_veggie in 0 .. requestNonSpecial) {
//                        val special_meat = requestSpecial - special_veggie
//                        val non_special_meat = requestNonSpecial - non_special_veggie
//
//                        //0sv 0nsv 5sm 2nsm
//
//                    }
//                }
//            }
//
//
//        }

        fun generatePlan(allMeals: List<Meal>, days: Int, mealsPerDay: Int, meat: Int, veggie: Int, special: Int, nonSpecial: Int): List<Meal> {
            val test = getPossibleCombinations(allMeals, days * mealsPerDay, veggie, meat, special, nonSpecial)

            if (days * mealsPerDay > allMeals.size){
                throw NotEnoughMealsException()
            }
//
//            if (special > veggieSpecialMeals.size + meatSpecialMeals.size || nonSpecial > veggieNonSpecialMeals.size + meatNonSpecialMeals.size || veggie > veggieSpecialMeals.size + veggieNonSpecialMeals.size|| meat > meatSpecialMeals.size + meatNonSpecialMeals.size) {
//                throw NotEnoughMealsException()
//            }
//
//
//            for (attempts in 1 .. GENERATION_ATTEMPTS){
//                for (meal_index in 1 .. days * mealsPerDay) {
//                    val mealSpecialCategory = chooseSpecialOrNonSpecial(specialLeft, nonSpecialLeft)
//                    val mealCategory = chooseMeatOrVeggie(meatLeft, veggieLeft)
//
//
//
//
//                    if (mealSpecialCategory == CATEGORIES.SPECIAL) {
//
//                    } else if (mealSpecialCategory == CATEGORIES.NON_SPECIAL) {
//
//                    }
//                }
//            }
//
//            throw NotEnoughMealsException()
            return emptyList()
        }
    }
}