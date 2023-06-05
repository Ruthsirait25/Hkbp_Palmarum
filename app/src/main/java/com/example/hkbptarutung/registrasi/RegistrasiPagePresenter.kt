package com.example.hkbptarutung.registrasi

import com.example.hkbptarutung.utils.FirebaseUtils
import com.google.firebase.firestore.SetOptions

interface RegistrasiPageInterface {
    fun onSessionExpired()
    fun onSuccessRegister()
    fun onFailureRegister(msg: String)

    fun onSuccessProcess(approved: Boolean)
}

class RegistrasiPagePresenter(val presenter: RegistrasiPageInterface) {
    fun submitBaptis(
        fullName: String?,
        dadName: String?,
        momName: String?,
        birthPlace: String?,
        birthDate: String?,
        gender: String?,
        baptisDate: String?,
    ) {
        val uid = FirebaseUtils.user()?.uid
        if (uid == null) presenter.onSessionExpired()
        FirebaseUtils.db().collection(FirebaseUtils.dbBaptis).add(
            hashMapOf(
                "fullName" to fullName,
                "dad" to dadName,
                "mom" to momName,
                "birthPlace" to birthPlace,
                "birthDate" to birthDate,
                "gender" to gender,
                "baptisDate" to baptisDate,
                "requestor" to uid
            )
        ).addOnSuccessListener {
            presenter.onSuccessRegister()
        }.addOnFailureListener {
            presenter.onFailureRegister(it.message ?: "Terjadi kesalahan")
        }
    }

    fun approveBaptis(docId: String) {
        processDoc(FirebaseUtils.dbBaptis, docId, true)
    }

    fun approveSidi(docId: String) {
        processDoc(FirebaseUtils.dbSidi, docId, true)
    }

    fun approveMartumpol(docId: String) {
        processDoc(FirebaseUtils.dbMartupol, docId, true)
    }

    fun approveNikah(docId: String) {
        processDoc(FirebaseUtils.dbNikah, docId, true)
    }

    fun approveJemaat(docId: String) {
        processDoc(FirebaseUtils.dbJemaatBaru, docId, true)
    }

    fun approvePindah(docId: String) {
        processDoc(FirebaseUtils.dbPindahHuria, docId, true)
    }

    fun rejectBaptis(docId: String) {
        processDoc(FirebaseUtils.dbBaptis, docId, false)
    }

    fun rejectSidi(docId: String) {
        processDoc(FirebaseUtils.dbSidi, docId, false)
    }

    fun rejectMartumpol(docId: String) {
        processDoc(FirebaseUtils.dbMartupol, docId, false)
    }

    fun rejectNikah(docId: String) {
        processDoc(FirebaseUtils.dbNikah, docId, false)
    }

    fun rejectJemaat(docId: String) {
        processDoc(FirebaseUtils.dbJemaatBaru, docId, false)
    }

    fun rejectPindah(docId: String) {
        processDoc(FirebaseUtils.dbPindahHuria, docId, false)
    }

    fun processDoc(dbName: String, docId: String, value: Boolean) {
        FirebaseUtils.db().collection(dbName).document(docId).set(
            hashMapOf(
                "approved" to value
            ), SetOptions.merge()
        ).addOnSuccessListener {
            presenter.onSuccessProcess(value)
        }.addOnFailureListener {
            presenter.onFailureRegister(it.message ?: "Terjadi kesalahan")
        }
    }

    fun submitSidi(
        fullName: String?,
        dadName: String?,
        momName: String?,
        birthPlace: String?,
        birthDate: String?,
        gender: String?,
        baptisDate: String?, sidiDate: String?,
    ) {
        val uid = FirebaseUtils.user()?.uid
        if (uid == null) presenter.onSessionExpired()
        FirebaseUtils.db().collection(FirebaseUtils.dbSidi).add(
            hashMapOf(
                "fullName" to fullName,
                "dad" to dadName,
                "mom" to momName,
                "birthPlace" to birthPlace,
                "birthDate" to birthDate,
                "gender" to gender,
                "baptisDate" to baptisDate,
                "sidiDate" to sidiDate,
                "requestor" to uid
            )
        ).addOnSuccessListener {
            presenter.onSuccessRegister()
        }.addOnFailureListener {
            presenter.onFailureRegister(it.message ?: "Terjadi kesalahan")
        }
    }

