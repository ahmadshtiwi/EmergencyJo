package com.example.emergencyjo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.drawer_main_id
import kotlinx.android.synthetic.main.activity_main.nav_side_list_id
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_user_setting.*
import kotlinx.android.synthetic.main.header_side_list.view.*
import java.util.*
import kotlin.system.exitProcess

class UserSetting :AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

     private  lateinit var userName:String
     private  lateinit var phoneNumber:String
     private  lateinit var toolbar: Toolbar
     private  lateinit var mRefData: DatabaseReference
     private  lateinit var headerView:View

     //Language change variables
    private var currentLanguage = "en"
    private var currentLang: String? = null
    lateinit var locale: Locale


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_setting)
        toolbar = findViewById(R.id.header_id)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        connectActionbar()
        connectDataBase()

        //----------//
        //Toast.makeText(applicationContext, "$userID", Toast.LENGTH_SHORT).show()
        userName=getName()
        phoneNumber=getPhone()
        headerActionBar()
        showInformation()
        //Get Name And Phone Number

        //Change password button
        btn_change_password_setting.setOnClickListener {
           val goToChangePassword = Intent(this,ChangePassword::class.java)
            startActivity(goToChangePassword)
        }
        btn_change_phone_setting.setOnClickListener {
            var gotochangephone = Intent(this,ChangePhone::class.java)
            startActivity(gotochangephone)
        }
     //Change Language
        currentLanguage = intent.getStringExtra(currentLang).toString()
        ar_radio.setOnClickListener{
            setLocale("ar")

        }
        en_radio.setOnClickListener{
            setLocale("en")
        }
    }
   //Set Language
    private fun setLocale(localeName: String) {
        if (localeName != currentLanguage) {
            locale = Locale(localeName)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(
                this, Main::class.java
            )
            refresh.putExtra(currentLang, localeName)
            startActivity(refresh)
        } else {
            Toast.makeText(this@UserSetting, "Language, , already, , selected)!", Toast.LENGTH_SHORT).show();
        }
    }



    //Store Name and phone
    private fun showInformation() {
        tv_name_setting_id.text=userName
        tv_phone_setting_id.text=phoneNumber
    }
//Store Name in header
    private fun  headerActionBar()
    {

        headerView =nav_side_list_id.getHeaderView(0)
        headerView.name_user_side_list_id.text=userName

    }
//Get Name
    @JvmName("getName1")
    private fun getName(): String
    {
        val sharedPreferences=getSharedPreferences(UserProperties.FILE_NAME_SHARED_INFORMATION, Context.MODE_PRIVATE)
        return sharedPreferences.getString(UserProperties.USER_NAME,"").toString()
    }
//Get Phone
    @JvmName("getPhone")
    private fun getPhone(): String
    {
        val sharedPreferences=getSharedPreferences(UserProperties.FILE_NAME_SHARED_INFORMATION, Context.MODE_PRIVATE)
        return sharedPreferences.getString(UserProperties.USER_PHONE,"").toString()
    }
    private fun connectActionbar()
    {
        val actionToggle= ActionBarDrawerToggle(this,drawer_main_id,toolbar,R.string.drawer_open,R.string.drawer_close)
        drawer_main_id.addDrawerListener(actionToggle)
        actionToggle.syncState()
        nav_side_list_id.setNavigationItemSelectedListener(this)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {


            R.id.logout_side_list_id -> {
                editIdToSharedPreferences()
                val goToLogin = Intent(this, Login::class.java)
                startActivity(goToLogin)
                finish()
            }


            R.id.safety_Instructions_side_list_id -> {
                val goToSafety = Intent(this, ChooseSafety::class.java)
                startActivity(goToSafety)
                finish()
            }

            R.id.setting_side_list_id -> {
                val goToSetting = Intent(this, UserSetting::class.java)
                startActivity(goToSetting)
                finish()
            }

            R.id.home_side_list_id -> {
                val goToMain = Intent(this, Main::class.java)
                startActivity(goToMain)
                finish()
            }
        }

        //Toast.makeText(this, "$item", Toast.LENGTH_SHORT).show()
        closeDrawer()
        return true
    }

    override fun onBackPressed() {
        if(drawer_main_id.isDrawerOpen(GravityCompat.START))
            closeDrawer()
        else
            super.onBackPressed()
    }

    private fun closeDrawer()
    {
        drawer_main_id.closeDrawer(GravityCompat.START)

    }
    private fun editIdToSharedPreferences() {

        val sharedPreferences=getSharedPreferences(UserProperties.FILE_NAME_SHARED_INFORMATION, Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()
        editor.putString(UserProperties.USER_ID,"0")
        editor.apply()
    }
    private fun connectDataBase()
    {
        val database= Firebase.database
        mRefData=database.getReference("Emergency_user")

    }
}
