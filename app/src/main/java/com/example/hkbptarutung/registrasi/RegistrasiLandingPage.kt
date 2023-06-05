package com.example.hkbptarutung.registrasi

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hkbptarutung.home.HomeJemaat
import com.example.hkbptarutung.Profile
import com.example.hkbptarutung.R
import com.example.hkbptarutung.adapters.RegistrasiTipeAdapter
import com.example.hkbptarutung.databinding.ActivityRegistrasiLandingPageBinding
import com.example.hkbptarutung.home.HomeAdminNew
import com.example.hkbptarutung.utils.PreferenceUtils

class RegistrasiLandingPage : AppCompatActivity() {

    lateinit var binding: ActivityRegistrasiLandingPageBinding
    lateinit var adapter: RegistrasiTipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrasiLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setupRV()
        setupListener()
    }

    private fun setupListener() {
        binding.ivBtnBack.setOnClickListener { finish() }
        findViewById<View>(R.id.ll_home).setOnClickListener {
            finishAffinity()
            val target =
                if (PreferenceUtils.isAdmin(this)) HomeAdminNew::class.java else HomeJemaat::class.java
            startActivity(Intent(this, target))
        }

        findViewById<View>(R.id.ll_profile).setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }
    }

    private fun setupRV() {
        binding.rvRegistrasi.layoutManager = LinearLayoutManager(this)
        val list = resources.getStringArray(R.array.registrasi_kegiatan)
        adapter = RegistrasiTipeAdapter(list)
        binding.rvRegistrasi.adapter = adapter
    }
}