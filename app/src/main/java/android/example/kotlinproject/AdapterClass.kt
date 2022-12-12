package android.example.kotlinproject

import android.content.Context
import android.content.Intent
import android.example.kotlinproject.model.Book
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import java.util.ArrayList

class AdapterClass(val context:Context, private val bookInfoList:ArrayList<Book>) : RecyclerView.Adapter<AdapterClass.DashboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_file, parent, false)
        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
       val book = bookInfoList[position]


        holder.txtBookName.text = book.bookName
        holder.txtBookauthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.textBookRating.text = book.bookRating

        /*loading image in the form of string using picasso library
        here in below line error is used to handle errors occurs by picasso
        error like if all images are not loaded correctly we can show default images*/

        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgBookImage)
        holder.relativeLayout.setOnClickListener {

          val intent = Intent(context, DescriptionActivity :: class.java)

            //adding book id in the intent
            intent.putExtra("book_id",book.bookId)

            //this method is used when we want to open any activity from current activity
            context.startActivity(intent)
        }


    }
    override fun getItemCount(): Int {
        return bookInfoList.size

    }
    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtBookName:TextView = view.findViewById(R.id.txtBookName)
        val imgBookImage:ImageView = view.findViewById(R.id.imgBookImage)
        val txtBookauthor:TextView = view.findViewById(R.id.txtBookauthor)
        val txtBookPrice:TextView = view.findViewById(R.id.txtBookPrice)
        val textBookRating:TextView = view.findViewById(R.id.txtBookrating)
        val relativeLayout:RelativeLayout = view.findViewById(R.id.relative)
    }

}
