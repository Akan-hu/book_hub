package android.example.kotlinproject.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao  //Dao is a interface not class in interfaces implementations of functions is not required

interface BookDao {
    @Insert
    fun insertBook(bookEntity : BookEntities)

    @Delete
    fun deleteBook(bookEntity: BookEntities)

    @Query("SELECT*FROM Books")
    fun getAllBooks() : List<BookEntities>

    //function to check that a particular book is added in favourites or not
    @Query("SELECT*FROM Books where book_id = :bookId")
    fun getBookById(bookId : String) : BookEntities


}
