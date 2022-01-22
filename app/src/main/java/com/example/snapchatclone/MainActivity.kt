package com.example.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    var emailEditText:EditText?=null
    var passwordEditText:EditText?=null
    val mAuth=FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText=findViewById(R.id.emailEditText)
        passwordEditText=findViewById(R.id.passwordEditText)

        if(mAuth.currentUser!=null)
        {
            login()
        }
    }

    fun loginClicked(view:View)
    {
        mAuth.signInWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login Successfull
                    task.result.user?.let {
                        FirebaseDatabase.getInstance().reference.child("users").child(it.uid).child("email").setValue(emailEditText?.text.toString())
                    }

                    login()
                } else {
                    //Login Failed.. Sign up user
                    mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
                        .addOnCompleteListener(this) { task ->
                        if(task.isSuccessful)
                        {
                            task.result.user?.let {
                                FirebaseDatabase.getInstance().reference.child("users").child(it.uid).child("email").setValue(emailEditText?.text.toString())
                            }
                            login()
                        }
                        else
                        {
                            Toast.makeText(this,"Login Failed",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }

    fun login()
    {
        val intent= Intent(this,SnapsActivity::class.java)
        startActivity(intent)
    }
}