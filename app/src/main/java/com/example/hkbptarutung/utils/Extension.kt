package com.example.hkbptarutung.utils

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hkbptarutung.LoginPage

fun EditText.value() = this.text.toString()

fun Context.showToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun Context.showAlert(title: String = "", msg: String, onYes: () -> Unit) {
    AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(msg)
        setPositiveButton("Ya") { _, _ ->
            onYes.invoke()
        }
    }.create().show()
}

fun AppCompatActivity.sessionExpired() {
    showToast("sesi anda telah habis")
    startActivity(Intent(this, LoginPage::class.java))
    finishAffinity()
}

fun View.visibleGone(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}