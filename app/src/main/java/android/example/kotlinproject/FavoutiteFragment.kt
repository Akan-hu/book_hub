package android.example.kotlinproject

import android.content.Context
import android.example.kotlinproject.database.BookDatabase
import android.example.kotlinproject.database.BookEntities
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

class FavoutiteFragment : Fragment() {

    lateinit var recyclerFavourite : RecyclerView
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar : ProgressBar
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavouriteRecyclerAdapter


    //this variable is used to store book list
    //data which we get from database store in this variable
    var dbBookList = listOf<BookEntities>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favoutite, container, false)

        recyclerFavourite = view.findViewById(R.id.recyclerFavourite)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        /*gridlayoutmanager show layout by rows it display multiple item within single row
        spam count is number of items we want to display in single row */

        layoutManager = GridLayoutManager(activity as Context,2)

        //here we give activity as context because fragment cant't access application context directly

        dbBookList = RetrieveFavourites(activity as Context).execute().get()

        //After obtaining list from database we will check it should't empty also the activity which hosting this fragment not empty
        //if both conditions are true we initialize adapter and use recycler adapter
        Log.d("list",dbBookList.toString())
        if(activity != null){

            progressLayout.visibility = View.GONE

            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context,dbBookList)
            recyclerFavourite.layoutManager = layoutManager
            recyclerFavourite.adapter = recyclerAdapter

        }

        return view
    }

    //retrieving books from database
    class RetrieveFavourites(val context : Context) : AsyncTask<Void,Void,List<BookEntities>>(){

        override fun doInBackground(vararg params: Void?): List<BookEntities> {

            //initialization of database
            val db = Room.databaseBuilder(context,BookDatabase :: class.java,"books_db").build()

            return db.bookDao().getAllBooks()

        }

    }
}