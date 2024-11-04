package com.example.worditory.game.board

import com.example.worditory.game.board.tile.TileModel

class Board private constructor() {
    companion object {
        fun newModel(width: Int, height: Int): BoardModel {
            val letterBag = LetterBag()
            val modelBuilder = BoardModel.newBuilder()
                .setWidth(width)
                .setHeight(height)

            for (y in 0..<height) {
                val ownership = if (y == 0) {
                    TileModel.Ownership.OWNED_PLAYER_2
                } else if (y == height - 1) {
                    TileModel.Ownership.OWNED_PLAYER_1
                } else {
                    TileModel.Ownership.UNOWNED
                }

                for (x in 0..<width) {
                    val tile = TileModel.newBuilder()
                        .setLetter(letterBag.takeLetter())
                        .setOwnership(ownership)
                        .build()
                    modelBuilder.addTiles(tile)
                }
            }

            return modelBuilder.build()
        }
    }
}