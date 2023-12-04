package com.example.chitchat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.chitchat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase



class SignUpActivity : AppCompatActivity() {

    //Firebase Authentication
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var etUserName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPasswordConfirm: EditText
    private lateinit var loginButton: Button
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        etUserName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etPasswordConfirm = findViewById(R.id.etConfirmPassword)
        loginButton = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val userName: String = etUserName.text.toString()
            val email: String = etEmail.text.toString()
            val password: String = etPassword.text.toString()
            val confirmPassword: String = etPasswordConfirm.text.toString()

            if (TextUtils.isEmpty(userName)) {
                Toast.makeText(applicationContext, "Username is required", Toast.LENGTH_SHORT).show()
            }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Email is required", Toast.LENGTH_SHORT).show()

            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Password is required", Toast.LENGTH_SHORT)
                    .show()
            }

            if (TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(
                    applicationContext,
                    "Confirm Password is required",
                    Toast.LENGTH_SHORT
                ).show()
            }

            if (password != confirmPassword) {
                Toast.makeText(
                    applicationContext,
                    "Confirm Password didn't match",
                    Toast.LENGTH_SHORT
                ).show()
            }

            registerUser(userName, email, password)

        }

        loginButton.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }




    }

    private fun registerUser(userName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            //If Success -> add user to database
            if (it.isSuccessful) {

                val user: FirebaseUser? = auth.currentUser
                val userId: String = user!!.uid

                databaseReference =
                    FirebaseDatabase.getInstance().getReference("User").child(userId)

                val hashMap: HashMap<String, String> = HashMap()
                hashMap.put("userId", userId)
                hashMap.put("username", userName)
                hashMap.put("profileImage", "")

                databaseReference.setValue(hashMap).addOnCompleteListener(this) {
                    if (it.isSuccessful) {

                        etUserName.setText("")
                        etEmail.setText("")
                        etPassword.setText("")
                        etPasswordConfirm.setText("")

                        //open home activity
                        val intent = Intent(this@SignUpActivity, UsersActivity::class.java)
                        startActivity(intent)

                    }
                }
            }
        }
    }
}


