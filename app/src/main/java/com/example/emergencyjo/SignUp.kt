package com.example.emergencyjo


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {


    var mRefEmergencyUser:DatabaseReference?=null
    var mRefVCivilAffairs:DatabaseReference?=null
    var position:Int=-1
    var dataCivilAffairs:ArrayList<DataBaseCivilAffairs>?=null
    var userProperties=UserProperties()

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
        et_phone_number_id.visibility=View.VISIBLE
        et_password_layout.visibility=View.VISIBLE
        et_phone_number_layout.visibility=View.VISIBLE
        et_re_password_layout.visibility=View.VISIBLE



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
        et_phone_number_id.visibility=View.INVISIBLE
        et_password_layout.visibility=View.INVISIBLE
        et_phone_number_layout.visibility=View.INVISIBLE
        et_re_password_layout.visibility=View.INVISIBLE
    }
/*********************************** on Create *****************************************************/
    //Main class
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        hideComponents()
        connectDatabase()

        dataCivilAffairs= ArrayList()


         mRefVCivilAffairs?.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for(d in snapshot.children)
                {
                    var objData=d.getValue(DataBaseCivilAffairs::class.java)
                    dataCivilAffairs!!.add(objData!!)
                }
            }
            override fun onCancelled(error: DatabaseError) {}

        })


        btn_get_data_id.setOnClickListener()
        {
            var personalId=et_personal_id_signup_id.text.toString()
            var check =et_check_number_signup_id.text.toString()

            if(personalId.isEmpty())
                Toast.makeText(this, R.string.message_personal_id_empty, Toast.LENGTH_SHORT).show()
            else  if(check.isEmpty())
                Toast.makeText(this, R.string.message_check_number_empty, Toast.LENGTH_SHORT).show()
            else
            {
                for(i in 0 until dataCivilAffairs!!.size)
                {
                    if(dataCivilAffairs!![i].personalID==personalId)
                    {
                        position=i
                        break
                    }

                }

                if(dataCivilAffairs!![position].check==check)
                {
                    showComponents()
                    tv_name_signup_id.text=dataCivilAffairs!![position].name
                    tv_birthday_signup_id.text=dataCivilAffairs!![position].birthday
                    tv_gender_signup_id.text=dataCivilAffairs!![position].gender
                    tv_governorate_signup_id.text=dataCivilAffairs!![position].governorate
                    tv_mother_name_signup_id.text=dataCivilAffairs!![position].mothername


                }


            }

        }
        btn_create_account_id.setOnClickListener()
        {
            var password=et_password_signup_id.text.toString()
            var rePassword=et_re_password_signup_id.text.toString()
            var phone=et_phone_number_id.text.toString()
            if(password.length<8)
            {
                Toast.makeText(this, R.string.message_strong_password, Toast.LENGTH_SHORT).show()
            }
            else if (!password.equals(rePassword))
                Toast.makeText(this, R.string.message_match_password, Toast.LENGTH_SHORT).show()
            else
            {
                var obj=DataBaseEmergencyUser(dataCivilAffairs!![position].id,dataCivilAffairs!![position].personalID ,
                    dataCivilAffairs!![position].check,dataCivilAffairs!![position].name,dataCivilAffairs!![position].mothername,
                    dataCivilAffairs!![position].gender,dataCivilAffairs!![position].governorate,dataCivilAffairs!![position].birthday,
                   password,phone)
                mRefEmergencyUser?.child(dataCivilAffairs!![position].id!!)?.setValue(obj)

                savedIdToSharedPreferences(obj) // save id in file shared preferences

                var goToMain=Intent(this,Main::class.java)
                startActivity(goToMain)                             // got main activity

                finish() // end activity when store data in database and shared preferences
            }
        }

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


    private fun connectDatabase()
    {
        var database= Firebase.database
        mRefVCivilAffairs=database.getReference("Civil Affairs")
        mRefEmergencyUser=database.getReference("Emergency_user")

    }
}



