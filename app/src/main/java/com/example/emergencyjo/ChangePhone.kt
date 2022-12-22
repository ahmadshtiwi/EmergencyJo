package com.example.emergencyjo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_change_phone.*


class ChangePhone : BaseActivity() {

    private var mRefEmergencyUser: DatabaseReference? = null
    private var position: Int = -1
    var dataBaseEmergencyUser: ArrayList<DataBaseEmergencyUser>? = null



    private fun showComponents() {

        et_phone_id_changephone.visibility = View.VISIBLE
        et_phone_layout_changephone.visibility = View.VISIBLE
        btn_change_phone_changephone_id.visibility = View.VISIBLE
        lock_icon_changephone_id.visibility = View.INVISIBLE


    }

    private fun hideComponents() {

        et_phone_id_changephone.visibility = View.INVISIBLE
        et_phone_layout_changephone.visibility = View.INVISIBLE
        btn_change_phone_changephone_id.visibility = View.INVISIBLE
        lock_icon_changephone_id.visibility = View.INVISIBLE

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_phone)
        hideComponents()
        connectDatabase()

        dataBaseEmergencyUser = ArrayList()

        mRefEmergencyUser?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (data in snapshot.children) {
                    dataBaseEmergencyUser?.add(data.getValue(DataBaseEmergencyUser::class.java)!!)
                }

            }

            override fun onCancelled(error: DatabaseError) {}

        })

        btn_check_information_changephone_id.setOnClickListener()
        {
            for (index in 0 until dataBaseEmergencyUser!!.size) {
                if (dataBaseEmergencyUser!![index].personalID == et_personal_id_changephonecheck_id.text.toString()) {
                    position = index
                    break
                }
            }
            if (dataBaseEmergencyUser!![position].check == et_check_number_changephonecheck_id.text.toString()) {
                showComponents()
            } else {
                showAlert()
            }
            btn_change_phone_changephone_id.setOnClickListener {

                if (!Expression.expPhone.matches(et_phone_id_changephone.text.toString())) {
                    showMessageError(
                        et_phone_id_changephone,
                        "Enter a Phone like (078/7/9/xxxxxxx)"
                    )
                } else {
                    showAlertChange()
                }
            }
        }
    }

    private fun connectDatabase() {
        val database = Firebase.database
        mRefEmergencyUser = database.getReference("Emergency_user")

    }
    private fun showMessageError(item: EditText, message: String) {
        item.error = message
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }
    private fun showAlert() {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setMessage("Wrong Information")
        alertBuilder.setPositiveButton("Ok", null)
        val alert = alertBuilder.create()
        alert.show()
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener()
        {
            alert.dismiss()
        }
    }

    private fun showAlertChange() {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setMessage(R.string.message_sure_change_phone)
        alertBuilder.setPositiveButton(R.string.yes, null)
        alertBuilder.setNegativeButton(R.string.no, null)

        val alert = alertBuilder.create()
        alert.show()
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener()
        {
            mRefEmergencyUser?.child(et_personal_id_changephonecheck_id.text.toString())
                ?.setValue(
                    DataBaseEmergencyUser(
                        dataBaseEmergencyUser!![position].id,
                        dataBaseEmergencyUser!![position].personalID,
                        dataBaseEmergencyUser!![position].check,
                        dataBaseEmergencyUser!![position].name,
                        dataBaseEmergencyUser!![position].mothername,
                        dataBaseEmergencyUser!![position].gender,
                        dataBaseEmergencyUser!![position].governorate,
                        dataBaseEmergencyUser!![position].birthday,
                        dataBaseEmergencyUser!![position].password,
                        et_phone_id_changephone.text.toString(),
                    )
                )
            hideComponents()
            alert.dismiss()
        }
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener()
        {
            alert.dismiss()
        }


    }



}