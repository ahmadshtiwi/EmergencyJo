package com.example.emergencyjo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() , TextWatcher {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_id.setOnClickListener()  // log in button { use intent to go Main activity }
        {
            var goToMain = Intent(this, Main::class.java)
            startActivity(goToMain)
        }//end login_button

        signup_button_id.setOnClickListener() // Signup in button { use intent to go Sign Up activity }
        {
            var goToSignUp = Intent(this, SignUp::class.java)
            startActivity(goToSignUp)
        }//end signup button

        et_password_login_id.addTextChangedListener(this)
        et_personal_id_login_id.addTextChangedListener(this)


    }//end onCreate method

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
    {
    }//end beforeTextChanged

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
    {
        login_button_id.isEnabled = et_personal_id_login_id.text.trim().isNotEmpty()&&et_password_login_id.text.trim().isNotEmpty()

    }//end onTextChanged

    override fun afterTextChanged(s: Editable?)
    {

    }//end afterTextChanged
}