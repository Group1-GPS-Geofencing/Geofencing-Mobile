package com.geofence.geofencing_mobile.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.geofence.geofencing_mobile.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var firstname: EditText
    private lateinit var surname: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var register: Button
    private lateinit var loginUser: TextView

    private lateinit var mRootRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    private lateinit var pd: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        firstname = findViewById(R.id.firstname)
        surname = findViewById(R.id.surname)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        register = findViewById(R.id.register)
        loginUser = findViewById(R.id.login_user)

        mRootRef = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        pd = ProgressDialog(this)

        loginUser.setOnClickListener {
            startActivity(Intent(this@SignUp, SignIn::class.java))
        }

        register.setOnClickListener {
            val txtFirstname = firstname.text.toString().trim()
            val txtSurname = surname.text.toString().trim()
            val txtEmail = email.text.toString().trim()
            val txtPassword = password.text.toString()

            when {
                TextUtils.isEmpty(txtFirstname) -> Toast.makeText(
                    this@SignUp,
                    "Enter preferred firstname!",
                    Toast.LENGTH_SHORT
                ).show()

                TextUtils.isEmpty(txtSurname) -> Toast.makeText(
                    this@SignUp,
                    "Please Enter your surname!",
                    Toast.LENGTH_SHORT
                ).show()

                TextUtils.isEmpty(txtEmail) -> Toast.makeText(
                    this@SignUp,
                    "Please enter your email address!",
                    Toast.LENGTH_SHORT
                ).show()

                TextUtils.isEmpty(txtPassword) -> Toast.makeText(
                    this@SignUp,
                    "Please enter your password!",
                    Toast.LENGTH_SHORT
                ).show()

                txtPassword.length < 7 -> Toast.makeText(
                    this@SignUp,
                    "Password too short!",
                    Toast.LENGTH_SHORT
                ).show()

                !txtPassword.matches(Regex(".*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) ->
                    Toast.makeText(
                        this@SignUp,
                        "Please include at least one special character!",
                        Toast.LENGTH_SHORT
                    ).show()

                else -> registerUser(txtFirstname, txtSurname, txtEmail, txtPassword)
            }
        }
    }

    private fun registerUser(firstname: String, surname: String, email: String, password: String) {
        pd.setMessage("Please Wait!")
        pd.show()

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener(this@SignUp, OnSuccessListener<AuthResult> { authResult ->
                Log.d("SignUp", "User created successfully")
                val map = HashMap<String, Any>()
                map["firstname"] = firstname
                map["email"] = email
                map["surname"] = surname
                map["id"] = mAuth.currentUser!!.uid
                map["bio"] = ""
                map["imageurl"] = "default"

                mRootRef.child("Users").child(mAuth.currentUser!!.uid).setValue(map)
                    .addOnCompleteListener { task ->
                        pd.dismiss()  // Ensure pd is dismissed
                        if (task.isSuccessful) {
                            Log.d("SignUp", "User data saved successfully")
                            Toast.makeText(
                                this@SignUp,
                                "welcome,keep track of your routes!!",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@SignUp, HomeActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.e("SignUp", "Error saving user data", task.exception)
                            Toast.makeText(
                                this@SignUp,
                                "Error: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        pd.dismiss()  // Ensure pd is dismissed
                        Log.e("SignUp", "Database Error", e)
                        Toast.makeText(
                            this@SignUp,
                            "Database Error: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            })
            .addOnFailureListener(this@SignUp, OnFailureListener { e ->
                pd.dismiss()  // Ensure pd is dismissed
                Log.e("SignUp", "Authentication Error", e)
                Toast.makeText(
                    this@SignUp,
                    "Authentication Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            })
    }
}
