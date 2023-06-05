package com.example.hkbptarutung

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.example.hkbptarutung.databinding.ActivityLupaPasswordBinding

class LupaPassword : AppCompatActivity() {
    lateinit var binding: ActivityLupaPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLupaPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.lupaPassword)
        binding.btnForgetPass.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.lupaPassword))
                setMessage("Password akun anda berhasil diubah, Silahkan login")
                setPositiveButton("Ya"){_, _ ->
                    onBackPressed()
                }
            }.create().show()
        }
        binding.LLForgetPass.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
    }
}