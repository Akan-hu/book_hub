package android.example.kotlinproject.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//this class is used to store data in tabular form so this class is data class

/*by using annotation we tells compiler that what we are creating
 here annotation tells to compiler that it is entity class*/

@Entity( tableName = "Books") //it is called annotation

data class BookEntities(

    @PrimaryKey val book_id : Int,
    @ColumnInfo(name = "book_name") val bookName : String,
    @ColumnInfo(name = "book_author") val bookAuthor : String,
    @ColumnInfo(name = "book_price") val bookPrice : String,
    @ColumnInfo(name = "book_rating") val bookRating : String,
    @ColumnInfo(name = "book_desc") val bookDesc : String,
    @ColumnInfo(name = "book_image") val bookImage : String

)
