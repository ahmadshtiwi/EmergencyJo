package com.example.emergencyjo


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.emergencyjo.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var database: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private fun showComponents() {
        tv_name_signup_id.visibility = View.VISIBLE
        tv_birthday_signup_id.visibility = View.VISIBLE
        tv_gender_signup_id.visibility = View.VISIBLE
        tv_governorate_signup_id.visibility = View.VISIBLE
        et_password_signup_id.visibility = View.VISIBLE
        et_re_password_signup_id.visibility = View.VISIBLE
        btn_create_account_id.visibility = View.VISIBLE
        tv_mother_name_signup_id.visibility = View.VISIBLE
        lock_icon_id.visibility = View.VISIBLE
    }

    private fun hideComponents() {
        tv_name_signup_id.visibility = View.INVISIBLE
        tv_birthday_signup_id.visibility = View.INVISIBLE
        tv_gender_signup_id.visibility = View.INVISIBLE
        tv_governorate_signup_id.visibility = View.INVISIBLE
        et_password_signup_id.visibility = View.INVISIBLE
        et_re_password_signup_id.visibility = View.INVISIBLE
        btn_create_account_id.visibility = View.INVISIBLE
        tv_mother_name_signup_id.visibility = View.INVISIBLE
        lock_icon_id.visibility = View.INVISIBLE
    }

    //Main class
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideComponents()
        //Call Data From FireBase
        binding.btnGetDataId.setOnClickListener {

            //Retrieve data from FireBase
            val userID: String = binding.etPersonalIdSignupId.text.toString()
            if (userID.isNotEmpty()) {
                readData(userID)
                showComponents()
            } else {
                Toast.makeText(this, "NOT EXIST", Toast.LENGTH_SHORT).show()
            }
        }

        //Account Create
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnCreateAccountId.setOnClickListener {

            val ID  = binding.etPersonalIdSignupId.text.toString() + "@gmail.com"
            val password = binding.etPasswordSignupId.text.toString()
            val Repassword = binding.etRePasswordSignupId.text.toString()

            if (ID.isNotEmpty() && password.isNotEmpty() && Repassword.isNotEmpty()) {
                if (password == Repassword) {

                    firebaseAuth.createUserWithEmailAndPassword(ID, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }

    }
        //Read Data From FireBase
       private fun readData(userID: String) {
            database = FirebaseDatabase.getInstance().getReference("Civil Affairs")
            database.child(userID).get().addOnSuccessListener {
                if (it.exists()) {
                    val name = it.child("name").value
                    val mothername = it.child("mother_name").value
                    val gender = it.child("gender").value
                    val governorate = it.child("governorate").value
                    val birthday = it.child("birthday").value
                    Toast.makeText(this, "Successfly Read", Toast.LENGTH_SHORT).show()
                    binding.tvNameSignupId.text = name.toString()
                    binding.tvMotherNameSignupId.text = mothername.toString()
                    binding.tvGenderSignupId.text = gender.toString()
                    binding.tvGovernorateSignupId.text = governorate.toString()
                    binding.tvBirthdaySignupId.text = birthday.toString()

                } else {
                    Toast.makeText(this, "Data NOT EXIST", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

