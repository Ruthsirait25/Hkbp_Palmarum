package com.example.hkbptarutung.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hkbptarutung.AcaraMinggu
import com.example.hkbptarutung.HistoryPage
import com.example.hkbptarutung.LoginPage
import com.example.hkbptarutung.WartaJemaat
import com.example.hkbptarutung.databinding.ActivityHomeTamuBinding

class HomeTamu : AppCompatActivity() {
    lateinit var binding: ActivityHomeTamuBinding
    private var pressedTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeTamuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.cvHistory.setOnClickListener {
            startActivity(Intent(this, HistoryPage::class.java))
        }

        binding.cvAcaraMinggu.setOnClickListener {
            startActivity(Intent(this, AcaraMinggu::class.java))
        }

        binding.cvWartaJemaat.setOnClickListener {
            startActivity(Intent(this, WartaJemaat::class.java))
        }

        binding.cvLogin.setOnClickListener {
            startActivity(Intent(this, LoginPage::class.java))
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        if (pressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed()
        } else {
            Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        }
        pressedTime = System.currentTimeMillis()
    }
}