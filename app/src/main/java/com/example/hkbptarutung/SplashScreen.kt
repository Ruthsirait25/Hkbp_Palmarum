package com.example.hkbptarutung

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.hkbptarutung.home.HomeAdminNew
import com.example.hkbptarutung.home.HomeJemaat
import com.example.hkbptarutung.utils.FirebaseUtils
import com.example.hkbptarutung.utils.showAlert
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashScreen : AppCompatActivity() {

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            getData()
        } else {
            showAlert(msg = "Anda tidak akan mendapatkan notifikasi") {
                getData()
            }
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                getData()
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                showAlert(msg = "Notifikasi diperlukan untuk mendapatkan notifikasi admin") {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            getData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        supportActionBar?.hide()

        askNotificationPermission()
    }

    fun getData() {
        val user = Firebase.auth.currentUser
        if (user == null) {
            startActivity(Intent(this, LoginPage::class.java))
        } else {
            FirebaseUtils.db().collection(FirebaseUtils.dbUser).document(user.uid)
                .get().addOnSuccessListener {
                    if (it["isAdmin"] == true) {
                        startActivity(Intent(this, HomeAdminNew::class.java))
                    } else {
                        startActivity(Intent(this, HomeJemaat::class.java))
                    }
                    finish()
                }
        }
    }
}