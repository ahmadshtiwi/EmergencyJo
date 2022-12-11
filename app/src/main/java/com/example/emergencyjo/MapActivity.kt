package com.example.emergencyjo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity() ,OnMapReadyCallback , GoogleMap.OnMarkerClickListener{

    private var mapFragment : SupportMapFragment?= null
    private lateinit var mMap: GoogleMap
    private lateinit var lastlocation : Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private  var  database = Firebase.database
    private  var  reference = database.getReference("UserLocation")
    var userProperties=UserProperties()

    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true

        mMap.setOnMarkerClickListener(this)
        setUpMap()
    }
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
            if(location != null) {
                lastlocation = location
                    val currentLatLong = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
               RequestLocation.setOnClickListener {


                   var alertBuilder=AlertDialog.Builder(this)
                   alertBuilder.setMessage("Are You Sure To Request")
                   alertBuilder.setPositiveButton("Yes",null)
                   alertBuilder.setNegativeButton("No",null)
                   var alert=alertBuilder.create()
                   alert.show()
                   alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener()
                   {
                       placeMarkerOnMap(currentLatLong)
                       val key = reference.push().key
                        if (key != null) {

                        val reminder = Reminder(key, location.latitude, location.longitude,getInfo(userProperties.USER_PERSONAL_ID),getInfo(userProperties.USER_NAME),getInfo(userProperties.USER_PHONE))//,getInfo(userProperties.USER_DES))
                        reference.child(key).setValue(reminder)
                            finish()
                    }
                       alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener()
                       {
                           alert.dismiss()
                       }

                }
               }
            }
        }
    }
    private fun placeMarkerOnMap(currentLatLong: LatLng) {

        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")
        mMap.addMarker(markerOptions)
    }

    private fun getInfo(data:String): String
    {
        var sharedPreferences=getSharedPreferences(userProperties.FILE_NAME_SHARED_INFORMATION, Context.MODE_PRIVATE)
        return sharedPreferences.getString(data,"").toString()
    }
//    private fun getName(): String
//    {
//        var sharedPreferences=getSharedPreferences(userProperties.FILE_NAME_SHARED_INFORMATION, Context.MODE_PRIVATE)
//        return sharedPreferences.getString(userProperties.USER_NAME,"").toString()
//    }
//    private fun getPhone(): String
//    {
//        var sharedPreferences=getSharedPreferences(userProperties.FILE_NAME_SHARED_INFORMATION, Context.MODE_PRIVATE)
//        return sharedPreferences.getString(userProperties.USER_PHONE,"").toString()
//    }


    override fun onMarkerClick(p0: Marker) = false

}