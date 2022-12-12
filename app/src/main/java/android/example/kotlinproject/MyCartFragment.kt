package android.example.kotlinproject

import android.content.Context
import android.content.Intent
import android.example.kotlinproject.database.BookDatabase
import android.example.kotlinproject.database.BookEntities
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.loader.content.AsyncTaskLoader
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import android.widget.Toast.makeText as makeText1

class MyCartFragment : Fragment() {

    lateinit var cartRecyclerView : RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: CartRecylerAdapter
    lateinit var placedOrder : Button

    var dataBaseBooks = listOf<BookEntities>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view =  inflater.inflate(R.layout.fragment_my_cart, container, false)
        cartRecyclerView = view.findViewById(R.id.cart_recycler)
        layoutManager = LinearLayoutManager(activity as Context)
        placedOrder = view.findViewById(R.id.total_amount)

        placedOrder.setOnClickListener {
            val intent = Intent(activity as Context,OrderConfirmActivity::class.java)
            startActivity(intent)
        }

        dataBaseBooks = RetrieveCartBook(activity as Context).execute().get()
        Log.d("myList",dataBaseBooks.toString())
        if(activity != null){

            recyclerAdapter = CartRecylerAdapter(activity as Context,dataBaseBooks,placedOrder)
            cartRecyclerView.layoutManager = layoutManager
            cartRecyclerView.adapter = recyclerAdapter

                var mySum = 0
                for (i in 0 until dataBaseBooks.size) {
                    mySum += dataBaseBooks[i].bookRating.replace("Rs. ", "").toInt()
                }
                var total = "Place Order(Total Rs. ${mySum})"
                placedOrder.text = total
        }
        return view

    }

    class RetrieveCartBook(val context : Context) : AsyncTask<Void,Void,List<BookEntities>>(){
        override fun doInBackground(vararg params: Void?): List<BookEntities> {
            val dataBase = Room.databaseBuilder(context,BookDatabase ::class.java,"books_db").build()
            return dataBase.bookDao().getAllBooks()

        }

    }
}