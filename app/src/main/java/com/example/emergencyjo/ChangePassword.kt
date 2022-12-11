package com.example.emergencyjo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_sign_up.*


class ChangePassword : AppCompatActivity() {


    var mRefEmergencyUser: DatabaseReference?=null
    var mRefVCivilAffairs: DatabaseReference?=null
    var position:Int=-1
    var dataCivilAffairs:ArrayList<DataBaseCivilAffairs>?=null
    var dataBaseEmergencyUser:ArrayList<DataBaseEmergencyUser>?=null


    private fun showComponents() {

        et_password_layout_changepassword.visibility= View.VISIBLE
        et_re_password_layout_changepassword.visibility= View.VISIBLE

    }
    private fun hideComponents() {

        et_password_layout.visibility=View.INVISIBLE
        et_re_password_layout.visibility=View.INVISIBLE
    }

    /*********************************** on Create *****************************************************/
    //Main class
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        hideComponents()
        connectDatabase()

        dataCivilAffairs = ArrayList()
        dataBaseEmergencyUser = ArrayList()

        mRefVCivilAffairs?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (d in snapshot.children) {
                    var objData = d.getValue(DataBaseCivilAffairs::class.java)
                    dataCivilAffairs!!.add(objData!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })

        btn_compareinfromation_changepassword.setOnClickListener()
        {
            var personalId = et_personal_id_changepasswordcheck_id.text.toString()
            var check = et_check_number_changepasswordcheck_id.text.toString()
            if (personalId.isEmpty())
                Toast.makeText(this, R.string.message_personal_id_empty, Toast.LENGTH_SHORT).show()
            else if (check.isEmpty())
                Toast.makeText(this, R.string.message_check_number_empty, Toast.LENGTH_SHORT).show()
            else {
                for (i in 0 until dataCivilAffairs!!.size) {
                    if (dataCivilAffairs!![i].personalID == personalId) {
                        position = i
                        break
                    }
                }
                if (dataCivilAffairs!![position].check == check) {
                    showComponents()
                }
            }
        }
        btn_change_password_changepassword.setOnClickListener {
           var password = et_password_id_changepassword.text.toString()
           var rePassword = et_re_password_changepassword.text.toString()
            if(password.length<8)
            {
                Toast.makeText(this, R.string.message_strong_password, Toast.LENGTH_SHORT).show()
           }
            else if (!password.equals(rePassword))
                Toast.makeText(this, R.string.message_match_password, Toast.LENGTH_SHORT).show()
            else
            {
                updatepassword(password)
                Toast.makeText(this,"Password Changed",Toast.LENGTH_SHORT)
                finish() // end activity when store data in database and shared preferences
            }
        }
    }

   private fun updatepassword(password: String) {

       mRefEmergencyUser?.child("password")?.setValue(password);

   }

    private fun connectDatabase() {
        var database= Firebase.database
        mRefVCivilAffairs=database.getReference("Civil Affairs")
        mRefEmergencyUser=database.getReference("Emergency_user")
    }
}