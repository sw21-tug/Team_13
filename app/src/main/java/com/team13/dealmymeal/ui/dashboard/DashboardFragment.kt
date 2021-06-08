package com.team13.dealmymeal.ui.dashboard

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.team13.dealmymeal.MainActivity
import com.team13.dealmymeal.MealApplication
import com.team13.dealmymeal.R
import com.team13.dealmymeal.data.Category
import com.team13.dealmymeal.data.MealViewModel
import com.team13.dealmymeal.data.MealViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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
        val layoutEmpty = view.findViewById<ConstraintLayout>(R.id.layoutPlanEmpty)
        val layoutPlan = view.findViewById<ConstraintLayout>(R.id.layoutPlan)

        val pager = view.findViewById<ViewPager2>(R.id.pagerMeals)
        pagerAdapter = PagerAdapter()

        mealViewModel.currentPlan.observe(viewLifecycleOwner) { plan ->
            if(plan == null) {
                layoutEmpty.visibility = View.VISIBLE
                layoutPlan.visibility = View.GONE
            } else {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val sdfPlan = SimpleDateFormat("dd.MM", Locale.getDefault())
                val creationTime = sdf.parse(plan.createdTime)
                val calendar = Calendar.getInstance()
                val time = System.currentTimeMillis()
                calendar.time = creationTime!!
                calendar.add(Calendar.DATE, plan.period)
                if (creationTime.time <= time && time <= calendar.time.time) {
                    layoutEmpty.visibility = View.GONE
                    layoutPlan.visibility = View.VISIBLE

                    // Felder befüllen
                    val chipGroupDays = view.findViewById<ChipGroup>(R.id.chipGroupDays)
                    val labelPlan = view.findViewById<TextView>(R.id.labelPlan)
                    val labelMeatCount = view.findViewById<TextView>(R.id.labelMeatCount)
                    val labelVeggieCount = view.findViewById<TextView>(R.id.labelVeggieCount)
                    val labelSpecialCount = view.findViewById<TextView>(R.id.labelSpecialCount)
                    val chipCurrentDay = view.findViewById<Chip>(R.id.chipCurrentDay)
                    val ratingAvg = view.findViewById<RatingBar>(R.id.ratingAvg)

                    val currentDay = TimeUnit.DAYS.convert(time - creationTime.time, TimeUnit.MILLISECONDS) + 1

                    for (i in 1..plan.period){
                        val chip = layoutInflater.inflate(R.layout.chip_day, null, false) as Chip
                        chip.text = "Day $i"
                        chip.isChecked = currentDay == i.toLong()
                        chipGroupDays.addView(chip)
                    }

                    labelPlan.text = "Current Plan: ${sdfPlan.format(creationTime.time)}-${sdfPlan.format(calendar.time.time)}"

                    mealViewModel.allMeals.observe(viewLifecycleOwner) { meals ->
                        val planMeals = meals.filter { plan.meals!!.contains(it.id) }
                        pagerAdapter.submitList(planMeals)

                        labelMeatCount.text = planMeals.count { it.categories!!.contains(Category.MEAT.category) }.toString()
                        labelVeggieCount.text = planMeals.count { it.categories!!.contains(Category.VEGGIE.category) }.toString()
                        labelSpecialCount.text = planMeals.count { it.categories!!.contains(Category.SPECIAL.category) }.toString()

                        var r = 0.0
                        for (m in planMeals)
                            r += m.rating!!
                        ratingAvg.rating = (r/planMeals.size).toFloat()
                    }

                    chipCurrentDay.text = "Day $currentDay"
                } else {
                    layoutEmpty.visibility = View.VISIBLE
                    layoutPlan.visibility = View.GONE
                }
            }
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