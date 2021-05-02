package com.team13.dealmymeal.ui.home

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.team13.dealmymeal.R




class AddMealFragment : Fragment() {

    private lateinit var addMealViewModel: AddMealViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        addMealViewModel =
                ViewModelProvider(this).get(AddMealViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_addmeal, container, false)

        AppCompatActivity()
        //val view = inflater.inflate(R.layout.fragment_addmeal, container, true)

        val save_button = root.findViewById<Button>(R.id.form_save)
        val form_editText = root.findViewById<EditText>(R.id.form_edit)
        val text = form_editText.text
        save_button.setOnClickListener() {
            val toast: Toast =
                Toast.makeText(context, "Meal " + text + " added!", Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 0, 250)
            toast.show()


        }

        return root
    }

}




