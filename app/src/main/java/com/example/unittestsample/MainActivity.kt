package com.example.unittestsample

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.preference.PreferenceManager
import java.util.*


class MainActivity : Activity() {

    private var mSharedPreferencesHelper: SharedPreferencesHelper? = null

    private var mNameText: EditText? = null

    private var mDobPicker: DatePicker? = null

    private var mEmailText: EditText? = null

    private var mEmailValidator: EmailValidator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNameText = findViewById<View>(R.id.userNameInput) as EditText
        mDobPicker = findViewById<View>(R.id.dateOfBirthInput) as DatePicker
        mEmailText = findViewById<View>(R.id.emailInput) as EditText

        mEmailValidator = EmailValidator()
        mEmailText!!.addTextChangedListener(mEmailValidator)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        mSharedPreferencesHelper = SharedPreferencesHelper(sharedPreferences)

        populateUi()

    }

    private fun populateUi() {

        val sharedPreferenceEntry: SharedPreferenceEntry
        sharedPreferenceEntry = mSharedPreferencesHelper!!.personalInfo
        mNameText!!.setText(sharedPreferenceEntry.name)
        val dateOfBirth = sharedPreferenceEntry.dateOfBirth
        mDobPicker!!.init(
            dateOfBirth[Calendar.YEAR], dateOfBirth[Calendar.MONTH],
            dateOfBirth[Calendar.DAY_OF_MONTH], null
        )
        mEmailText!!.setText(sharedPreferenceEntry.email)
    }

    fun onSaveClick(view: View?) {

        if (!mEmailValidator!!.isValid) {
            mEmailText!!.error = "Invalid email"
            Log.w(TAG, "Invalid email")
            return
        }

        val name = mNameText!!.text.toString()
        val dateOfBirth = Calendar.getInstance()
        dateOfBirth[mDobPicker!!.year, mDobPicker!!.month] = mDobPicker!!.dayOfMonth
        val email = mEmailText!!.text.toString()

        val sharedPreferenceEntry = SharedPreferenceEntry(name, dateOfBirth, email)

        val isSuccess = mSharedPreferencesHelper!!.savePersonalInfo(sharedPreferenceEntry)
        if (isSuccess) {
            Toast.makeText(this, "Data saved", Toast.LENGTH_LONG).show()
            Log.i(TAG, "Saved")
        } else {
            Log.e(TAG, "Failed")
        }
    }


    fun onRevertClick(view: View?) {
        populateUi()
        Toast.makeText(this, "Data reverted", Toast.LENGTH_LONG).show()
        Log.i(TAG, "Data reverted")
    }

    companion object {

        private const val TAG = "MainActivity"
    }
}