package android.example.kotlinproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class OrderConfirmActivity : AppCompatActivity() {

    lateinit var okButton : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirm)

        okButton = findViewById(R.id.ok)
        okButton.setOnClickListener {
            onBackPressed()
        }

    }

}