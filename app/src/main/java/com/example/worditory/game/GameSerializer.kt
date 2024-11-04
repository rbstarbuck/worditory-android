package com.example.worditory.game

import androidx.datastore.core.Serializer
import com.example.worditory.game.board.GameModel
import java.io.InputStream
import java.io.OutputStream

class GameSerializer: Serializer<GameModel> {
    override val defaultValue: GameModel
        get() = GameModel.newBuilder().build()

    override suspend fun readFrom(input: InputStream): GameModel {
        return GameModel.parseFrom(input)
    }

    override suspend fun writeTo(t: GameModel, output: OutputStream) {
        t.writeTo(output)
    }

}