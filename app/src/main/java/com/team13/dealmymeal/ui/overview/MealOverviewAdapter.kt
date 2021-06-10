package com.team13.dealmymeal.ui.overview

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.team13.dealmymeal.data.Meal
import com.team13.dealmymeal.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


/**
 * [ListAdapter] that can display a [Meal].
 *
 */
class MealOverviewAdapter(
        private var valuesOriginal: MutableList<Meal>,
        private val listener: OnItemClickListener
) : ListAdapter<Meal, MealOverviewAdapter.ViewHolder>(MEAL_COMPARATOR), Filterable {

    var tracker: SelectionTracker<Meal>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_meal_overview, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        tracker?.let {
            holder.setItemSelected(it.isSelected(getItem(position)))
        }

        val current = getItem(position)
        holder.bind(current, position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val txtTitle: TextView = view.findViewById(R.id.labelMeal)
        private val imgBackgroundMeal: ImageView = view.findViewById(R.id.imgCardBackground)
        private val cardMeal: MaterialCardView = view.findViewById(R.id.cardMeal)
        private val chips: ChipGroup = view.findViewById(R.id.chipGroupCategories)
        private val ratingBar: RatingBar = view.findViewById(R.id.ratingMeal)
        val context: Context = view.context

        init {
            itemView.setOnClickListener(this)
        }

        override fun toString(): String {
            return super.toString()
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Meal> =
            object : ItemDetailsLookup.ItemDetails<Meal>() {
                override fun getPosition(): Int = bindingAdapterPosition
                override fun getSelectionKey(): Meal? = getItem(bindingAdapterPosition)
            }

        fun setItemSelected(isSelected: Boolean = false) {
            cardMeal.isSelected = isSelected
        }

        fun bind(meal: Meal, position: Int) {
            txtTitle.text = meal.title

            val typedValue = TypedValue()
            when (position % 2) {
                0 -> context.theme.resolveAttribute(R.attr.overview_item_1, typedValue, true)
                1 -> context.theme.resolveAttribute(R.attr.overview_item_2, typedValue, true)
            }
            imgBackgroundMeal.setImageResource(typedValue.resourceId)

            chips.removeAllViews()
            val categoryNames = context.resources.getStringArray(R.array.categories)
            val categoryIcons = context.resources.obtainTypedArray(R.array.icons_category)
            for (category in meal.categories!!){
                val chip = Chip(context)
                chip.text = categoryNames[category]
                chip.setChipBackgroundColorResource(R.color.green)
                chip.setTextColor(Color.WHITE)
                chip.setChipIconResource(categoryIcons.getResourceId(category, 0))
                chip.setChipIconTintResource(R.color.chip_icon_tint)
                chips.addView(chip)
            }
            categoryIcons.recycle()

            ratingBar.rating = meal.rating!!
        }
    }

    class MealItemKeyProvider(private val adapter: MealOverviewAdapter) :
        ItemKeyProvider<Meal>(SCOPE_CACHED) {
        override fun getKey(position: Int): Meal? {
            return adapter.getItem(position)
        }

        override fun getPosition(key: Meal): Int {
            return adapter.currentList.indexOf(key)//adapter.getI.indexOfFirst { it.name == key.title }
        }
    }

    class MealItemDetailsLookup(private val recyclerView: RecyclerView) :
        ItemDetailsLookup<Meal>() {
        override fun getItemDetails(event: MotionEvent): ItemDetails<Meal>? {
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
                submitList(results.values as MutableList<Meal>?)
            }

            override fun performFiltering(constraint: CharSequence): FilterResults {
                if(currentList.size >= valuesOriginal.size)
                    valuesOriginal = currentList
                val filteredResults = if (constraint.isEmpty()) {
                    valuesOriginal
                } else {
                    getFilteredResults(constraint.toString().toLowerCase(Locale.getDefault()))
                }
                val results = FilterResults()
                results.values = filteredResults
                return results
            }
        }
    }

    private fun getFilteredResults(constraint: String?): List<Meal?> {
        val results: MutableList<Meal?> = ArrayList()
        for (item in valuesOriginal) {
            if (constraint?.let { item.title?.toLowerCase(Locale.getDefault())?.contains(it) } == true) {
                results.add(item)
            }
        }
        return results
    }

    private fun filterCategory(list: MutableList<Meal?>, categories: List<Int>): MutableList<Meal?> {
        val results: MutableList<Meal?> = ArrayList()
        for (item in list) {
            if (item?.categories?.containsAll(categories) == true) {
                results.add(item)
            }
        }
        return results
    }

    private fun filterRating(list: MutableList<Meal?>, ratings: List<Float>): MutableList<Meal?> {
        val results: MutableList<Meal?> = ArrayList()
        for (item in list) {
            if (ratings.minOrNull()?.roundToInt()!! <= item?.rating!!.roundToInt()
                &&  item.rating!!.roundToInt()  <= ratings.maxOrNull()?.roundToInt()!!)
                results.add(item)
        }
        return results
    }

    fun applyFilter(categories: List<Int>, ratings: List<Float>) {
        if(currentList.size >= valuesOriginal.size)
            valuesOriginal = currentList

        val results: MutableList<Meal?> = filterCategory(filterRating(currentList, ratings), categories)
        submitList(results)
    }

    fun resetFilter() {
        submitList(valuesOriginal)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    companion object {
        private val MEAL_COMPARATOR = object : DiffUtil.ItemCallback<Meal>() {
            override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
                return oldItem == newItem
            }
        }
    }
}