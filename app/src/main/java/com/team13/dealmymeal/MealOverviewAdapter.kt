package com.team13.dealmymeal

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker

import com.team13.dealmymeal.dummy.DummyContent.DummyItem

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MealOverviewAdapter(
    private val values: List<String>
) : RecyclerView.Adapter<MealOverviewAdapter.ViewHolder>() {

    var tracker: SelectionTracker<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_meal_overview, parent, false)

        view.setOnClickListener {
            //TODO edit
            Toast.makeText(parent.context, "Edit", Toast.LENGTH_SHORT).show()

            view.findViewById<TextView>(R.id.item_name).setTextColor(Color.RED)
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item

        tracker?.let {
            holder.setItemSelected(values[position], it.isSelected(values[position]))
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_name)

        override fun toString(): String {
            return super.toString()
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<String> =
            object : ItemDetailsLookup.ItemDetails<String>() {
                override fun getPosition(): Int = position
                override fun getSelectionKey(): String? = values[position]
            }

        fun setItemSelected(postItem: String, isActivated: Boolean = false) {
            itemView.isActivated = isActivated
        }
    }

    class MyItemKeyProvider(private val adapter: MealOverviewAdapter) :
        ItemKeyProvider<String>(SCOPE_CACHED) {
        override fun getKey(position: Int): String? {
            return adapter.values[position]
        }

        override fun getPosition(key: String): Int {
            return adapter.values.indexOfFirst { it == key }
        }
    }


    //TODO is this needed? -->listener
    class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
        ItemDetailsLookup<String>() {
        override fun getItemDetails(event: MotionEvent): ItemDetails<String>? {
            val view = recyclerView.findChildViewUnder(event.x, event.y)
            if (view != null) {
                return (recyclerView.getChildViewHolder(view) as MealOverviewAdapter.ViewHolder)
                    .getItemDetails()
            }
            return null
        }
    }
}