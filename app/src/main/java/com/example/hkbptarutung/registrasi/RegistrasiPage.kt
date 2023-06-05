package com.example.hkbptarutung.registrasi

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.hkbptarutung.home.HomeJemaat
import com.example.hkbptarutung.Profile
import com.example.hkbptarutung.R
import com.example.hkbptarutung.utils.FirebaseUtils
import com.example.hkbptarutung.utils.PreferenceUtils
import com.example.hkbptarutung.utils.sendSimpleNotif
import com.example.hkbptarutung.utils.sessionExpired
import com.example.hkbptarutung.utils.showAlert
import com.example.hkbptarutung.utils.showToast
import com.example.hkbptarutung.utils.value
import com.example.hkbptarutung.utils.visibleGone

class RegistrasiPage : AppCompatActivity(), RegistrasiPageInterface {

    var layout = R.layout.activity_registrasi_calon_mempelai
    var hideView = -1
    var presenter = RegistrasiPagePresenter(this)
    var uidRequestor = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        setContentView(layout)
        initAdmin()
        supportActionBar?.hide()
        findViewById<TextView>(R.id.tv_registrasi_title).text = titleString()
        setupAdapter()
        setupListener()
        hideView()
    }

    private fun hideView() =
        if (hideView == -1) println("oke") else findViewById<View>(hideView).visibility = View.GONE

    private fun titleString(): String = intent?.extras?.getString("title") ?: "Registrasi"
    private fun docId() = intent?.extras?.getString("docId")

    private fun initLayout() {
        when (titleString()) {
            getString(R.string.registrasi_baptis) -> {
                layout = R.layout.activity_registrasi_page
                hideView = R.id.ll_tanggal_sidi
            }

            getString(R.string.registrasi_sidi) -> {
                layout = R.layout.activity_registrasi_page
            }

            getString(R.string.registrasi_martupol) -> {
                layout = R.layout.activity_registrasi_calon_mempelai
                hideView = R.id.ll_jekel
            }

            getString(R.string.registrasi_pemberkatan_nikah) -> {
                layout = R.layout.activity_registrasi_other
                hideView = R.id.ll_gereja
            }

            getString(R.string.registrasi_jemaat_baru) -> {
                layout = R.layout.activity_registrasi_other
                hideView = R.id.ll_gereja
            }

            getString(R.string.registrasi_pindah_huria) -> {
                layout = R.layout.activity_registrasi_other
            }
        }
    }

    private fun setupListener() {
        findViewById<View>(R.id.btn_lanjut).setOnClickListener {
            submit()
        }

        findViewById<View>(R.id.btn_batal).setOnClickListener {
            if (PreferenceUtils.isAdmin(this)) {
                reject()
                return@setOnClickListener
            }
            finish()
        }
        findViewById<View>(R.id.iv_btn_back).setOnClickListener {

            finish()
        }
        findViewById<View>(R.id.ll_home).setOnClickListener {
            finishAffinity()
            startActivity(Intent(this, HomeJemaat::class.java))
        }

        findViewById<View>(R.id.ll_profile).setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }

        findViewById<View>(R.id.iv_btn_delete).setOnClickListener {
            delete()
        }
    }

    private fun delete() {
        val dbName = FirebaseUtils.getDbByType(this, titleString()) ?: return
        val docId = docId() ?: return
        FirebaseUtils.db().collection(dbName).document(docId).delete().addOnSuccessListener {
            showAlert(msg = "Anda berhasil hapus pengajuan") {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private fun setupAdapter() {
        ArrayAdapter.createFromResource(
            this, R.array.jenisKelamin, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            if (layout != R.string.registrasi_martupol) {
                findViewById<Spinner>(R.id.sp_jekel).adapter = adapter
            }
        }
    }

    private fun initAdmin() {
        val isAdmin = PreferenceUtils.isAdmin(this)
        findViewById<ImageView>(R.id.iv_btn_delete).visibleGone(isAdmin)
        if (isAdmin) {
            findViewById<Button>(R.id.btn_lanjut).text = getString(R.string.setuju)
            findViewById<Button>(R.id.btn_batal).text = getString(R.string.tolak)
            findViewById<Button>(R.id.btn_batal).setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.merah
                )
            )
            val dbName = FirebaseUtils.getDbByType(this, titleString()) ?: return
            val docId = docId() ?: return
            FirebaseUtils.db().collection(dbName).document(docId).get().addOnSuccessListener {
                uidRequestor = "${it["requestor"]}"
                when (dbName) {
                    FirebaseUtils.dbBaptis -> {
                        findViewById<EditText>(R.id.edt_nama_Lengkap).setText("${it["fullName"]}")
                        findViewById<EditText>(R.id.edt_nama_ayah).setText("${it["dad"]}")
                        findViewById<EditText>(R.id.edt_nama_ibu).setText("${it["mom"]}")
                        findViewById<EditText>(R.id.edt_Tempat_Lahir).setText("${it["birthPlace"]}")
                        findViewById<EditText>(R.id.edt_tanggal_lahir).setText("${it["birthDate"]}")
                        findViewById<EditText>(R.id.edt_tanggal_baptis).setText("${it["baptisDate"]}")
                        val gender = if (it["gender"] == getString(R.string.female)) 1 else 0
                        findViewById<Spinner>(R.id.sp_jekel).setSelection(gender)
                    }

                    FirebaseUtils.dbSidi -> {
                        findViewById<EditText>(R.id.edt_nama_Lengkap).setText("${it["fullName"]}")
                        findViewById<EditText>(R.id.edt_nama_ayah).setText("${it["dad"]}")
                        findViewById<EditText>(R.id.edt_nama_ibu).setText("${it["mom"]}")
                        findViewById<EditText>(R.id.edt_Tempat_Lahir).setText("${it["birthPlace"]}")
                        findViewById<EditText>(R.id.edt_tanggal_lahir).setText("${it["birthDate"]}")
                        findViewById<EditText>(R.id.edt_tanggal_baptis).setText("${it["baptisDate"]}")
                        findViewById<EditText>(R.id.edt_tanggal_sidi).setText("${it["sidiDate"]}")
                        val gender = if (it["gender"] == getString(R.string.female)) 1 else 0
                        findViewById<Spinner>(R.id.sp_jekel).setSelection(gender)
                    }

                    FirebaseUtils.dbMartupol -> {
                        findViewById<EditText>(R.id.edt_nama_lakilaki).setText("${it["boyName"]}")
                        findViewById<EditText>(R.id.edt_alamat).setText("${it["boyAddress"]}")
                        findViewById<EditText>(R.id.edt_gereja).setText("${it["boyChurch"]}")
                        findViewById<EditText>(R.id.edt_nama_ayah).setText("${it["boyDad"]}")
                        findViewById<EditText>(R.id.edt_nama_ibu).setText("${it["boyMom"]}")
                        findViewById<EditText>(R.id.edt_nama_perempuan).setText("${it["girlName"]}")
                        findViewById<EditText>(R.id.edt_alamat_perempuan).setText("${it["girlAddress"]}")
                        findViewById<EditText>(R.id.edt_gereja_perempuan).setText("${it["girlChurch"]}")
                        findViewById<EditText>(R.id.edt_nama_ayah_perempuan).setText("${it["girlDad"]}")
                        findViewById<EditText>(R.id.edt_nama_ibu_perempuan).setText("${it["girlMom"]}")
                    }

                    FirebaseUtils.dbNikah -> {
                        findViewById<EditText>(R.id.edt_nama_jemaat).setText("${it["fullName"]}")
                        findViewById<EditText>(R.id.edt_tanggal_lahir).setText("${it["birthDate"]}")
                        findViewById<EditText>(R.id.edt_tempat_lahir).setText("${it["birthPlace"]}")
                        val gender = if (it["gender"] == getString(R.string.female)) 1 else 0
                        findViewById<Spinner>(R.id.sp_jekel).setSelection(gender)
                        findViewById<EditText>(R.id.edt_goldar).setText("${it["bloodType"]}")
                        findViewById<EditText>(R.id.edt_alamat).setText("${it["address"]}")
                        findViewById<EditText>(R.id.edt_nomorHP).setText("${it["phone"]}")
                    }

                    FirebaseUtils.dbJemaatBaru -> {
                        findViewById<EditText>(R.id.edt_nama_jemaat).setText("${it["fullName"]}")
                        findViewById<EditText>(R.id.edt_tanggal_lahir).setText("${it["birthDate"]}")
                        findViewById<EditText>(R.id.edt_tempat_lahir).setText("${it["birthPlace"]}")
                        val gender = if (it["gender"] == getString(R.string.female)) 1 else 0
                        findViewById<Spinner>(R.id.sp_jekel).setSelection(gender)
                        findViewById<EditText>(R.id.edt_goldar).setText("${it["bloodType"]}")
                        findViewById<EditText>(R.id.edt_alamat).setText("${it["address"]}")
                        findViewById<EditText>(R.id.edt_nomorHP).setText("${it["phone"]}")
                    }

                    FirebaseUtils.dbPindahHuria -> {
                        findViewById<EditText>(R.id.edt_nama_jemaat).setText("${it["fullName"]}")
                        findViewById<EditText>(R.id.edt_tanggal_lahir).setText("${it["birthDate"]}")
                        findViewById<EditText>(R.id.edt_tempat_lahir).setText("${it["birthPlace"]}")
                        val gender = if (it["gender"] == getString(R.string.female)) 1 else 0
                        findViewById<Spinner>(R.id.sp_jekel).setSelection(gender)
                        findViewById<EditText>(R.id.edt_goldar).setText("${it["bloodType"]}")
                        findViewById<EditText>(R.id.edt_alamat).setText("${it["address"]}")
                        findViewById<EditText>(R.id.edt_nomorHP).setText("${it["phone"]}")
                        findViewById<EditText>(R.id.edt_gereja).setText("${it["originChurch"]}")
                    }
                }
            }
        }
    }

    private fun submit() {
        when (titleString()) {
            getString(R.string.registrasi_baptis) -> {
                if (PreferenceUtils.isAdmin(this)) {
                    docId()?.apply {
                        presenter.approveBaptis(this)
                    }
                    return
                }
                presenter.submitBaptis(
                    fullName = findViewById<EditText>(R.id.edt_nama_Lengkap)?.value(),
                    dadName = findViewById<EditText>(R.id.edt_nama_ayah)?.value(),
                    momName = findViewById<EditText>(R.id.edt_nama_ibu)?.value(),
                    birthPlace = findViewById<EditText>(R.id.edt_Tempat_Lahir)?.value(),
                    birthDate = findViewById<EditText>(R.id.edt_tanggal_lahir)?.value(),
                    gender = findViewById<Spinner>(R.id.sp_jekel).selectedItem.toString(),
                    baptisDate = findViewById<EditText>(R.id.edt_tanggal_baptis).value(),
                )
            }

            getString(R.string.registrasi_sidi) -> {
                if (PreferenceUtils.isAdmin(this)) {
                    docId()?.apply {
                        presenter.approveSidi(this)
                    }
                    return
                }
                presenter.submitSidi(
                    fullName = findViewById<EditText>(R.id.edt_nama_Lengkap)?.value(),
                    dadName = findViewById<EditText>(R.id.edt_nama_ayah)?.value(),
                    momName = findViewById<EditText>(R.id.edt_nama_ibu)?.value(),
                    birthPlace = findViewById<EditText>(R.id.edt_Tempat_Lahir)?.value(),
                    birthDate = findViewById<EditText>(R.id.edt_tanggal_lahir)?.value(),
                    gender = findViewById<Spinner>(R.id.sp_jekel).selectedItem.toString(),
                    baptisDate = findViewById<EditText>(R.id.edt_tanggal_baptis).value(),
                    sidiDate = findViewById<EditText>(R.id.edt_tanggal_sidi).value(),
                )
            }

            getString(R.string.registrasi_martupol) -> {
                if (PreferenceUtils.isAdmin(this)) {
                    docId()?.apply {
                        presenter.approveMartumpol(this)
                    }
                    return
                }
                presenter.submitMartupol(
                    boyName = findViewById<EditText>(R.id.edt_nama_lakilaki).value(),
                    boyAddress = findViewById<EditText>(R.id.edt_alamat).value(),
                    boyChurch = findViewById<EditText>(R.id.edt_gereja).value(),
                    boyDad = findViewById<EditText>(R.id.edt_nama_ayah).value(),
                    boyMom = findViewById<EditText>(R.id.edt_nama_ibu).value(),
                    girlName = findViewById<EditText>(R.id.edt_nama_perempuan).value(),
                    girlAddress = findViewById<EditText>(R.id.edt_alamat_perempuan).value(),
                    girlChurch = findViewById<EditText>(R.id.edt_gereja_perempuan).value(),
                    girlDad = findViewById<EditText>(R.id.edt_nama_ayah_perempuan).value(),
                    girlMom = findViewById<EditText>(R.id.edt_nama_ibu_perempuan).value(),
                )
            }

            getString(R.string.registrasi_pemberkatan_nikah) -> {
                if (PreferenceUtils.isAdmin(this)) {
                    docId()?.apply {
                        presenter.approveNikah(this)
                    }
                    return
                }
                presenter.submitNikah(
                    fullName = findViewById<EditText>(R.id.edt_nama_jemaat).value(),
                    birthDate = findViewById<EditText>(R.id.edt_tanggal_lahir).value(),
                    birthPlace = findViewById<EditText>(R.id.edt_tempat_lahir).value(),
                    gender = findViewById<Spinner>(R.id.sp_jekel).selectedItem.toString(),
                    bloodType = findViewById<EditText>(R.id.edt_goldar).value(),
                    address = findViewById<EditText>(R.id.edt_alamat).value(),
                    phone = findViewById<EditText>(R.id.edt_nomorHP).value(),
                )
            }

            getString(R.string.registrasi_jemaat_baru) -> {
                if (PreferenceUtils.isAdmin(this)) {
                    docId()?.apply {
                        presenter.approveJemaat(this)
                    }
                    return
                }
                presenter.submitJemaatBaru(
                    fullName = findViewById<EditText>(R.id.edt_nama_jemaat).value(),
                    birthDate = findViewById<EditText>(R.id.edt_tanggal_lahir).value(),
                    birthPlace = findViewById<EditText>(R.id.edt_tempat_lahir).value(),
                    gender = findViewById<Spinner>(R.id.sp_jekel).selectedItem.toString(),
                    bloodType = findViewById<EditText>(R.id.edt_goldar).value(),
                    address = findViewById<EditText>(R.id.edt_alamat).value(),
                    phone = findViewById<EditText>(R.id.edt_nomorHP).value(),
                )
            }

            getString(R.string.registrasi_pindah_huria) -> {
                if (PreferenceUtils.isAdmin(this)) {
                    docId()?.apply {
                        presenter.approvePindah(this)
                    }
                    return
                }
                presenter.submitPindahHuria(
                    fullName = findViewById<EditText>(R.id.edt_nama_jemaat).value(),
                    birthDate = findViewById<EditText>(R.id.edt_tanggal_lahir).value(),
                    birthPlace = findViewById<EditText>(R.id.edt_tempat_lahir).value(),
                    gender = findViewById<Spinner>(R.id.sp_jekel).selectedItem.toString(),
                    bloodType = findViewById<EditText>(R.id.edt_goldar).value(),
                    address = findViewById<EditText>(R.id.edt_alamat).value(),
                    phone = findViewById<EditText>(R.id.edt_nomorHP).value(),
                    originChurch = findViewById<EditText>(R.id.edt_gereja).value(),
                )
            }
        }
    }

    private fun reject() {
        when (titleString()) {
            getString(R.string.registrasi_baptis) -> {
                if (PreferenceUtils.isAdmin(this)) {
                    docId()?.apply {
                        presenter.rejectBaptis(this)
                    }
                }
            }

            getString(R.string.registrasi_sidi) -> {
                if (PreferenceUtils.isAdmin(this)) {
                    docId()?.apply {
                        presenter.rejectSidi(this)
                    }
                    return
                }
            }

            getString(R.string.registrasi_martupol) -> {
                if (PreferenceUtils.isAdmin(this)) {
                    docId()?.apply {
                        presenter.rejectMartumpol(this)
                    }
                    return
                }
            }

            getString(R.string.registrasi_pemberkatan_nikah) -> {
                if (PreferenceUtils.isAdmin(this)) {
                    docId()?.apply {
                        presenter.rejectNikah(this)
                    }
                    return
                }
            }

            getString(R.string.registrasi_jemaat_baru) -> {
                if (PreferenceUtils.isAdmin(this)) {
                    docId()?.apply {
                        presenter.rejectJemaat(this)
                    }
                    return
                }
            }

            getString(R.string.registrasi_pindah_huria) -> {
                if (PreferenceUtils.isAdmin(this)) {
                    docId()?.apply {
                        presenter.rejectPindah(this)
                    }
                    return
                }
            }
        }
    }

    override fun onSessionExpired() {
        sessionExpired()
    }

    override fun onSuccessRegister() {
        val token = "/topics/admin"
        sendSimpleNotif(token, "anda memiliki pengajuan baru")
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.registrasiKegiatan))
            setMessage("Registrasi kegiatan sudah ditambahkan, silahkan tunggu")
            setPositiveButton("Ya") { _, _ ->
                finish()
            }
        }.create().show()
    }

    private fun notify(approved: Boolean) {
        FirebaseUtils.db().collection(FirebaseUtils.dbUser).document(uidRequestor).get()
            .addOnSuccessListener {
                val token = "${it["fcm"]}"
                val action = if (approved) "setujui" else "tolak"
                val msg = "${titleString()} anda di$action"
                sendSimpleNotif(token, msg)
            }
    }

    override fun onSuccessProcess(approved: Boolean) {
        val msg = if (approved) "Berhasil approve permintaan" else "Berhasil tolak permintaan"
        notify(approved)
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.registrasiKegiatan))
            setMessage(msg)
            setPositiveButton("Ya") { _, _ ->
                setResult(Activity.RESULT_OK)
                finish()
            }
        }.create().show()
    }

    override fun onFailureRegister(msg: String) {
        showToast(msg)
    }
}