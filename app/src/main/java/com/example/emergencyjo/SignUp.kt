package com.example.emergencyjo


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    fun showComponents()
    {
        tv_name_signup_id.visibility=View.VISIBLE
        tv_birthday_signup_id.visibility=View.VISIBLE
        tv_gender_signup_id.visibility=View.VISIBLE
        tv_governorate_signup_id.visibility=View.VISIBLE
        et_password_signup_id.visibility=View.VISIBLE
        et_re_password_signup_id.visibility=View.VISIBLE
        btn_create_account_id.visibility=View.VISIBLE
        tv_mother_name_signup_id.visibility= View.VISIBLE
        lock_icon_id.visibility=View.VISIBLE
    }
    fun hideComponents()
    {
        tv_name_signup_id.visibility=View.INVISIBLE
        tv_birthday_signup_id.visibility=View.INVISIBLE
        tv_gender_signup_id.visibility=View.INVISIBLE
        tv_governorate_signup_id.visibility=View.INVISIBLE
        et_password_signup_id.visibility=View.INVISIBLE
        et_re_password_signup_id.visibility=View.INVISIBLE
        btn_create_account_id.visibility=View.INVISIBLE
        tv_mother_name_signup_id.visibility= View.INVISIBLE
        lock_icon_id.visibility=View.INVISIBLE
    }
    fun getDataForComponetns()
    {
        tv_name_signup_id.text="الاسم: احمد عبد الفتاح محمود شتيوي"
        tv_gender_signup_id.text="الجنس : ذكر"
        tv_birthday_signup_id.text="الميلاد : 23/2/2000"
        tv_governorate_signup_id.text="المحافظة : المفرق"
        tv_mother_name_signup_id.text="اسم الام :  "

    }

    //Main class
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        hideComponents()

        btn_get_data_id.setOnClickListener()
        {

            var personalId=et_personal_id_signup_id.text.toString()
            var qualifierNumber=et_qualifier_number_signup_id.text.toString()


            if(personalId.isEmpty())
                Toast.makeText(baseContext,R.string.message_personal_id_empty,Toast.LENGTH_SHORT).show()

            else if(qualifierNumber.isEmpty())
                Toast.makeText(baseContext,R.string.message_qualifier_number_empty,Toast.LENGTH_SHORT).show()
            else if(personalId.equals("2000033442")&&qualifierNumber.equals("222/050"))
            {
                showComponents()
                getDataForComponetns()
            }
            else
            {
                hideComponents()
            }

        }


    }
}