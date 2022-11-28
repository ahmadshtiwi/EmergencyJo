package com.example.emergencyjo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.emergencyjo.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class Login:AppCompatActivity(),TextWatcher{

    lateinit var dataLogin:ArrayList<DataBaseEmergencyUser>
     var mRefEmergencyUser: DatabaseReference?=null
    var position:Int?=-1

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

         var test=getID()
         if(test.length==10)
         {
             var goToMain=Intent(this,Main::class.java)
             startActivity(goToMain)
             finish()

         }
            dataLogin= ArrayList()
        connectDataBase()

        mRefEmergencyUser?.addValueEventListener(object :ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children )
                {
                    dataLogin.add(snap.getValue(DataBaseEmergencyUser::class.java)!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



        et_password_login_id.addTextChangedListener(this)
        et_personal_id_login_id.addTextChangedListener(this)
    }//end onCreate method



    override fun onStart() {
        super.onStart()

        signup_button_id.setOnClickListener() // Signup in button { use intent to go Sign Up activity }
        {
            val goToSignUp = Intent(this, SignUp::class.java)
            startActivity(goToSignUp)
        }//end signup button

        login_button_id.setOnClickListener()
        {
            var personalID = et_personal_id_login_id.text.toString()
            var password = et_password_login_id.text.toString()

            for (data in 0 until dataLogin.size) {



                   if (dataLogin[data].personalID.equals(personalID)) {
                       position = data
                       break
                   }


            }
            if(position==-1)
                Toast.makeText(applicationContext, "Please sign Up", Toast.LENGTH_SHORT).show()
            else if (dataLogin[position!!].password == password) {

                savedIdToSharedPreferences(dataLogin[position!!])

                var goToMain=Intent(this,Main::class.java)
                startActivity(goToMain)
                finish()

            }

        }

    }

    private fun getID(): String
    {
        var sharedPreferences=getSharedPreferences("Information", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_id","").toString()
    }

    private fun savedIdToSharedPreferences(data:DataBaseEmergencyUser) {

        var sharedPreferences=getSharedPreferences("Information", Context.MODE_PRIVATE)
        var editor=sharedPreferences.edit()
        editor.putString("user_id",data.id)
        editor.putString("user_personalID",data.personalID)
        editor.putString("user_check",data.check)
        editor.putString("user_name",data.name)
        editor.putString("user_mothername",data.mothername)
        editor.putString("user_birthday",data.birthday)
        editor.putString("user_governorate",data.governorate)
        editor.putString("user_gender",data.gender)
        editor.putString("user_password",data.password)
        editor.putString("user_phone",data.phone)


        editor.commit()
    }
    private fun connectDataBase()
    {
        var database=Firebase.database
        mRefEmergencyUser=database.getReference("Emergency_user")

    }
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