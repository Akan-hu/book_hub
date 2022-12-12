package android.example.kotlinproject

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.example.kotlinproject.CartRecylerAdapter.CartViewHolder
import android.example.kotlinproject.database.BookEntities
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import android.example.kotlinproject.MyCartFragment as MyCart1

class CartRecylerAdapter(val context : Context,val bookList : List<BookEntities>,val placedOrder : Button) : RecyclerView.Adapter<CartViewHolder>() {
     var listData: MutableList<BookEntities> = bookList as MutableList<BookEntities>

    override fun onCreateViewHolder(parent : ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_cart_file,parent,false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        val bindBook = listData[position]

        holder.cartBookName.text = bindBook.bookName
        holder.cartBookAuthor.text = bindBook.bookAuthor
        holder.cartRating.text = bindBook.bookRating
        Picasso.get().load(bindBook.bookImage).error(R.drawable.default_book_cover)
            .into(holder.cartBookImage)

        holder.deleteBook.setOnClickListener {
                DescriptionActivity.DBAsyncTask(this.context,bindBook, mode = 3).execute().get()

            listData.removeAt(position)

            notifyItemRemoved(position)
            updatePrice()

        }
        holder.cartRelLayout.setOnClickListener {
            val intent = Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",bindBook.book_id.toString())
            context.startActivity(intent)
        }
    }

    class CartViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val cartBookImage : ImageView = view.findViewById(R.id.cart_BookImage)
        val cartBookName : TextView = view.findViewById(R.id.cart_BookName)
        val cartBookAuthor : TextView = view.findViewById(R.id.cart_Book_author)
        val cartRating : TextView = view.findViewById(R.id.cart_Book_rating)
        val cartRelLayout : RelativeLayout = view.findViewById(R.id.cart_relative)
        var deleteBook : ImageView = view.findViewById(R.id.delete_book)
    }
    fun updatePrice() {
        var mySum = 0
        for (i in 0 until listData.size) {
            mySum += listData[i].bookRating.replace("Rs. ", "").toInt()
        }
        var total = "Place Order(Total Rs. ${mySum})"
        placedOrder.text = total
    }
}