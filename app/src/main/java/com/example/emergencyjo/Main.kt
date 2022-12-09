package com.example.emergencyjo

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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



lateinit var userName:String
lateinit var toolbar:Toolbar
lateinit var mRefData:DatabaseReference
lateinit var headerView:View
//lateinit var objDataBaseEmergencyUser: DataBaseEmergencyUser
    var name:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        toolbar = findViewById<Toolbar>(R.id.header_id)

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
            intent=Intent(this,MapsActivity::class.java)
            startActivity(intent)
        }

        tv_fire_id.setOnClickListener()
        {
            var fire=tv_fire_id.text.toString()
            et_descrption_box_id.setText(fire)
        }
        tv_accident_id.setOnClickListener()
        {
            var accedent=tv_accident_id.text.toString()
            et_descrption_box_id.setText(accedent)
        }
        tv_stroke_id.setOnClickListener()
        {
            var stroke=tv_stroke_id.text.toString()
            et_descrption_box_id.setText(stroke)

        }
        btn_direct_call_id.setOnClickListener()
        {
            var goToCall =Intent(Intent.ACTION_DIAL, Uri.parse("tel:911"))
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
        var sharedPreferences=getSharedPreferences("Information", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_name","").toString()
    }
    private fun connectActionbar()
    {
        var actionToggle=ActionBarDrawerToggle(this,drawer_main_id,toolbar,R.string.drawer_open,R.string.drawer_close)
        drawer_main_id.addDrawerListener(actionToggle)
        actionToggle.syncState()
        nav_side_list_id.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        var item=item.itemId
        when(item)
        {
            R.id.logout_side_list_id->{
                editIdToSharedPreferences()
            var goToLogin=Intent(this,Login::class.java)
                startActivity(goToLogin)
                finish()
            }

        }
        Toast.makeText(this, "$item", Toast.LENGTH_SHORT).show()
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

        var sharedPreferences=getSharedPreferences("Information", Context.MODE_PRIVATE)
        var editor=sharedPreferences.edit()
        editor.putString("user_id","0")



        editor.commit()
    }
private fun connectDataBase()
{
    var database=Firebase.database
    mRefData=database.getReference("Emergency_user")

}


//    fun setDescription(view: View)
//    {
//
//
//
//    }

}
