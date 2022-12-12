package android.example.kotlinproject

import android.content.Context
import android.content.Intent
import android.example.kotlinproject.database.BookEntities
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context:Context, val bookList:List<BookEntities>) : RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favourite_single_row,parent,false)
        return FavouriteViewHolder(view)

    }

    override fun getItemCount(): Int {

        return bookList.size

    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {

            val book = bookList[position]

            holder.favBookName.text = book.bookName
            holder.favBookAuthor.text = book.bookAuthor
            holder.favBookPrice.text = book.bookPrice
            holder.favBookRating.text = book.bookRating

            Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.favBookImage)
        holder.favLinearLayout.setOnClickListener {
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("book_id", book.book_id.toString())
            context.startActivity(intent)

        }

    }


    class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val favBookImage : ImageView = view.findViewById(R.id.fav_image)
        val favBookName : TextView = view.findViewById(R.id.fav_my)
        val favBookAuthor : TextView = view.findViewById(R.id.fav_android)
        val favBookPrice : TextView = view.findViewById(R.id.fav_price)
        val favBookRating : TextView = view.findViewById(R.id.fav_rating)
        val favLinearLayout : LinearLayout = view.findViewById(R.id.llFavContent)

    }

}