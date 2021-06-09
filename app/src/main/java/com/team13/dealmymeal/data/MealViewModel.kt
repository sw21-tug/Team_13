package com.team13.dealmymeal.data

import androidx.lifecycle.*
import com.team13.dealmymeal.core.Plan
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MealViewModel(private val repository: MealRepository) : ViewModel() {
    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allMeals: LiveData<List<Meal>> = repository.allMeals.asLiveData()
    val currentPlan: LiveData<Plan> = repository.currentPlan.asLiveData()

    suspend fun getCount(title: String): Int {
        return repository.getCountMeals(title)
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insertMeal(meal: Meal) = viewModelScope.launch {
        repository.insertMeal(meal)
    }

    fun deleteMeal(meal: Meal) = viewModelScope.launch {
        repository.deleteMeal(meal)
    }

    fun deleteMealById(id: Long) = viewModelScope.launch {
        repository.deleteMealById(id)
    }

    fun deleteAllMeals() = viewModelScope.launch {
        repository.deleteAllMeals()
    }

    fun updateMeal(meal: Meal) = viewModelScope.launch {
        repository.updateMeal(meal)
    }

    fun deletePlanWithId(id: Long) = viewModelScope.launch {
        repository.deletePlanWithId(id)
    }

    fun deleteAllPlans() = viewModelScope.launch {
        repository.deleteAllPlans()
    }
}

class MealViewModelFactory(private val repository: MealRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MealViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}