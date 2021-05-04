package com.team13.dealmymeal.ui.editmeal

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.team13.dealmymeal.*


class EditMealFragment : Fragment() {
    private val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory((activity?.application as MealApplication).repository)
    }
    private lateinit var editMealViewModel: EditMealViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        editMealViewModel =
                ViewModelProvider(this).get(EditMealViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_editmeal, container, false)


        return root
    }

}




