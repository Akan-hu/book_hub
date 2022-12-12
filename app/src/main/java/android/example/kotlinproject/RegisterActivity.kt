package android.example.kotlinproject

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
import org.w3c.dom.Text
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var text1 : EditText
    lateinit var text2 : EditText
    lateinit var text3 : EditText
    lateinit var text4 : EditText
    lateinit var text5 : EditText
    lateinit var register : Button
    lateinit var login : TextView

    lateinit var strName : String
    lateinit var strEmail : String
    lateinit var strPhone : String
    lateinit var strPass : String
    lateinit var strConfirm : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        text1 = findViewById(R.id.reg_name)
        text2 = findViewById(R.id.reg_email)
        text3 = findViewById(R.id.reg_phone)
        text4 = findViewById(R.id.reg_password)
        text5 = findViewById(R.id.reg_confirm_pass)
        register = findViewById(R.id.register_button)
        login = findViewById(R.id.login_text)

        login.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        register.setOnClickListener{
                validation()

        }
    }
    private fun validation()  {
        strName = text1.text.toString()
        strEmail = text2.text.toString()
        strPhone = text3.text.toString()
        strPass = text4.text.toString()
        strConfirm = text5.text.toString()

        if (strName.isEmpty()) {
            text1.error = "Enter name"
            text1.requestFocus()
            return
        }
        if (strEmail.isEmpty()) {
            text2.error = "Enter email"
            text2.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            text2.error = "Enter valid email "
            text2.requestFocus()
            return
        }
        if (strPhone.isEmpty()) {
            text3.error = "Enter phone number"
            text3.requestFocus()
            return
        }
        if (!numberCheck(strPhone)) {
            text3.error = "Invalid Mobile number"
            text3.requestFocus()
            return
        }
        if (strPass.isEmpty()) {
            text4.error = "Enter your password"
            text4.requestFocus()
            return
        }
        else if (!strValidation(strPass)) {
            text4.error = "Enter minimum 6 digits"
            text4.requestFocus()
            return
        }
        if (strConfirm.isEmpty()) {
            text5.error = "Enter confirm password"
            text5.requestFocus()
            return
        }
        if (strConfirm != strPass) {
            text5.error = "Password and Confirm Password should be same"
            text5.requestFocus()
            return
        }
        uploadingDataToDb()
     }

   private fun uploadingDataToDb() {
        val regTime = "" + System.currentTimeMillis()
        val data = HashMap<String, Any>()
        data["Time"] = regTime
        data["Name"] = strName
        data["Email"] = strEmail
        data["Phone"] = strPhone
        data["Pass"] = strPass

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("User")
        databaseReference.child(strName).setValue(data).addOnSuccessListener {
            //progressDialog.dismiss()
            Toast.makeText(applicationContext,"Registered Successful",Toast.LENGTH_SHORT).show()
            val intent = Intent(this@RegisterActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
            .addOnFailureListener{
               // progressDialog.dismiss()
                Toast.makeText(applicationContext,"Error",Toast.LENGTH_SHORT).show()
            }
    }

    private fun numberCheck(strPhone : String) : Boolean{
            val p = Pattern.compile("[0-9]{10}")
            val m = p.matcher(strPhone)
            return m.matches()
    }

    private fun strValidation(strPass : String) : Boolean{
            val p = Pattern.compile(".{6}")
            val m = p.matcher(strPass)
            return m.matches()
    }
}
