package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var mDatabaseReference:DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()
        mAuth= FirebaseAuth.getInstance()
        edtName=findViewById(R.id.edt_Name)
        edtEmail=findViewById(R.id.edt_Email)
        edtPassword=findViewById(R.id.edt_Password)
        btnSignUp=findViewById(R.id.btn_SignUp)

        btnSignUp.setOnClickListener {
            val email = edtEmail.text.toString()
            val password= edtPassword.text.toString()
            val name=edtName.text.toString()
            signUp(name,email,password)
        }
    }

    private fun signUp(name: String,email: String,password: String){

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //make the line below null safe
                    addUserToDatabase(name,email,mAuth.uid!!)
                   // code here takes us to main page
                    val intent = Intent(this@SignUp, MainActivity::class.java)
                    finish()
                    startActivity(intent)




                } else {
                    Toast.makeText(this@SignUp,"Error occured",Toast.LENGTH_SHORT).show()

                }
            }
    }


    private fun addUserToDatabase(name:String,email:String,uid:String){
        mDatabaseReference=FirebaseDatabase.getInstance().getReference()

        //by making this child each user will have a unique uid
        mDatabaseReference.child("user").child(uid).setValue(User(name,email,uid))

    }

}