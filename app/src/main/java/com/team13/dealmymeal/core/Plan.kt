package com.team13.dealmymeal.core

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.team13.dealmymeal.data.Category
import com.team13.dealmymeal.data.Meal

@Entity(tableName = "plan")
class Plan(
    @ColumnInfo(name = "period") var period: Int,
    @ColumnInfo(name = "meals_per_day") var mealsPerDay: Int,
    @ColumnInfo(name = "created_time", defaultValue = "CURRENT_TIMESTAMP") var createdTime: String,
    @ColumnInfo(name = "meals") var meals: List<Long>?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

    /*
    fun getMeatCount(): Int {
        return meals!!.count { it.categories!!.contains(Category.MEAT.category) }
    }

    fun getVeggieCount(): Int {
        return meals!!.count { it.categories!!.contains(Category.VEGGIE.category) }
    }

    fun getSpecialCount(): Int {
        return meals!!.count { it.categories!!.contains(Category.SPECIAL.category) }
    }*/
}