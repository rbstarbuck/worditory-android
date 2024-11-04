package com.example.worditory.game.board

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import com.example.worditory.game.Game
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.board.tile.TileModel
import com.example.worditory.game.board.tile.TileViewModel
import com.example.worditory.game.board.word.WordViewModel
import com.example.worditory.game.dict.WordDictionary
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.pow
import kotlin.math.sqrt

class BoardViewModel(
    model: BoardModel,
    val isPlayerTurnStateFlow: StateFlow<Boolean>,
    val colorScheme: Tile.ColorScheme,
    onWordChanged: () -> Unit
): ViewModel() {
    val width = model.width
    val height = model.height
    val tiles: List<TileViewModel> = model.tilesList.map { TileViewModel(it, colorScheme) }
    val word = WordViewModel(width, height, onWordChanged)
    val letterBag = LetterBag()

    var currentDragPoint = Offset.Zero

    val model: BoardModel
        get() = BoardModel.newBuilder()
            .setWidth(width)
            .setHeight(height)
            .addAllTiles(tiles.map { it.model })
            .build()

    init {
        tiles.forEach { tile ->
            letterBag.removeLetter(tile.letter)
        }
    }

    fun getScore(): Game.Score {
        var scorePlayer1 = 0
        var scorePlayer2 = 0

        for (tile in tiles) {
            if (tile.isOwnedBy(Game.Player.PLAYER_1)) {
                ++scorePlayer1
            } else if (tile.isOwnedBy(Game.Player.PLAYER_2)) {
                ++scorePlayer2
            }
        }

        return Game.Score(scorePlayer1, scorePlayer2)
    }

    fun onDragStart(startPoint: Offset) {
        currentDragPoint = startPoint
    }

    private val boardWidthFloat = width.toFloat()
    private val boardHeightFloat = height.toFloat()
    private var didDragIntoSecondTile = true

    fun onDrag(offset: Offset, viewSize: IntSize) {
        currentDragPoint += offset

        val viewWidthFloat = viewSize.width.toFloat()
        val viewHeightFloat = viewSize.height.toFloat()
        val tileX = (boardWidthFloat * currentDragPoint.x / viewWidthFloat).toInt()
        val tileY = (boardHeightFloat * currentDragPoint.y / viewHeightFloat).toInt()

        if (tileX >= 0 && tileX < width && tileY >= 0 && tileY < height) {
            val tile = tiles[tileY * width + tileX]

            val tileDiameter = viewWidthFloat / boardWidthFloat
            val tileRadius = tileDiameter / 2f
            val tileDragPointX =
                currentDragPoint.x - viewWidthFloat * tile.x.toFloat() / boardWidthFloat
            val tileDragPointY =
                currentDragPoint.y - viewHeightFloat * tile.y.toFloat() / boardHeightFloat
            val distanceToCenter = sqrt(
                (tileDragPointX - tileRadius).pow(2) + (tileDragPointY - tileRadius).pow(2)
            )

            if (distanceToCenter < tileDiameter / 2.75f) {
                val wordTiles = word.model.tiles

                if (wordTiles.size > 1 && tile == wordTiles[wordTiles.size - 2]) {
                    word.onSelectTile(wordTiles.last(), Game.Player.PLAYER_1)
                } else if (!wordTiles.contains(tile)) {
                    word.onSelectTile(tile, Game.Player.PLAYER_1)

                    if (!wordTiles.isEmpty()) {
                        didDragIntoSecondTile = true
                    }
                }
            }
        }
    }

    fun onDragEnd() {
        if (didDragIntoSecondTile && word.model.tiles.size == 1) {
            word.onSelectTile(word.model.tiles.first(), Game.Player.PLAYER_1)
        }
        didDragIntoSecondTile = false
    }

    fun playWord(player: Game.Player) {
        updateOwnershipsForWord(player)
        updateLettersForWord()
        word.onSelectTile(word.model.tiles.first(), player)
    }

    private fun updateOwnershipsForWord(player: Game.Player) {
        for (tile in word.model.tiles) {
            if (tile.isUnowned) {
                tile.ownership = when (player) {
                    Game.Player.PLAYER_1 -> TileModel.Ownership.OWNED_PLAYER_1
                    Game.Player.PLAYER_2 -> TileModel.Ownership.OWNED_PLAYER_2
                }
            } else if (!tile.isOwnedBy(player)) {
                tile.ownership = TileModel.Ownership.UNOWNED
            }
        }

        for (tile in tiles) {
            if (tile.isOwnedBy(Game.Player.PLAYER_1)) {
                tile.ownership =
                    if (adjacentTiles(tile).all { it.isOwnedBy(Game.Player.PLAYER_1) }) {
                        TileModel.Ownership.SUPER_OWNED_PLAYER_1
                    } else {
                        TileModel.Ownership.OWNED_PLAYER_1
                    }
            } else if (tile.isOwnedBy(Game.Player.PLAYER_2)) {
                tile.ownership =
                    if (adjacentTiles(tile).all { it.isOwnedBy(Game.Player.PLAYER_2) }) {
                        TileModel.Ownership.SUPER_OWNED_PLAYER_2
                    } else {
                        TileModel.Ownership.OWNED_PLAYER_2
                    }

            }
        }
    }

    private fun updateLettersForWord() {
        for (tile in word.model.tiles) {
            val previousLetter = tile.letter
            val connectedTiles = connectedTiles(tile)
            val numberOfConnectedVowels =
                connectedTiles.count { WordDictionary.isVowel(it.letter) }
            tile.letter = if (numberOfConnectedVowels < connectedTiles.size / 2f) {
                letterBag.exchangeForVowel(previousLetter)
            } else {
                letterBag.exchangeForConsonant(previousLetter)
            }
        }
    }

    fun connectedTiles(tile: TileViewModel): List<TileViewModel> {
        val tiles = mutableListOf<TileViewModel>()

        addIfExists(tile.x - 1, tile.y - 1, tiles)
        addIfExists(tile.x, tile.y - 1, tiles)
        addIfExists(tile.x + 1, tile.y - 1, tiles)
        addIfExists(tile.x + 1, tile.y, tiles)
        addIfExists(tile.x + 1, tile.y + 1, tiles)
        addIfExists(tile.x, tile.y + 1, tiles)
        addIfExists(tile.x - 1, tile.y + 1, tiles)
        addIfExists(tile.x - 1, tile.y, tiles)

        return tiles
    }

    fun adjacentTiles(tile: TileViewModel): List<TileViewModel> {
        val tiles = mutableListOf<TileViewModel>()

        addIfExists(tile.x, tile.y - 1, tiles)
        addIfExists(tile.x + 1, tile.y, tiles)
        addIfExists(tile.x, tile.y + 1, tiles)
        addIfExists(tile.x - 1, tile.y, tiles)

        return tiles
    }

    private fun addIfExists(x: Int, y: Int, list: MutableList<TileViewModel>) {
        if (x >= 0 && x < width && y >= 0 && y < height)
            list.add(tiles[y * width + x])
    }
}
