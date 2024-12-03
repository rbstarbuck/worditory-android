package com.example.worditory.friends

import com.example.worditory.database.DbKey
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.database

internal object FriendRepository {
    private val database = Firebase.database.reference
    private val auth = Firebase.auth

    internal fun sendFriendRequestFromGame(gameId: String) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database.child(DbKey.GAMES).child(gameId).get().addOnSuccessListener { snapshot ->
                val player1Uid = snapshot.child(DbKey.Games.PLAYER_1).getValue(String::class.java)!!
                val player2Uid = snapshot.child(DbKey.Games.PLAYER_2).getValue(String::class.java)!!

                val opponentUid = when (currentUser.uid) {
                    player1Uid -> player2Uid
                    player2Uid -> player1Uid
                    else -> throw IllegalArgumentException("User is not in game")
                }

                sendFriendRequest(opponentUid)
            }
        }
    }

    internal fun sendFriendRequestFromEmail(
        email: String,
        onError: (OnFailure) -> Unit
    ) {
        database.child(DbKey.EMAIL_TO_UID).child(email).get().addOnSuccessListener { snapshot ->
            val uid = snapshot.getValue(String::class.java)

            if (uid == null) {
                onError(OnFailure(OnFailure.Reason.EMAIL_NOT_REGISTERED))
            } else {
                sendFriendRequest(uid)
            }
        }
    }

    private fun sendFriendRequest(uid: String) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database.child(DbKey.FRIEND_REQUESTS).child(uid).child(currentUser.uid).setValue(true)
        }
    }

    internal fun acceptFriendRequest(uid: String) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database
                .child(DbKey.FRIENDS)
                .child(currentUser.uid)
                .child(uid)
                .setValue(ServerValue.TIMESTAMP)

            database
                .child(DbKey.FRIENDS)
                .child(uid)
                .child(currentUser.uid)
                .setValue(ServerValue.TIMESTAMP)

            deleteFriendRequest(uid)
        }
    }

    internal fun deleteFriendRequest(uid: String) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database.child(DbKey.FRIEND_REQUESTS).child(currentUser.uid).child(uid).removeValue()
        }
    }

    internal class OnFailure(
        internal val reason: Reason,
        internal val error: DatabaseError? = null
    ) {
        enum class Reason {
            EMAIL_NOT_REGISTERED,
            CANCELLED
        }
    }
}