package com.example.emergencyjo

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class Login:AppCompatActivity(),TextWatcher{

    private lateinit var dataLogin:ArrayList<DataBaseEmergencyUser>
    private var mRefEmergencyUser: DatabaseReference?=null
    private var position:Int?=-1



/******************************* on create ***************************************************/

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    //Check Internet
    if ( ! isNetworkConnected() ) {

        var goToDirectCall = Intent(this, DirectCall::class.java)
        startActivity(goToDirectCall)


    }


    val test=getID()
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
            val personalID = et_personal_id_login_id.text.toString()
            val password = et_password_login_id.text.toString()

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
    direct_call_login_button_id.setOnClickListener()
    {
        val goToCall =Intent(Intent.ACTION_DIAL, Uri.parse("tel:911"))
        startActivity(goToCall)
    }

    forget_password_id.setOnClickListener{
        var forgetpassword = Intent(this,forgetpassword::class.java)
        startActivity(forgetpassword)
    }
    }

    private fun getID(): String
    {
        val sharedPreferences=getSharedPreferences("Information", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_id","").toString()
    }

    private fun savedIdToSharedPreferences(data:DataBaseEmergencyUser) {

        val sharedPreferences=getSharedPreferences(UserProperties.FILE_NAME_SHARED_INFORMATION, Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()


        editor.putString(UserProperties.USER_ID,data.id)
        editor.putString(UserProperties.USER_PERSONAL_ID,data.personalID)
        editor.putString(UserProperties.USER_CHECK,data.check)
        editor.putString(UserProperties.USER_NAME,data.name)
        editor.putString(UserProperties.USER_MOTHERNAME,data.mothername)
        editor.putString(UserProperties.USER_BIRTHDAY,data.birthday)
        editor.putString(UserProperties.USER_GOVERNORATE,data.governorate)
        editor.putString(UserProperties.USER_GENDER,data.gender)
        editor.putString(UserProperties.USER_PASSWORD,data.password)
        editor.putString(UserProperties.USER_PHONE,data.phone)


        editor.apply()
    }
    private fun connectDataBase()
    {
        val database=Firebase.database
        mRefEmergencyUser=database.getReference("Emergency_user")

    }
    private fun goToSignUpActivity()
    {
        val goToSignUp = Intent(this, SignUp::class.java)
        startActivity(goToSignUp)
    }
    private fun goToMainActivity()
    {
        val goToMain = Intent (this , Main :: class.java)
        startActivity(goToMain)
    }
    private fun messageDialogToSignUp()
    {
        val alertBuilder=AlertDialog.Builder(this)
        alertBuilder.setTitle(R.string.message_not_found)
        alertBuilder.setMessage(R.string.message_signup)

        alertBuilder.setPositiveButton(R.string.btn_signup_alertdialog) { _, _ ->

            goToSignUpActivity()

        }// end positive button

        alertBuilder.setNeutralButton(R.string.btn_cancel_alertdialog,null)
        val alert=alertBuilder.create()
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

    //Check Internet
    private fun isNetworkConnected(): Boolean {
        var cm : ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager;

        return (cm.activeNetworkInfo != null) && (cm.activeNetworkInfo!!.isConnected)
    }
}
