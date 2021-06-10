package com.team13.dealmymeal.ui.dashboard

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.team13.dealmymeal.R
import com.team13.dealmymeal.data.Meal


class PagerAdapter(): ListAdapter<Meal, PagerAdapter.ViewHolder>(MEAL_COMPARATOR) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_meal_overview, parent, false)
        view.layoutParams =
            ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagerAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtTitle: TextView = view.findViewById(R.id.labelMeal)
        private val imgBackgroundMeal: ImageView = view.findViewById(R.id.imgCardBackground)
        private val chips: ChipGroup = view.findViewById(R.id.chipGroupCategories)
        private val ratingBar: RatingBar = view.findViewById(R.id.ratingMeal)
        val context: Context = view.context

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