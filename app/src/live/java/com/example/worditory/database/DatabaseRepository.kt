package com.example.worditory.database

import com.google.firebase.Firebase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.database

internal object DatabaseRepository {
    val database = Firebase.database.reference

    internal fun getServerTime(onRetrieved: (Long) -> Unit) {
        database.child(DbKey.SERVER_TIME).setValue(ServerValue.TIMESTAMP).addOnSuccessListener {
            database.child(DbKey.SERVER_TIME).get().addOnSuccessListener { snapshot ->
                onRetrieved(snapshot.getValue(Long::class.java)!!)
            }
        }
    }
}