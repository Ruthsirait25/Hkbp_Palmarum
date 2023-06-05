package com.example.hkbptarutung

import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.hkbptarutung.databinding.ActivityProfileBinding
import com.example.hkbptarutung.utils.FirebaseUtils
import com.example.hkbptarutung.utils.showAlert
import com.example.hkbptarutung.utils.value
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase

class Profile : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.profile)
        getData()
        binding.btnKembali.setOnClickListener {
            finish()
        }
        binding.btnUbah.setOnClickListener {
            update()
        }
        ArrayAdapter.createFromResource(
            this, R.array.jenisKelamin, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spJekel.adapter = adapter
        }
    }

    private fun update() {
        val user = Firebase.auth.currentUser
        val uid = user?.uid ?: return
        binding.apply {
            FirebaseUtils.db().collection(FirebaseUtils.dbUser).document(uid).set(
                hashMapOf(
                    "fullName" to this.etNamaLengkap.value(),
                    "username" to this.etUsername.value(),
                    "wijk" to this.etWijk.value(),
                    "phone" to this.etNomorHP.value(),
                    "address" to this.etAlamat.value(),
                    "gender" to spJekel.selectedItem
                ), SetOptions.merge()
            ).addOnSuccessListener {
                showAlert(msg = "Berhasil update profile") {
                    finish()
                }
            }
        }
    }

    private fun getData() {
        val user = Firebase.auth.currentUser
        val uid = user?.uid ?: return
        FirebaseUtils.db().collection(FirebaseUtils.dbUser).document(uid).get()
            .addOnSuccessListener {
                binding.apply {
                    etNamaLengkap.setText("${it["fullName"]}")
                    etUsername.setText("${it["username"]}")
                    etEmail.setText("${user.email}")
                    etWijk.setText("${it["wijk"]}")
                    etNomorHP.setText("${it["phone"]}")
                    etAlamat.setText("${it["address"]}")
                    val gender = if (it["gender"] == getString(R.string.female)) 1 else 0
                    spJekel.setSelection(gender)
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}