package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
//late init is used to initialize later during creation


    private lateinit var edtEmail:EditText
    private lateinit var edtPassword:EditText
    private lateinit var btnLogin:Button
    private lateinit var btnSignUp: Button

    private lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        mAuth= FirebaseAuth.getInstance()

        edtEmail=findViewById(R.id.edtEmail)
        edtPassword=findViewById(R.id.edtPassword)
        btnLogin=findViewById(R.id.btnLogin)
        btnSignUp=findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener{
            val intent=Intent(this,SignUp::class.java)

            startActivity(intent)

        }
//getting data from text box
        btnLogin.setOnClickListener {
            val email= edtEmail.text.toString()
            val password = edtPassword.text.toString()

            login(email,password)

        }
    }



    private fun login(email: String,password: String){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@Login, MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(this@Login,"User Not Registered", Toast.LENGTH_SHORT).show()
                }
            }
    }
}