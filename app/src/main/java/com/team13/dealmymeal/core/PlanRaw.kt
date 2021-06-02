package com.team13.dealmymeal.core

class PlanRaw(var categoryList: ArrayList<TypeCategory>) {
    fun addCategory(category: TypeCategory) {
        categoryList.add(category)
    }
}