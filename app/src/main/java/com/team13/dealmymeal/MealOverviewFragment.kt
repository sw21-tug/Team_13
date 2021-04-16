package com.team13.dealmymeal

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.team13.dealmymeal.dummy.DummyContent

/**
 * A fragment representing a list of Items.
 */
class MealOverviewFragment : Fragment() {

    private var columnCount = 1

    private var tracker: SelectionTracker<String>? = null

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

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                // TODO hier items von db
                adapter = MealOverviewAdapter(DummyContent.ITEMS)

                tracker = SelectionTracker.Builder<String>(
                    "mySelection",
                    view,
                    MealOverviewAdapter.MyItemKeyProvider(adapter as MealOverviewAdapter),
                    MealOverviewAdapter.MyItemDetailsLookup(view),
                    StorageStrategy.createStringStorage()
                ).withSelectionPredicate(
                    SelectionPredicates.createSelectAnything()
                ).build()

                (adapter as MealOverviewAdapter).tracker = tracker
            }
        }
        return view
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