package com.example.hkbptarutung.model

import com.google.firebase.firestore.DocumentSnapshot

data class ApprovalItem(
    var requestorId: String = "",
    var documentId: String = "",
    var person: String = "Riana Sianturi",
    var approved: Boolean?,
    var type: String = "Pengajuan Administrasi Baptis"
) {
    companion object {
        fun fromDoc(doc: DocumentSnapshot): ApprovalItem {
            return ApprovalItem(
                requestorId = "${doc["requestor"]}",
                documentId = doc.id,
                person = "",
                type = "",
                approved = doc["approved"] as Boolean?
            )
        }
    }
}