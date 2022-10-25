package android.example.kotlinproject

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentActivity

class AboutFragment() : Fragment() {

    lateinit var myButton : Button
    private lateinit var editText : EditText
    lateinit var sharedPreferences : SharedPreferences
    private val SHARED_PREF = "justFile"
    private val KEY_NOTE = "note"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_about, container, false)
        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE)

        editText = view.findViewById(R.id.story_edit)
        val note : String? = sharedPreferences.getString(KEY_NOTE,null)

          myButton = view.findViewById(R.id.saveBtn)

        myButton.setOnClickListener(View.OnClickListener {

            val editor : SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString(KEY_NOTE,note)
            editor.commit()
        })

        return view
    }
}
