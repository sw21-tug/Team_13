package com.team13.dealmymeal.ui.dashboard

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.team13.dealmymeal.MainActivity
import com.team13.dealmymeal.MealApplication
import com.team13.dealmymeal.R
import com.team13.dealmymeal.data.MealViewModel
import com.team13.dealmymeal.data.MealViewModelFactory

class DashboardFragment : Fragment() {

    private val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory((activity?.application as MealApplication).repository)
    }

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var pagerAdapter: PagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val pager = view.findViewById<ViewPager2>(R.id.pagerMeals)
        pagerAdapter = PagerAdapter()
        mealViewModel.allMeals.observe(viewLifecycleOwner) { meals ->
            // Update the cached copy of the words in the adapter.
            meals.let { pagerAdapter.submitList(it) }
        }
        pager.adapter = pagerAdapter

        val tabLayout = view.findViewById<TabLayout>(R.id.tabDotsMeals)
        TabLayoutMediator(tabLayout, pager) { tab, position ->
            //tab.text = "OBJECT ${(position + 1)}"
        }.attach()

        setHasOptionsMenu(true)

        // TODO check if plan exists -> context switch

        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.dashboard_menu, menu)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.language -> {
                    val languages = arrayOf("русский", "English")
                    val selectLanguageAlert = AlertDialog.Builder(context)
                    selectLanguageAlert.setTitle(R.string.chooseLanguage)
                    selectLanguageAlert.setSingleChoiceItems(languages, -1) { dialog, selection ->
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
                    selectLanguageAlert.create().show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}