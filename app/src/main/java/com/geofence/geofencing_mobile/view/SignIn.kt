package com.geofence.geofencing_mobile.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.app.ProgressDialog
import android.content.Intent
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.geofence.geofencing_mobile.R
import com.geofence.geofencing_mobile.R.id.sign_up_user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

/**
 * Author Sibongile Nyirenda
 * Sign in Authentication with Firebase
 * date: 18/june/2024
 */
class SignIn : AppCompatActivity() {
    /**
     * Declare UI elements and Firebase authentication variables
     */
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var signin: Button
    private lateinit var signUpUser: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var pd: ProgressDialog

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * Set the theme for the activity before it is created
         * Initialize UI elements and Firebase authentication and progress dialog and set click listener
         *
         */
        setTheme(R.style.AppThemeNoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        signin = findViewById(R.id.login)
        signUpUser = findViewById(R.id.sign_up_user)

       var  mRootRef = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        pd = ProgressDialog(this)

        signUpUser.setOnClickListener {
            val intent = Intent(this@SignIn, SignUp::class.java)
            startActivity(intent)
            finish()
        }


        signin.setOnClickListener {
            val txtEmail = email.text.toString()
            val txtPassword = password.text.toString()

            when {
                TextUtils.isEmpty(txtEmail) && TextUtils.isEmpty(txtPassword) -> {
                    Toast.makeText(this@SignIn, "Please enter your email and password", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(txtEmail) -> {
                    Toast.makeText(this@SignIn, "Please enter your email", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(txtPassword) -> {
                    Toast.makeText(this@SignIn, "Please enter your password", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    signinUser(txtEmail, txtPassword)
                }
            }
        }
    }

    /**
     * Function to sign in the user with email and password
     */
    private fun signinUser(email: String, password: String) {
        pd.setMessage("Please Wait!")
        pd.show()

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                pd.dismiss()
                Toast.makeText(this@SignIn, "Welcome User", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SignIn, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { e ->
            pd.dismiss()
            Toast.makeText(this@SignIn, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}




