package com.team13.dealmymeal.ui.overview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.team13.dealmymeal.MealItem
import com.team13.dealmymeal.R

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * 
 */
class MealOverviewAdapter(
    private var valuesOriginal: List<MealItem>,
    private var values: List<MealItem> = valuesOriginal
) : RecyclerView.Adapter<MealOverviewAdapter.ViewHolder>(), Filterable {

    var tracker: SelectionTracker<MealItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_meal_overview, parent, false)

        view.setOnClickListener {
            //TODO edit
            //TODO check if we are in selection -> abort
            if(tracker?.hasSelection() == false) {
                Toast.makeText(parent.context, "Edit", Toast.LENGTH_SHORT).show()
                view.findViewById<TextView>(R.id.item_name).setTextColor(Color.RED)
            }
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.name

        tracker?.let {
            holder.setItemSelected(values[position], it.isSelected(values[position]))
        }
    }

    override fun getItemCount(): Int = values.size

    fun addItems(items: List<MealItem>) {
        valuesOriginal += items
        values = valuesOriginal
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_name)

        override fun toString(): String {
            return super.toString()
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<MealItem> =
            object : ItemDetailsLookup.ItemDetails<MealItem>() {
                override fun getPosition(): Int = bindingAdapterPosition
                override fun getSelectionKey(): MealItem? = values[bindingAdapterPosition]
            }

        fun setItemSelected(postItem: MealItem, isSelected: Boolean = false) {
            itemView.isSelected = isSelected
        }
    }

    class MyItemKeyProvider(private val adapter: MealOverviewAdapter) :
        ItemKeyProvider<MealItem>(SCOPE_CACHED) {
        override fun getKey(position: Int): MealItem? {
            return adapter.values[position]
        }

        override fun getPosition(key: MealItem): Int {
            return adapter.values.indexOfFirst { it.name == key.name }
        }
    }


    //TODO is this needed? -->listener
    class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
        ItemDetailsLookup<MealItem>() {
        override fun getItemDetails(event: MotionEvent): ItemDetails<MealItem>? {
            val view = recyclerView.findChildViewUnder(event.x, event.y)
            if (view != null) {
                return (recyclerView.getChildViewHolder(view) as ViewHolder)
                    .getItemDetails()
            }
            return null
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                values = results.values as List<MealItem>
                notifyDataSetChanged()
            }

            override fun performFiltering(constraint: CharSequence): FilterResults {
                var filteredResults: List<MealItem?>? = null
                filteredResults = if (constraint.isEmpty()) {
                    valuesOriginal
                } else {
                    getFilteredResults(constraint.toString().toLowerCase())
                }
                val results = FilterResults()
                results.values = filteredResults
                return results
            }
        }
    }

    private fun getFilteredResults(constraint: String?): List<MealItem?> {
        val results: MutableList<MealItem?> = ArrayList()
        for (item in valuesOriginal) {
            if (constraint?.let { item.name?.toLowerCase()?.contains(it) } == true) {
                results.add(item)
            }
        }
        return results
    }
}