package com.codekotliners.memify.features.templates.data.datasource

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class FirebaseDatasource {
    val db = Firebase.firestore

    fun getTemplates() {
        db.collection("templates").get().addOnSuccessListener { result ->
            for (document in result) {
                Log.d("Data", "${document.id} => ${document.data}")
            }
        }
    }
}