    fun submitMartupol(
        boyName: String?,
        boyAddress: String?,
        boyChurch: String?,
        boyDad: String?,
        boyMom: String?,
        girlName: String?,
        girlAddress: String?,
        girlChurch: String?,
        girlDad: String?,
        girlMom: String?,
    ) {
        val uid = FirebaseUtils.user()?.uid
        if (uid == null) presenter.onSessionExpired()
        FirebaseUtils.db().collection(FirebaseUtils.dbMartupol).add(
            hashMapOf(
                "boyName" to boyName,
                "boyAddress" to boyAddress,
                "boyChurch" to boyChurch,
                "boyDad" to boyDad,
                "boyMom" to boyMom,
                "girlName" to girlName,
                "girlAddress" to girlAddress,
                "girlChurch" to girlChurch,
                "girlDad" to girlDad,
                "girlMom" to girlMom,
                "requestor" to uid
            )
        ).addOnSuccessListener {
            presenter.onSuccessRegister()
        }.addOnFailureListener {
            presenter.onFailureRegister(it.message ?: "Terjadi kesalahan")
        }
    }

    fun submitNikah(
        fullName: String?,
        birthPlace: String?,
        birthDate: String?,
        gender: String?,
        bloodType: String?,
        address: String?,
        phone: String?,
    ) {
        submitOther(
            fullName = fullName,
            birthPlace = birthPlace,
            birthDate = birthDate,
            gender = gender,
            bloodType = bloodType,
            address = address,
            phone = phone,
            originChurch = null,
            db = FirebaseUtils.dbNikah,
        )
    }

    fun submitJemaatBaru(
        fullName: String?,
        birthPlace: String?,
        birthDate: String?,
        gender: String?,
        bloodType: String?,
        address: String?,
        phone: String?,
    ) {
        submitOther(
            fullName = fullName,
            birthPlace = birthPlace,
            birthDate = birthDate,
            gender = gender,
            bloodType = bloodType,
            address = address,
            phone = phone,
            originChurch = null,
            db = FirebaseUtils.dbJemaatBaru,
        )
    }

    fun submitPindahHuria(
        fullName: String?,
        birthPlace: String?,
        birthDate: String?,
        gender: String?,
        bloodType: String?,
        address: String?,
        phone: String?,
        originChurch: String?
    ) {
        submitOther(
            fullName = fullName,
            birthPlace = birthPlace,
            birthDate = birthDate,
            gender = gender,
            bloodType = bloodType,
            address = address,
            phone = phone,
            originChurch = originChurch,
            db = FirebaseUtils.dbPindahHuria,
        )
    }

    private fun submitOther(
        fullName: String?,
        birthPlace: String?,
        birthDate: String?,
        gender: String?,
        bloodType: String?,
        address: String?,
        phone: String?,
        originChurch: String?,
        db: String
    ) {
        val uid = FirebaseUtils.user()?.uid
        if (uid == null) presenter.onSessionExpired()
        val data = hashMapOf(
            "fullName" to fullName,
            "bloodType" to bloodType,
            "address" to address,
            "birthPlace" to birthPlace,
            "birthDate" to birthDate,
            "gender" to gender,
            "phone" to phone,
            "requestor" to uid
        )
        if (!originChurch.isNullOrEmpty()) data["originChurch"] = originChurch
        FirebaseUtils.db().collection(db).add(data).addOnSuccessListener {
            presenter.onSuccessRegister()
        }.addOnFailureListener {
            presenter.onFailureRegister(it.message ?: "Terjadi kesalahan")
        }
    }
}