package com.example.emergencyjo

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.header_side_list.view.*


class Main : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {



private lateinit var userName:String
private lateinit var toolbar:Toolbar
private lateinit var mRefData:DatabaseReference
private lateinit var headerView:View

    var name:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Check Internet
        if ( ! isNetworkConnected() ) {

            var goToDirectCall = Intent(this, DirectCall::class.java)
            startActivity(goToDirectCall)


        }

        toolbar = findViewById(R.id.header_id)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        connectActionbar()

        connectDataBase()

        userName=getName()
        //Toast.makeText(applicationContext, "$userID", Toast.LENGTH_SHORT).show()

        headerActionBar()
    }

    override fun onStart() {
        super.onStart()

        btn_map_id.setOnClickListener()
        {
            if(et_description_box_id.text.toString().isNotEmpty()){



                intent=Intent(this,MapActivity::class.java)
                intent.putExtra("description",et_description_box_id.text.toString())
               startActivity(intent)
            }
            else
           {
               et_description_box_id.error="Set Description "
           }


        }

        tv_fire_id.setOnClickListener()
        {
            val fire=tv_fire_id.text.toString()
            et_description_box_id.setText(fire)
        }
        tv_accident_id.setOnClickListener()
        {
            val accident=tv_accident_id.text.toString()
            et_description_box_id.setText(accident)
        }
        tv_stroke_id.setOnClickListener()
        {
            val stroke=tv_stroke_id.text.toString()
            et_description_box_id.setText(stroke)

        }
        btn_direct_call_id.setOnClickListener()
        {
            val goToCall =Intent(Intent.ACTION_DIAL, Uri.parse("tel:911"))
            startActivity(goToCall)
        }


    }
    private fun  headerActionBar()
    {
        headerView =nav_side_list_id.getHeaderView(0)
        headerView.name_user_side_list_id.text=userName

    }

    @JvmName("getName1")
    private fun getName(): String
    {
        val sharedPreferences=getSharedPreferences(UserProperties.FILE_NAME_SHARED_INFORMATION, Context.MODE_PRIVATE)
        return sharedPreferences.getString(UserProperties.USER_NAME,"").toString()
    }
    private fun connectActionbar()
    {
        val actionToggle=ActionBarDrawerToggle(this,drawer_main_id,toolbar,R.string.drawer_open,R.string.drawer_close)
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
                val goToSafety = Intent(this, choosesafty::class.java)
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
    val database=Firebase.database
    mRefData=database.getReference("Emergency_user")

}

//internet fun

    private fun isNetworkConnected(): Boolean {
        var cm : ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager;

        return (cm.activeNetworkInfo != null) && (cm.activeNetworkInfo!!.isConnected)
    }


}
