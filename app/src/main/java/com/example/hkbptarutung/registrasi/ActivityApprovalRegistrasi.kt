package com.example.hkbptarutung.registrasi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hkbptarutung.R
import com.example.hkbptarutung.adapters.ApprovalRegistrasiAdapter
import com.example.hkbptarutung.adapters.ApprovalRegistrasiInterface
import com.example.hkbptarutung.databinding.ActivityApprovalRegistrasiBinding
import com.example.hkbptarutung.model.ApprovalItem
import com.example.hkbptarutung.utils.FirebaseUtils

class ActivityApprovalRegistrasi : AppCompatActivity(), ApprovalRegistrasiInterface {

    lateinit var binding: ActivityApprovalRegistrasiBinding
    lateinit var adapter: ApprovalRegistrasiAdapter

    private fun intentType() = intent?.extras?.getString("type")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApprovalRegistrasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()
        binding.ivBtnBack.setOnClickListener {
            finish()
        }
    }

    private fun getData() {
        val db = FirebaseUtils.getDbByType(this, intentType()) ?: return
        FirebaseUtils.db().collection(db).get()
            .addOnSuccessListener {
                val approvalItems = arrayListOf<ApprovalItem>()
                for (i in 0 until it.documents.size) {
                    approvalItems.add(ApprovalItem.fromDoc(it.documents[i]).apply {
                        type = " ~ Pengajuan ${intentType()}"
                    })
                }
                adapter = ApprovalRegistrasiAdapter(approvalItems, this)
                binding.rvApproval.layoutManager = LinearLayoutManager(this)
                binding.rvApproval.adapter = adapter
            }
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            getData()
        }
    }

    override fun onClickApprovalItem(item: ApprovalItem) {
        resultLauncher.launch(Intent(this, RegistrasiPage::class.java).apply {
            putExtra("title", convertType(item.type.replace(" ~ Pengajuan ", "")))
            putExtra("docId", item.documentId)
        })
    }

    private fun Context.convertType(from: String): String? {
        when (from) {
            getString(R.string.menu_baptis) -> {
                return getString(R.string.registrasi_baptis)
            }

            getString(R.string.menu_pindah_huria) -> {
                return getString(R.string.registrasi_pindah_huria)
            }

            getString(R.string.menu_nikah) -> {
                return getString(R.string.registrasi_pemberkatan_nikah)
            }

            getString(R.string.menu_martumpol) -> {
                return getString(R.string.registrasi_martupol)
            }

            getString(R.string.menu_sidi) -> {
                return getString(R.string.registrasi_sidi)
            }

            getString(R.string.menu_jemaat_baru) -> {
                return getString(R.string.registrasi_jemaat_baru)
            }
        }
        return null
    }

}