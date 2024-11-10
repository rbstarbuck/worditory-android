package com.example.worditory.game.npc

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import java.io.InputStream
import java.io.OutputStream

class BeatenOpponentsSerializer: Serializer<BeatenOpponents> {
    override val defaultValue: BeatenOpponents
        get() = BeatenOpponents.newBuilder().build()

    override suspend fun readFrom(input: InputStream): BeatenOpponents {
        return BeatenOpponents.parseFrom(input)
    }

    override suspend fun writeTo(t: BeatenOpponents, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.beatenOpponentsDataStore: DataStore<BeatenOpponents> by dataStore(
    fileName = "beaten-opponents.pb",
    serializer = BeatenOpponentsSerializer()
)

suspend fun Context.addBeatenOpponent(opponent: NpcModel.Spec) {
    beatenOpponentsDataStore.updateData { beatenOpponents ->
        BeatenOpponents.newBuilder()
            .addOpponents(opponent)
            .addAllOpponents(beatenOpponents.opponentsList.filter { it != opponent })
            .build()
    }
}
