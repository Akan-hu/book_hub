package android.example.kotlinproject

import android.content.Context
import android.content.Intent
import android.example.kotlinproject.database.BookDatabase
import android.example.kotlinproject.database.BookEntities
import android.example.kotlinproject.util.ConnectionManager
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {

    lateinit var image : ImageView
    lateinit var bookName : TextView
    lateinit var authorName : TextView
    lateinit var bookPrice : TextView
    lateinit var bookRating : TextView
    lateinit var bookDescr: TextView
    lateinit var favourButton : Button
    lateinit var cartButton : Button

    lateinit var progressBar: ProgressBar
    lateinit var progressLayout : RelativeLayout
    lateinit var toolbar : Toolbar

    //lateinit modifier is not allow on properties of nullable types

     var bookId : String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        image = findViewById(R.id.image)
        bookName = findViewById(R.id.anna)
        authorName = findViewById(R.id.writer)
        bookPrice = findViewById(R.id.rupees)
        bookRating = findViewById(R.id.rat)
        bookDescr = findViewById(R.id.descript)
        favourButton = findViewById(R.id.add)
        cartButton = findViewById(R.id.add_to_cart)

        progressBar = findViewById(R.id.bar)
        progressBar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.favour_layout)
        progressLayout.visibility = View.VISIBLE
        toolbar = findViewById(R.id.tool_bar)
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"

        if (intent != null) {
            bookId = intent.getStringExtra("book_id")

        } else {
            finish()
            Toast.makeText(this@DescriptionActivity, "Some error occurred", Toast.LENGTH_SHORT)
                .show()
        }
        if (bookId == "100") {
            Toast.makeText(this@DescriptionActivity, "Error is occurred", Toast.LENGTH_SHORT).show()
        }
        val request = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"
        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)

        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
            val jsonRequest = object :
                JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                    try {
                        val success = it.getBoolean("success")

                        if (success) {

                            val bookJsonObject = it.getJSONObject("book_data")
                            progressLayout.visibility = View.GONE
                            val bookImageUrl = bookJsonObject.getString("image")
                            Picasso.get().load(bookJsonObject.getString("image"))
                                .error(R.drawable.default_book_cover).into(image)
                            bookName.text = bookJsonObject.getString("name")
                            authorName.text = bookJsonObject.getString("author")
                            bookRating.text = bookJsonObject.getString("rating")
                            bookPrice.text = bookJsonObject.getString("price")
                            bookDescr.text = bookJsonObject.getString("description")

                            var bookEntities = BookEntities(
                                bookId?.toInt() as Int,
                                bookName.text.toString(),
                                authorName.text.toString(),
                                bookRating.text.toString(),
                                bookPrice.text.toString(),
                                bookDescr.text.toString(),
                                bookImageUrl
                            )
                            val checkFav = DBAsyncTask(
                                applicationContext,
                                bookEntities,
                                mode = 1
                            ).execute()

                            //this variable checks the value of checkfav if book in favourites or not
                            val isFav = checkFav.get()

                            //if button in favourites change the colour of button

                            if (isFav) {
                                favourButton.text = "Remove from favourites"
                                val favrouriteColour = ContextCompat.getColor(
                                    applicationContext,
                                    R.color.colourFavourites
                                )

                                favourButton.setBackgroundColor(favrouriteColour)
                            } else {
                                favourButton.text = "Add to Favourites"
                                val noFavCol = ContextCompat.getColor(
                                    applicationContext,
                                    R.color.notFavouriteColour
                                )
                                favourButton.setBackgroundColor(noFavCol)
                            }
                            //adding click listener on button

                            //this code for when book is not added in favourites so to add book in favourite we use this code
                            //we call mode 2 method for this

                            favourButton.setOnClickListener {
                                if (!DBAsyncTask(
                                        applicationContext,
                                        bookEntities,
                                        mode = 1
                                    ).execute().get()
                                ) {
                                    val addFav = DBAsyncTask(
                                        applicationContext,
                                        bookEntities,
                                        mode = 2
                                    ).execute()
                                    val favResult = addFav.get()
                                    if (favResult) {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Successfully add in Favourites",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        favourButton.text = "Remove from favourites"
                                        val favrouriteColour = ContextCompat.getColor(
                                            applicationContext,
                                            R.color.colourFavourites
                                        )
                                        favourButton.setBackgroundColor(favrouriteColour)
                                    } else {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Unable to add in favourites",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }

                                //this code for when book is added in favourites so to add remove book from favourite we use this code
                                //we call mode 3 method for this

                                else {
                                    val removeFav = DBAsyncTask(
                                        applicationContext,
                                        bookEntities,
                                        mode = 3
                                    ).execute()
                                    val removeResult = removeFav.get()
                                    if (removeResult) {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Successfully remove from Favourite",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        favourButton.text = "Add to Favourites"
                                        val noFavCol = ContextCompat.getColor(
                                            applicationContext,
                                            R.color.notFavouriteColour
                                        )
                                        favourButton.setBackgroundColor(noFavCol)
                                    } else {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Unable to remove from Favourites" +
                                                    "",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                            val checkCart =
                                    DBAsyncTask(applicationContext, bookEntities, 1).execute()
                            val isInCart = checkCart.get()
                            if (isInCart) {
                                cartButton.text = "Added in Cart"
                                val textCol = ContextCompat.getColor(applicationContext,R.color.textColor)
                                val backCol = ContextCompat.getColor(applicationContext,R.color.cartColour)
                                cartButton.setTextColor(textCol)
                                cartButton.setBackgroundColor(backCol)
                            } else {
                                cartButton.text = "Add to Cart"
                            }

                            //adding clickListener to cart button

                            cartButton.setOnClickListener {
                                if(!DBAsyncTask(applicationContext,bookEntities,1).execute().get()) {
                                    val cartModeTwo = DBAsyncTask(
                                        applicationContext,
                                        bookEntities,
                                        2
                                    ).execute()

                                    val modeTwoResult = cartModeTwo.get()
                                    if(modeTwoResult){
                                        Toast.makeText(this@DescriptionActivity,"Book added in Cart",Toast.LENGTH_SHORT).show()
                                        cartButton.text = "Added in Cart"
                                        val txtcol = ContextCompat.getColor(applicationContext,R.color.textColor)
                                        val col = ContextCompat.getColor(applicationContext,R.color.cartColour)
                                        cartButton.setBackgroundColor(col)
                                        cartButton.setTextColor(txtcol)
                                    }
                                    else{
                                        Toast.makeText(this@DescriptionActivity,"Some Error occured",Toast.LENGTH_SHORT).show()
                                    }
                                }
                               /* else{
                                   val cartModeThree = DBAsyncTask(applicationContext,bookEntities,3).execute()
                                    val modeThreeResult = cartModeThree.get()
                                    if(modeThreeResult) {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Book remove from cart",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        cartButton.text = "Add to cart"
                                    }
                                    else{
                                            Toast.makeText(this@DescriptionActivity,"Some error ocurred",Toast.LENGTH_SHORT).show()

                                        }
                                }*/

                            }
                        } else {
                            Toast.makeText(
                                this@DescriptionActivity,
                                "error occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } catch (e: Exception) {

                        Toast.makeText(
                            this@DescriptionActivity,
                            "error occurred",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }, Response.ErrorListener {

                    Toast.makeText(this@DescriptionActivity, "volley error $it", Toast.LENGTH_SHORT)
                        .show()


                })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "e56bc42da29daf"
                    return headers
                }

            }
            request.add(jsonRequest)
        }
    }

    /* below class is used to perform three operations
    1. check if book is added to favourite or not
    2. to add book in database as favourites
    3. to remove book from favourites
     */
    //this class is used to access database
    class DBAsyncTask(val context : Context,val bookEntities: BookEntities,val mode:Int) : AsyncTask<Void, Void, Boolean>() {

        /*  mode 1 -> check the DB if book in favourite or not
            mode 2 -> save the book in DB as favourites
            mode 3 -> remove the favourite book from DB
         */

        // initialization of BookDatabase class

        val db = Room.databaseBuilder(context, BookDatabase::class.java, "books_db").build()
        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {

                1 -> {

                    // check the DB if book in favourite or not

                    val book: BookEntities? =
                            db.bookDao().getBookById(bookEntities.book_id.toString())
                    db.close()

                    //if book in the favourites it return true else false
                    return book != null

                }

                2 -> {

                    // save the book in DB as favourites
                    db.bookDao().insertBook(bookEntities)
                    db.close()
                    return true

                }

                3 -> {
                    // remove the favourite book from DB
                    db.bookDao().deleteBook(bookEntities)
                    db.close()
                    return true
                }

            }
            return false
        }
    }
 }




