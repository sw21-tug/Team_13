package com.team13.dealmymeal.ui.overview

import android.app.AlertDialog
import android.database.DataSetObserver
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.RangeSlider
import com.team13.dealmymeal.*
import com.team13.dealmymeal.data.Meal
import com.team13.dealmymeal.data.MealViewModel
import com.team13.dealmymeal.data.MealViewModelFactory
import kotlin.math.roundToInt



/**
 * A fragment representing a list of Items.
 */
class MealOverviewFragment : Fragment(), ActionMode.Callback, SearchView.OnQueryTextListener, MealOverviewAdapter.OnItemClickListener {
    private val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory((activity?.application as MealApplication).repository)
    }

    private var columnCount = 1
    private var tracker: SelectionTracker<Meal>? = null
    private var selectedPostItems: MutableList<Meal> = mutableListOf()
    private var actionMode: ActionMode? = null
    private var overviewAdapter: MealOverviewAdapter? = null
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meal_overview_list, container, false)

        setHasOptionsMenu(true)

        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
        val emptyView = view.findViewById<View>(R.id.emptyView)
        // Set the adapter
        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }

            adapter =
                MealOverviewAdapter(
                    ArrayList(), this@MealOverviewFragment
                )

            tracker = SelectionTracker.Builder(
                "mySelection",
                recyclerView,
                MealOverviewAdapter.MealItemKeyProvider(
                    adapter as MealOverviewAdapter
                ),
                MealOverviewAdapter.MealItemDetailsLookup(
                    recyclerView
                ),
                StorageStrategy.createParcelableStorage(Meal::class.java)
            ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
            ).build()

            tracker?.addObserver(
                object : SelectionTracker.SelectionObserver<Meal>() {
                    override fun onSelectionChanged() {
                        super.onSelectionChanged()
                        tracker?.let {
                            selectedPostItems = it.selection.toMutableList()

                            if (selectedPostItems.isEmpty()) {
                                actionMode?.finish()
                            } else {
                                if (actionMode == null) actionMode = parent.startActionModeForChild(view, this@MealOverviewFragment)
                                actionMode?.title =
                                    "${selectedPostItems.size}"
                            }
                        }
                    }
                })

            overviewAdapter = adapter as MealOverviewAdapter
            overviewAdapter!!.tracker = tracker
            // Add an observer on the LiveData returned by getAll.
            // The onChanged() method fires when the observed data changes and the activity is
            // in the foreground.
            mealViewModel.allMeals.observe(viewLifecycleOwner) { meals ->
                // Update the cached copy of the words in the adapter.
                meals.let { overviewAdapter!!.submitList(it) }
                emptyView.visibility = if(meals.isEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
            //Log.d("MOF", "calling coroutine")
            //main(context, overviewAdapter!!).invoke()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_view_delete -> {

                val builder = AlertDialog.Builder(context)
                builder.setMessage(R.string.delete_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes) { _, _ ->
                            // Delete selected note from database
                            for (meal in selectedPostItems)
                                mealViewModel.deleteMeal(meal)
                        }
                        .setNegativeButton(R.string.no) { dialog, _ ->
                            // Dismiss the dialog
                            dialog.dismiss()
                        }
                val alert = builder.create()
                alert.show()

            }
        }
        return true
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.let {
            val inflater: MenuInflater = it.menuInflater
            inflater.inflate(R.menu.overview_selection_menu, menu)
            return true
        }
        return false
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        overviewAdapter?.tracker?.clearSelection()
        overviewAdapter?.notifyDataSetChanged()
        actionMode = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overview_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            R.id.action_filter -> {
                if (!item.isChecked)
                {
                    val builder = AlertDialog.Builder(context, R.style.RoundedCornersDialog)
                    val view = layoutInflater.inflate(R.layout.dialog_filter_meals, null)
                    val chipGroup = view.findViewById<ChipGroup>(R.id.chipsCategory)
                    val ratingRange = view.findViewById<RangeSlider>(R.id.sliderRating)
                    val textSliderRight = view.findViewById<TextView>(R.id.textSliderRight)
                    val textSliderLeft = view.findViewById<TextView>(R.id.textSliderLeft)

                    ratingRange.addOnChangeListener { rangeSlider, value, fromUser ->
                        // Responds to when slider's value is changed
                        textSliderLeft.text = rangeSlider.values.minOrNull()?.roundToInt().toString()
                        textSliderRight.text = rangeSlider.values.maxOrNull()?.roundToInt().toString()
                    }

                    builder.setView(view)
                    //builder.setTitle("Apply filter")
                    builder.setIcon(R.drawable.ic_filter)
                    builder.setPositiveButton(getString(R.string.apply)) { dialog, selection ->

                        val categories = ArrayList<Int>()
                        for(ids in chipGroup.checkedChipIds) {
                            categories.add(resources.getStringArray(R.array.categories).indexOf(chipGroup.findViewById<Chip>(ids).text))
                        }

                        overviewAdapter?.applyFilter(categories, ratingRange.values)

                        item.isChecked  = true
                        item.setIcon(R.drawable.ic_close)
                        dialog.dismiss()
                    }
                    val dialog = builder.create()
                    dialog.setOnShowListener {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setTextColor(resources.getColor(R.color.green, context?.theme))
                    }
                    dialog.show()


                }else{
                    overviewAdapter?.resetFilter()
                    item.isChecked = false
                    item.setIcon(R.drawable.ic_filter)
                }
                true
            }
            else -> false
        }
    }

    override fun onQueryTextChange(query: String?): Boolean {
        // Here is where we are going to implement the filter logic
        overviewAdapter?.filter?.filter(query)
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onItemClick(position: Int) {
        val clickedMeal = overviewAdapter!!.currentList[position]

        val bundle = bundleOf("Meal" to clickedMeal)
        navController.navigate(R.id.action_navigation_overview_to_editMealFragment, bundle)

        overviewAdapter?.notifyItemChanged(position)
    }

}
