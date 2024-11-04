package com.example.worditory.game.board.tile

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

class TileSerializer: Serializer<TileModel> {
    override val defaultValue: TileModel
        get() = TileModel.newBuilder().build()

    override suspend fun readFrom(input: InputStream): TileModel {
        return TileModel.parseFrom(input)
    }

    override suspend fun writeTo(t: TileModel, output: OutputStream) {
        t.writeTo(output)
    }
}