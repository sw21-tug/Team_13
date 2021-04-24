package com.team13.dealmymeal

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity



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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)






                }


            }






    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        AppCompatActivity()
        val view = inflater.inflate(R.layout.fragment_add_entry, container, false)



        val form_textview: TextView = view.findViewById(R.id.form_showEntry)
        val save_button: Button = view.findViewById(R.id.form_save)
        val cancel_button: Button = view.findViewById(R.id.form_cancel)
        val form_editText: EditText = view.findViewById(R.id.form_edit)
        val text = form_editText.text
        save_button.setOnClickListener() {
               // v ->

            val toast: Toast =
                Toast.makeText(context, "Meal " + text + " added!", Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 0, 250)
            toast.show()

            form_textview.setText(text)

        }

        cancel_button.setOnClickListener() { v ->
            var c = childFragmentManager.beginTransaction()
            //var d = AddEntry()
            //c.replace(R.id.fragment1, main)
            //c.remove(this)
            childFragmentManager.popBackStackImmediate()
            c.commit()

        }






        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddEntry.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                AddEntry().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}