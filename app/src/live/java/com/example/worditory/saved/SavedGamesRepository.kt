package com.example.worditory.saved

import android.content.Context
import com.example.worditory.database.DbKey
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal object SavedGamesRepository {
    private val database = Firebase.database.reference
    private val auth = Firebase.auth

    internal fun restoreLocalDataFromServers(scope: CoroutineScope, context: Context) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database
                .child(DbKey.PLAYER_GAMES)
                .child(userId)
                .addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val listener = this

                        scope.launch {
                            val liveGameIds =
                                snapshot.children.map { it.getValue(String::class.java)!! }.toSet()

                            context.savedLiveGamesDataStore.data.collect { savedGames ->
                                for (liveGame in savedGames.gamesList) {
                                    if (!liveGameIds.contains(liveGame.game.id)) {
                                        context.removeSavedLiveGame(liveGame.game.id)
                                    }
                                }
                            }

                            database
                                .child(DbKey.PLAYER_GAMES)
                                .child(userId)
                                .removeEventListener(listener)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })



        }
    }
}