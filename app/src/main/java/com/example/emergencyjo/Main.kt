package com.example.emergencyjo

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.zeugmasolutions.localehelper.Locales
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.header_side_list.view.*


class Main : BaseActivity(), NavigationView.OnNavigationItemSelectedListener  {



private lateinit var userName:String
private lateinit var toolbar:Toolbar
private lateinit var mRefData:DatabaseReference
private lateinit var headerView:View
private var statusData: ArrayList<Status>?=null
private lateinit var mRefStatus: DatabaseReference

    var name:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusData=ArrayList()

        //Check Internet
        if ( ! isNetworkConnected() ) {

            val goToDirectCall = Intent(this, DirectCall::class.java)
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

        mRefStatus.addValueEventListener(object:ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {

                for(d in snapshot.children)
                {
                  //  statusData?.clear()
                    statusData?.add(d.getValue(Status::class.java)!!)
                    //Toast.makeText(baseContext, "j", Toast.LENGTH_SHORT).show()
                }
                val adapter=AdapterStatus(baseContext,R.layout.status_view,statusData!!)
                gd_status_id.adapter=adapter

            }

            override fun onCancelled(error: DatabaseError) {}

        })



    }

    override fun onStart() {
        super.onStart()


        gd_status_id.onItemClickListener= AdapterView.OnItemClickListener { parent, view, position, id->

            et_description_box_id.setText(statusData!![position].case_description)

        }


        btn_remove_text_id.setOnClickListener {
            et_description_box_id.setText("")
        }

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
                val goToSafety = Intent(this, ChooseSafety::class.java)
                startActivity(goToSafety)
                //finish()
            }

            R.id.setting_side_list_id -> {
                val goToSetting = Intent(this, UserSetting::class.java)
                startActivity(goToSetting)
               // finish()
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
    mRefStatus=database.getReference("Common Cases")


}

//internet fun

    private fun isNetworkConnected(): Boolean {
        val cm : ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return (cm.activeNetworkInfo != null) && (cm.activeNetworkInfo!!.isConnected)
    }


}
