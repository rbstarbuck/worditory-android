package com.example.worditory.game.board

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

class BoardSerializer: Serializer<BoardModel> {
    override val defaultValue: BoardModel
        get() = BoardModel.newBuilder().build()

    override suspend fun readFrom(input: InputStream): BoardModel {
        return BoardModel.parseFrom(input)
    }

    override suspend fun writeTo(t: BoardModel, output: OutputStream) {
        t.writeTo(output)
    }
}