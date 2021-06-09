package com.team13.dealmymeal.util

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.Nullable
import com.team13.dealmymeal.R


class HorizontalNumberPicker(context: Context?, @Nullable attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private val et_number: EditText?
    var min = 0
    var max = 0

    /***
     * HANDLERS
     */
    private inner class AddHandler(val diff: Int) : OnClickListener {
        override fun onClick(v: View?) {
            var newValue = value + diff
            if (newValue < min) {
                newValue = min
            } else if (newValue > max) {
                newValue = max
            }
            et_number!!.setText(newValue.toString())
        }
    }

    /***
     * GETTERS & SETTERS
     */
    var value: Int
        get() {
            if (et_number != null) {
                try {
                    val value = et_number.text.toString()
                    return value.toInt()
                } catch (ex: NumberFormatException) {
                    Log.e("HorizontalNumberPicker", ex.toString())
                }
            }
            return 0
        }
        set(value) {
            et_number?.setText(value.toString())
        }

    init {
        inflate(context, R.layout.numberpicker_horizontal, this)
        et_number = findViewById(R.id.et_number)
        val btn_less: Button = findViewById(R.id.btn_less)
        btn_less.setOnClickListener(AddHandler(-1))
        val btn_more: Button = findViewById(R.id.btn_more)
        btn_more.setOnClickListener(AddHandler(1))
    }
}