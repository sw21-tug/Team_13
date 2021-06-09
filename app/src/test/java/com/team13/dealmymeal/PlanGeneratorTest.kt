package com.team13.dealmymeal

import com.google.android.material.chip.Chip
import com.team13.dealmymeal.core.NotEnoughMealsException
import com.team13.dealmymeal.core.PlanGenerator
import com.team13.dealmymeal.data.Category
import com.team13.dealmymeal.data.Meal
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class PlanGeneratorTest {
    private lateinit var mealList: List<Meal>

    @Before
    fun fillList() {
        val meal1 = Meal("MealMeat1", listOf(Category.MEAT.category), 1.0f)
        val meal2 = Meal("MealMeat2", listOf(Category.MEAT.category), 2.0f)
        val meal3 = Meal("MealMeatSpecial", listOf(Category.MEAT.category, Category.SPECIAL.category), 3.0f)
        val meal4 = Meal("MealVegetarian1", listOf(Category.VEGGIE.category), 4.0f)
        val meal5 = Meal("MealVegetarian2", listOf(Category.VEGGIE.category), 5.0f)
        val meal6 = Meal("MealVegetarianSpecial", listOf(Category.VEGGIE.category, Category.SPECIAL.category), 5.0f)

        mealList = listOf(meal1, meal2, meal3, meal4, meal5, meal6)

        var i = 0L
        for (meal in mealList)
            meal.id = ++i

    }

    @Test
    fun planNotPossibleToLessSpecial() {
        assertThrows(NotEnoughMealsException::class.java) {
            PlanGenerator.generatePlan(mealList, 2, 2, 3)
        }
    }

    @Test
    fun planNotPossibleToLessMeat() {
        assertThrows(NotEnoughMealsException::class.java) {
            PlanGenerator.generatePlan(mealList, 4, 0, 1)
        }
    }

    @Test
    fun generatePlan() {
        val expectedCountMeat = 3
        val expectedCountVeggie = 3
        val expectedCountSpecial = 2
        val planRaw = PlanGenerator.generatePlan(mealList, expectedCountMeat, expectedCountVeggie, expectedCountSpecial)

        val planMeals = mealList.filter { planRaw.contains(it.id) }
        val countMeat = planMeals.count { it.categories!!.contains(Category.MEAT.category) }
        val countVeggie = planMeals.count { it.categories!!.contains(Category.VEGGIE.category) }
        val countSpecial = planMeals.count { it.categories!!.contains(Category.SPECIAL.category) }

        assertEquals(expectedCountMeat, countMeat)
        assertEquals(expectedCountVeggie, countVeggie)
        assertEquals(expectedCountSpecial, countSpecial)
    }

    @Test
    fun generatePlanRandom() {
        val expectedCountMeat = 1
        val expectedCountVeggie = 1
        val expectedCountSpecial = 1
        val planRaw = PlanGenerator.generatePlan(mealList, expectedCountMeat, expectedCountVeggie, expectedCountSpecial)

        val planMeals = mealList.filter { planRaw.contains(it.id) }
        val countMeat = planMeals.count { it.categories!!.contains(Category.MEAT.category) }
        val countVeggie = planMeals.count { it.categories!!.contains(Category.VEGGIE.category) }
        val countSpecial = planMeals.count { it.categories!!.contains(Category.SPECIAL.category) }

        assertEquals(expectedCountMeat, countMeat)
        assertEquals(expectedCountVeggie, countVeggie)
        assertEquals(expectedCountSpecial, countSpecial)

    }

}