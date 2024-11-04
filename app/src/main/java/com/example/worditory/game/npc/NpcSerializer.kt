package com.example.worditory.game.npc

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

class NpcSerializer: Serializer<NpcModel> {
    override val defaultValue: NpcModel
        get() = NpcModel.newBuilder().build()

    override suspend fun readFrom(input: InputStream): NpcModel {
        return NpcModel.parseFrom(input)
    }

    override suspend fun writeTo(t: NpcModel, output: OutputStream) {
        t.writeTo(output)
    }
}