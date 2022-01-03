package com.example.and_per.utils

import android.Manifest.permission.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

/**
 * Helper functions to simplify permission checks/requests.
 */
fun Context.hasPermission(permission: String): Boolean {

    // Background permissions didn't exit prior to Q, so it's approved by default.
    if (permission == ACCESS_BACKGROUND_LOCATION && SDK_INT < Q) {
        return true
    } else if (permission == READ_EXTERNAL_STORAGE && SDK_INT > Q) {
        return true
    } else if (permission == WRITE_EXTERNAL_STORAGE && SDK_INT > Q) {
        return true
    }

    return ContextCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED
}

fun Fragment.isPermissionPermanentDenied(permission: String): Boolean {
    return shouldShowRequestPermissionRationale(permission)
}

/**
 * Requests permission and if the user denied a previous request, but didn't check
 * "Don't ask again", we provide additional rationale.
 *
 * Note: The Snackbar should have an action to request the permission.
 */
fun Fragment.requestPermissionWithRationale(
    permission: String,
    requestCode: Int,
    snackbar: Snackbar
) {
    val provideRationale = shouldShowRequestPermissionRationale(permission)

    if (provideRationale) {
        Log.d("TAG", "provideRationale iff")
        snackbar.show()
    } else {
        Log.d("TAG", "provideRationale ELSE")
        requestPermissions(arrayOf(permission), requestCode)
    }
}

fun Fragment.showToast(
    message: String
) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}