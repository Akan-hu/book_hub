package android.example.kotlinproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import java.util.*

class LoginActivity : AppCompatActivity() {

    lateinit var logPass : TextInputEditText
    lateinit var logName : TextInputEditText
    lateinit var logButton : Button
    lateinit var forgotPass : TextView
    lateinit var registerUser : TextView
    lateinit var googleImage : ImageView
    lateinit var strLogName : String
    lateinit var strLogPassword : String
    lateinit var client : GoogleSignInClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        logName = findViewById(R.id.login_name)
        logPass = findViewById(R.id.login_password)
        logButton = findViewById(R.id.login_button)
        forgotPass = findViewById(R.id.forgot)
        registerUser = findViewById(R.id.new_user_reg)

        //google signing code

        googleImage = findViewById(R.id.google)

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()

        client = GoogleSignIn.getClient(this,options)
        googleImage.setOnClickListener {
            val intent = client.signInIntent
            startActivityForResult(intent,10001)

        }

        registerUser.setOnClickListener {
            val intent = Intent(applicationContext,RegisterActivity::class.java)
            startActivity(intent)
        }
        logButton.setOnClickListener {
            myValidation()
        }

    }

    private fun myValidation() {
         strLogName = logName.text.toString()
         strLogPassword = logPass.text.toString()

        if(strLogName.isEmpty()){
            logName.error = "Enter name"
        }
        if(strLogPassword.isEmpty()){
            logPass.error = "Enter Password"
        }
        checkFromDb()

    }

    private fun checkFromDb() {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val referenceDatabase = firebaseDatabase.getReference("User")
        referenceDatabase.child(strLogName)
        referenceDatabase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val checkName = snapshot.child(strLogName).child("Name").getValue(String::class.java)
                    val myPass = snapshot.child("Pass").getValue(String::class.java)
                    if(myPass == strLogPassword){
                        Toast.makeText(applicationContext,"Successfully login",Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(applicationContext,"Incorrect password or Name",Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(applicationContext,"User not found",Toast.LENGTH_SHORT).show()

                }

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==10001){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken,null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener {task ->
                    if(task.isSuccessful){

                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                    else{
                        Toast.makeText(this,task.exception?.message,Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }

}