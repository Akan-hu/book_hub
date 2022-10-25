package android.example.kotlinproject

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.example.kotlinproject.model.Book
import android.example.kotlinproject.util.ConnectionManager
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

class DashboardFragment() : Fragment() {
    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    //lateinit var btnNetwork: Button
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    val bookInfoList = arrayListOf<Book>()

    lateinit var recyclerAdapter : AdapterClass

//comparator class of kotlin allows to compare different objects so we use this class to compare ratings and to arrange them

    var ratingComparator = Comparator<Book>{
        book1,book2 ->
//code to compare books on the basis of rating
            book1.bookRating.compareTo(book2.bookRating,true)

//if rating of books is same we want arange them alphabetically this code be optional
          if(book1.bookRating.compareTo(book2.bookRating,true) == 0){
              book1.bookName.compareTo(book2.bookName,true)
          }

        else {
              book1.bookRating.compareTo(book2.bookRating,true)
          }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        //telling the compiler that we add a menu on toolbar
        setHasOptionsMenu(true)

        recyclerDashboard = view.findViewById(R.id.recycle)

       // btnNetwork = view.findViewById(R.id.network)

        progressLayout = view.findViewById(R.id.my_relative)
        progressBar = view.findViewById(R.id.progress_bar)

        //While loading contents status bar will visible

        progressLayout.visibility = View.VISIBLE

     /*   btnNetwork.setOnClickListener {
            if (ConnectionManager().checkConnectivity(activity as Context)) {
                //internet is connected
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Success")
                dialog.setMessage("Internet Connection Found")
                dialog.setPositiveButton("Ok") { text, listener ->
                    //do nothing
                }
                dialog.setNegativeButton("Cancel") { text, listener ->

                }
                dialog.create()
                dialog.show()

            } else {
                //internet is not available

                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Fail")
                dialog.setMessage("Internet Connection is not Found")
                dialog.setPositiveButton("Ok") { text, listener ->
                    //do nothing
                }
                dialog.setNegativeButton("Cancel") { text, listener ->
                }
                dialog.create()
                dialog.show()

            }
        }*/

        layoutManager = LinearLayoutManager(activity)
        //this variable is used to store request queue
        val queue = Volley.newRequestQueue(activity as Context)

        //below url gives response to our request url always a string
        val url = "http://13.235.250.119/v1/book/fetch_books/"

        //this code is used to not send request to json if network is not available
        //checking network before sending request to json

       if(ConnectionManager().checkConnectivity(activity as Context)) {
           //json request is null because it is get request
           val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

               //Here we handle response
               //checking response of API here 'it' is variable in which response is stored in the form of string
               try {

                   //it will hide progress bar
                   progressLayout.visibility = View.GONE

                   val success = it.getBoolean("success")

                   //here we extracting value of success key if true we extract data from json array if not give an error message to user
                   if (success) {

                       val data = it.getJSONArray("data")

                       //retrieving every json object from json array using loop
                       for (i in 0 until data.length()) {

                           val bookJsonobject = data.getJSONObject(i)
                           val bookObject = Book(
                               bookJsonobject.getString("book_id"),
                               bookJsonobject.getString("name"),
                               bookJsonobject.getString("author"),
                               bookJsonobject.getString("price"),
                               bookJsonobject.getString("rating"),
                               bookJsonobject.getString("image")

                           )
                           bookInfoList.add(bookObject)
                           recyclerAdapter = AdapterClass(activity as Context, bookInfoList)
                           recyclerDashboard.adapter = recyclerAdapter
                           recyclerDashboard.layoutManager = layoutManager

                           //below line is used to add divider line among list elements
                         /*  recyclerDashboard.addItemDecoration(
                               DividerItemDecoration(
                                   recyclerDashboard.context,
                                   (layoutManager as LinearLayoutManager).orientation
                               )
                           )*/
                       }
                   } else {
                       Toast.makeText(
                           activity as Context, "Some Error occured!!!", Toast.LENGTH_SHORT
                       ).show()
                   }
               } catch (e : JSONException){
                   Toast.makeText(activity as Context,"Some unexpected error is occurred",Toast.LENGTH_SHORT).show()
               }
           }, Response.ErrorListener {
                       Toast.makeText(activity as Context,"Some error occurred",Toast.LENGTH_SHORT).show()

                   }) {
                       override fun getHeaders(): MutableMap<String, String> {
                           val headers = HashMap<String, String>()
                           headers["Content-type"] = "application/json"
                           headers["token"] = "e56bc42da29daf"
                           return headers
                       }
                   }
           queue.add(jsonObjectRequest)
       }

        //if network is not available we give 2 options to user exit app or go to mobile settings and add network

        else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Oops!")
            dialog.setMessage("Internet is not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->

         //code for when we click on open settings our phone's setting open and we can add network
         //ACTION_WIRELESS_SETTINGS it consist commands to open android system's setting

                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)

         //we finish activity so when user back to open internet fragment loads content and page or we can say list refrashes
               //
            activity?.finish()

            }
            dialog.setNegativeButton("Exit") { text, listener ->
         /*this code exit the app in ActivityCompat class there is a method
         finishAffinity which close all running processes and close app accept current activity as parameter*/
                ActivityCompat.finishAffinity(activity as Activity)

            }
            dialog.create()
            dialog.show()
        }
        return view
    }

    //by using this method we can add new menu items in toolbar
    //here the menu variable hold the file which we created for this menu

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.sorting_menu,menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

//this variable is used to store the id of clicked item
        val id = item?.itemId
        if(id == R.id.action_sort) {

//this code for sorting books for this we use inbuilt function
//this sort books in increasing order
            Collections.sort(bookInfoList, ratingComparator)

//now books sorted in decreasing order
            bookInfoList.reverse()

        }
//notifying adapter about changes
        recyclerAdapter.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }
}

