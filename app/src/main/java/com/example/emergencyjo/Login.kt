package com.example.emergencyjo

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.emergencyjo.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class Login:AppCompatActivity(),TextWatcher{

    private lateinit var dataLogin:ArrayList<DataBaseEmergencyUser>
    private var mRefEmergencyUser: DatabaseReference?=null
    private var position:Int?=-1

    var userProperties=UserProperties()


/******************************* on create ***************************************************/

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

         var test=getID()
         if(test.length==10)
         {
             goToMainActivity()
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
            }// end data chang
            override fun onCancelled(error: DatabaseError) {}

        })//end add value event listener


        et_password_login_id.addTextChangedListener(this)  // using text watcher
        et_personal_id_login_id.addTextChangedListener(this) //using text watcher
    }//end onCreate method

/***************************************** on start ***************************************/

    override fun onStart() {
        super.onStart()

        signup_button_id.setOnClickListener() // Signup in button { use intent to go Sign Up activity }
        {
          goToSignUpActivity()
        }//end signup button

        login_button_id.setOnClickListener()
        {
            var personalID = et_personal_id_login_id.text.toString()
            var password = et_password_login_id.text.toString()

            for (data in 0 until dataLogin.size) {      // get data from array list and check id found

                   if (dataLogin[data].personalID.equals(personalID)) {
                       position = data
                       break
                   }

            }
            if(position==-1) {
             messageDialogToSignUp()
            } // end if

                else if (dataLogin[position!!].password == password) {

                savedIdToSharedPreferences(dataLogin[position!!])           // save data in shared preferences
               goToMainActivity()
                finish()

            } // end else

        }

    }

    private fun getID(): String
    {
        var sharedPreferences=getSharedPreferences(userProperties.FILE_NAME_SHARED_INFORMATION, Context.MODE_PRIVATE)
        return sharedPreferences.getString(userProperties.USER_ID,"").toString()
    }

    private fun savedIdToSharedPreferences(data:DataBaseEmergencyUser) {

        var sharedPreferences=getSharedPreferences(userProperties.FILE_NAME_SHARED_INFORMATION, Context.MODE_PRIVATE)
        var editor=sharedPreferences.edit()


        editor.putString(userProperties.USER_ID,data.id)
        editor.putString(userProperties.USER_PERSONAL_ID,data.personalID)
        editor.putString(userProperties.USER_CHECK,data.check)
        editor.putString(userProperties.USER_NAME,data.name)
        editor.putString(userProperties.USER_MOTHERNAME,data.mothername)
        editor.putString(userProperties.USER_BIRTHDAY,data.birthday)
        editor.putString(userProperties.USER_GOVERNORATE,data.governorate)
        editor.putString(userProperties.USER_GENDER,data.gender)
        editor.putString(userProperties.USER_PASSWORD,data.password)
        editor.putString(userProperties.USER_PHONE,data.phone)


        editor.commit()
    }
    private fun connectDataBase()
    {
        var database=Firebase.database
        mRefEmergencyUser=database.getReference("Emergency_user")

    }
    private fun goToSignUpActivity()
    {
        val goToSignUp = Intent(this, SignUp::class.java)
        startActivity(goToSignUp)
    }
    private fun goToMainActivity()
    {
        var goToMain = Intent (this , Main :: class.java)
        startActivity(goToMain)
    }
    private fun messageDialogToSignUp()
    {
        var alertBuilder=AlertDialog.Builder(this)
        alertBuilder.setTitle(R.string.message_not_found)
        alertBuilder.setMessage(R.string.message_signup)

        alertBuilder.setPositiveButton(R.string.btn_signup_alertdialog) { dialogInterface, which->

            goToSignUpActivity()

        }// end positive button

        alertBuilder.setNeutralButton(R.string.btn_cancel_alertdialog,null)
        var alert=alertBuilder.create()
        alert.show()
        alert.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener()
        {
            alert.cancel()
        }
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
