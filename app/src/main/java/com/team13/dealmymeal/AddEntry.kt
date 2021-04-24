package com.team13.dealmymeal

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddEntry.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddEntry : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


            }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        AppCompatActivity()
        val view = inflater.inflate(R.layout.fragment_add_entry, container, false)


        val form_textview: TextView = view.findViewById(R.id.form_showEntry)
        val save_button: Button = view.findViewById(R.id.form_save)
        val cancel_button: Button = view.findViewById(R.id.form_cancel)
        val form_editText: EditText = view.findViewById(R.id.form_edit)
        val text = form_editText.text
        save_button.setOnClickListener() {
            val toast: Toast =
                Toast.makeText(context, "Meal " + text + " added!", Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 0, 250)
            toast.show()

            form_textview.setText(text)

        }


        cancel_button.setOnClickListener() { v ->
            val result = "cancel"
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
            activity?.onBackPressed()
        }






        return view
    }


}