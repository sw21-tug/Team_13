package com.team13.dealmymeal.ui.overview

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team13.dealmymeal.*

/**
 * A fragment representing a list of Items.
 */
class MealOverviewFragment : Fragment(), ActionMode.Callback {
    private val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory((activity?.application as MealApplication).repository)
    }

    private var columnCount = 1

    private var tracker: SelectionTracker<Meal>? = null

    private var selectedPostItems: MutableList<Meal> = mutableListOf()
    private var actionMode: ActionMode? = null
    private var overviewAdapter: MealOverviewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meal_overview_list, container, false)

        setHasOptionsMenu(true)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                adapter =
                    MealOverviewAdapter(
                        ArrayList()
                        //DummyContent.generateDummyList(15)
                    )

                tracker = SelectionTracker.Builder<Meal>(
                    "mySelection",
                    view,
                    MealOverviewAdapter.MyItemKeyProvider(
                        adapter as MealOverviewAdapter
                    ),
                    MealOverviewAdapter.MyItemDetailsLookup(
                        view
                    ),
                    StorageStrategy.createParcelableStorage(Meal::class.java)
                ).withSelectionPredicate(
                    SelectionPredicates.createSelectAnything()
                ).build()

                (adapter as MealOverviewAdapter).tracker = tracker

                tracker?.addObserver(
                    object : SelectionTracker.SelectionObserver<Meal>() {
                        override fun onSelectionChanged() {
                            super.onSelectionChanged()
                            tracker?.let {
                                selectedPostItems = it.selection.toMutableList()

                                // TODO enable this when implementing delete
                                /*
                                if (selectedPostItems.isEmpty()) {
                                    actionMode?.finish()
                                } else {
                                    if (actionMode == null) actionMode = parent.startActionModeForChild(view, this@MealOverviewFragment)
                                    actionMode?.title =
                                        "${selectedPostItems.size}"
                                }*/

                                // TODO delete
                            }
                        }
                    })
                overviewAdapter = adapter as MealOverviewAdapter

                // Add an observer on the LiveData returned by getAll.
                // The onChanged() method fires when the observed data changes and the activity is
                // in the foreground.
                mealViewModel.allMeals.observe(viewLifecycleOwner) { meals ->
                    // Update the cached copy of the words in the adapter.
                    meals.let { overviewAdapter!!.submitList(it) }
                }
                //Log.d("MOF", "calling coroutine")
                //main(context, overviewAdapter!!).invoke()
            }
        }
        mealViewModel.deleteAll()
        mealViewModel.insert(Meal("Test1"))
        mealViewModel.insert(Meal("Test2"))
        return view
    }

    /*
    private fun main(context: Context, adapter: MealOverviewAdapter): () -> Job =  {
        lifecycleScope.launch { // coroutine on Main
            getDBEntries(context, adapter)
        }
    }

    private fun getDBEntries(context: Context, adapter: MealOverviewAdapter) { // this: CoroutineScope
        Log.d("MOF", "Adding test meal")
        DBManager.invoke(context).mealDao().insertAll(Meal("Test"))

        var listMeals: MutableList<MealItem> = ArrayList()
        for (l in DBManager.invoke(context).mealDao().getAll()) {
            Log.d("MOF", l.title)
            listMeals + MealItem(l.title, 0, 0)
        }
        adapter.addItems(listMeals)
    }*/

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_view_delete -> {
                Toast.makeText(
                    context,
                    selectedPostItems.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        return true
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.let {
            val inflater: MenuInflater = it.menuInflater
            inflater.inflate(R.menu.delete, menu)
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

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            MealOverviewFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
