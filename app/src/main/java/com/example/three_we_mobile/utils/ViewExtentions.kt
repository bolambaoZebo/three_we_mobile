package com.example.three_we_mobile.utils

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Context.showSnackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_LONG,
    view: View
) {
    Snackbar.make(view, message, duration).show()
}