package com.example.worditory.user

import android.content.Context
import com.example.worditory.database.DbKey
import com.example.worditory.setPlayerAvatarId
import com.example.worditory.setPlayerDisplayName
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal object UserRepository {
    private val database = Firebase.database.reference
    private val auth = Firebase.auth

    internal fun saveCurrentUser(avatarId: Int) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val model = UserRepoModel(
                email = currentUser.email,
                displayName = currentUser.displayName,
                avatarId = avatarId
            )

            database.child(DbKey.USERS).child(currentUser.uid).setValue(model)
        }
    }

    internal fun updateCurrentUserAvatarId(avatarId: Int) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database
                .child(DbKey.USERS)
                .child(currentUser.uid)
                .child(DbKey.Users.AVATAR_ID)
                .setValue(avatarId)
        }
    }

    internal fun restoreUserParameters(scope: CoroutineScope, context: Context) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database
                .child(DbKey.USERS)
                .child(currentUser.uid)
                .get()
                .addOnSuccessListener { result ->
                    val user = result.getValue(UserRepoModel::class.java)

                    if (user != null) {
                        scope.launch {
                            context.setPlayerDisplayName(user.displayName ?: "")
                            context.setPlayerAvatarId(user.avatarId ?: 0)
                        }
                    }
                }
        }
    }
}