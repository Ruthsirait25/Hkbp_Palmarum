package com.example.hkbptarutung.utils

import android.content.Context
import com.example.hkbptarutung.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseUtils {
    companion object {
        fun db() = Firebase.firestore

        fun user() = Firebase.auth.currentUser

        const val dbUser = "users"
        const val dbBaptis = "baptis"
        const val dbSidi = "sidi"
        const val dbMartupol = "martupol"
        const val dbNikah = "nikah"
        const val dbJemaatBaru = "jemaatBaru"
        const val dbPindahHuria = "pindahHuria"

        fun getDbByType(context: Context, type: String?): String? {
            val src = context.resources
            when {
                type == src.getString(R.string.registrasi_baptis) -> {
                    return dbBaptis
                }

                type == src.getString(R.string.menu_baptis) -> {
                    return dbBaptis
                }

                type == src.getString(R.string.registrasi_sidi) -> {
                    return dbSidi
                }

                type == src.getString(R.string.menu_sidi) -> {
                    return dbSidi
                }

                type == src.getString(R.string.registrasi_martupol) -> {
                    return dbMartupol
                }

                type == src.getString(R.string.menu_martumpol) -> {
                    return dbMartupol
                }

                type == src.getString(R.string.registrasi_pemberkatan_nikah) -> {
                    return dbNikah
                }

                type == src.getString(R.string.menu_nikah) -> {
                    return dbNikah
                }

                type == src.getString(R.string.registrasi_jemaat_baru) -> {
                    return dbJemaatBaru
                }

                type == src.getString(R.string.menu_jemaat_baru) -> {
                    return dbJemaatBaru
                }

                type == src.getString(R.string.registrasi_pindah_huria) -> {
                    return dbPindahHuria
                }

                type == src.getString(R.string.menu_pindah_huria) -> {
                    return dbPindahHuria
                }
            }
            return null
        }

        fun setToken(token: String) {
            val uid = user()?.uid ?: return
            db().collection(dbUser).document(uid).set(
                hashMapOf("fcm" to token), SetOptions.merge()
            )
        }
    }
}