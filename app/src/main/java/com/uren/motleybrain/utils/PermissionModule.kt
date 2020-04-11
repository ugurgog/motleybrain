package com.uren.motleybrain.utils


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

import androidx.core.app.ActivityCompat

class PermissionModule(private val mContext: Context) {

    //camera permission =================================================
    fun checkCameraPermission(): Boolean {

        return ActivityCompat.checkSelfPermission(
            mContext,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    //WriteExternalStorage permission =================================================
    fun checkWriteExternalStoragePermission(): Boolean {

        return ActivityCompat.checkSelfPermission(
            mContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    //RecordAudio permission =================================================
    fun checkRecordAudioPermission(): Boolean {

        return ActivityCompat.checkSelfPermission(
            mContext,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    //AccessFineLocation permission =================================================
    fun checkAccessFineLocationPermission(): Boolean {

        return ActivityCompat.checkSelfPermission(
            mContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    //READ_PHONE_STATE permission =================================================
    fun checkReadPhoneStatePermission(): Boolean {

        return ActivityCompat.checkSelfPermission(
            mContext,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    //READ_PHONE_NUMBERS permission =================================================
    fun checkReadPhoneNumbersPermission(): Boolean {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.READ_PHONE_NUMBERS
            ) == PackageManager.PERMISSION_GRANTED
        } else false
    }

    //READ_CONTACTS permission =================================================
    fun checkReadContactsPermission(): Boolean {

        return ActivityCompat.checkSelfPermission(
            mContext,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }
}