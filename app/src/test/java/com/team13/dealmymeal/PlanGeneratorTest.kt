package com.team13.dealmymeal

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
    }

    @Test
    fun planNotPossibleToManyDays() {
        assertThrows(NotEnoughMealsException::class.java) {
            PlanGenerator.generatePlan(mealList,7, 1, 2, 2, 2, 0)
        }
    }

    @Test
    fun planNotPossibleToManyMealsPerDay() {
        assertThrows(NotEnoughMealsException::class.java) {
            PlanGenerator.generatePlan(mealList, 4, 2, 2, 2, 2, 0)
        }
    }

    @Test
    fun planNotPossibleToLessSpecial() {
        assertThrows(NotEnoughMealsException::class.java) {
            PlanGenerator.generatePlan(mealList, 5, 1, 2, 2, 3, 0)
        }
    }

    @Test
    fun planNotPossibleToLessMeat() {
        assertThrows(NotEnoughMealsException::class.java) {
            PlanGenerator.generatePlan(mealList, 5, 1, 5, 2, 2, 0)
        }
    }

    @Test
    fun generatePlan() {
        val expectedCountMeat = 3
        val expectedCountVeggie = 3
        val expectedCountSpecial = 2
        val expectedCountNonSpecial = 0
        val days = 3
        val mealsPerDay = 2
        val plan = PlanGenerator.generatePlan(mealList, days, mealsPerDay, expectedCountMeat, expectedCountVeggie, expectedCountSpecial, expectedCountNonSpecial)

        val countMeat = plan.count { it.categories!!.contains(Category.MEAT.category) }
        val countVeggie = plan.count { it.categories!!.contains(Category.VEGGIE.category) }
        val countSpecial = plan.count { it.categories!!.contains(Category.SPECIAL.category) }

        assertEquals(expectedCountMeat, countMeat)
        assertEquals(expectedCountVeggie, countVeggie)
        assertEquals(expectedCountSpecial, countSpecial)

    }

    @Test
    fun generatePlanRandom() {
        val expectedCountMeat = 1
        val expectedCountVeggie = 1
        val expectedCountSpecial = 0
        val expectedCountNonSpecial = 3
        val days = 3
        val mealsPerDay = 1
        val plan = PlanGenerator.generatePlan(mealList, days, mealsPerDay, expectedCountMeat, expectedCountVeggie, expectedCountSpecial, expectedCountNonSpecial)

        val countMeat = plan.count { it.categories!!.contains(Category.MEAT.category) }
        val countVeggie = plan.count { it.categories!!.contains(Category.VEGGIE.category) }
        val countSpecial = plan.count { it.categories!!.contains(Category.SPECIAL.category) }

        assertTrue(expectedCountMeat <= countMeat)
        assertTrue(expectedCountVeggie <= countVeggie)
        assertTrue(expectedCountSpecial <= countSpecial)

    }

}