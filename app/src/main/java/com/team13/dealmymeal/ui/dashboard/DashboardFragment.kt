package com.team13.dealmymeal.ui.dashboard

import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.team13.dealmymeal.MainActivity
import com.team13.dealmymeal.R

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val languageButton: Button = root.findViewById(R.id.changeLanguageButton)
        languageButton.setOnClickListener() {
            val languages = arrayOf("русский", "English")
            val langSelectorBuilder = AlertDialog.Builder(context)
            langSelectorBuilder.setTitle(R.string.chooseLanguage)
            langSelectorBuilder.setSingleChoiceItems(languages, -1) { dialog, selection ->
                when(selection) {
                    0 -> {
                        (activity as MainActivity?)!!.setLocale("ru")
                    }
                    1 -> {
                        (activity as MainActivity?)!!.setLocale("en")
                    }

                }
                (activity as MainActivity?)!!.recreate()
                dialog.dismiss()
            }
            langSelectorBuilder.create().show()

        }

        return root
    }
}