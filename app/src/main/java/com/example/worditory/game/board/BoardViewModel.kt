package com.example.worditory.game.board

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import com.example.worditory.audio.AudioPlayer
import com.example.worditory.game.Game
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.board.tile.TileModel
import com.example.worditory.game.board.tile.TileViewModel
import com.example.worditory.game.board.tile.asCharCode
import com.example.worditory.game.board.tile.asLetter
import com.example.worditory.game.board.word.WordViewModel
import com.example.worditory.game.dict.WordDictionary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.pow
import kotlin.math.sqrt

class BoardViewModel(
    model: BoardModel,
    val colorScheme: Tile.ColorScheme,
    internal val isPlayerTurnStateFlow: StateFlow<Boolean> = MutableStateFlow(false),
    onWordChanged: () -> Unit = {}
): ViewModel() {
    internal val width = model.width
    internal val height = model.height
    internal val tiles: List<TileViewModel>
    internal val word = WordViewModel(width, height, onWordChanged)
    internal val letterBag = LetterBag()

    private var currentDragPoint = Offset.Zero

    internal var model: BoardModel
        get() = BoardModel.newBuilder()
            .setWidth(width)
            .setHeight(height)
            .addAllTiles(tiles.map { it.model })
            .build()
        set(value) {
            restoreModel(value)
        }

    init {
        val tileModel = TileModel.newBuilder()
            .setOwnership(TileModel.Ownership.UNOWNED)
            .setLetter(" ".asCharCode())
            .build()

        val tilesData = Array(width * height) { item ->
            TileViewModel(
                model = tileModel,
                x = item % width,
                y = item / width,
                colorScheme = colorScheme
            )
        }

        tiles = tilesData.toList()

        restoreModel(model)

        for (tile in tiles) {
            letterBag.removeLetter(tile.letter)
        }
    }

    internal fun restoreModel(model: BoardModel) {
        assert(model.tilesList.size == tiles.size)

        for (i in 0..<model.tilesList.size) {
            val model = model.tilesList[i]
            val letter = model.letter.asLetter()
            val tile = tiles[i]

            if (tile.letter != letter) {
                tile.letter = letter
            }
            tile.ownership = model.ownership
        }

        updateSuperOwnerships()
    }

    internal fun computeScore(): Game.Score {
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

    internal fun onDragStart(startPoint: Offset) {
        currentDragPoint = startPoint
    }

    private val boardWidthFloat = width.toFloat()
    private val boardHeightFloat = height.toFloat()
    private var didDragIntoSecondTile = true

    internal fun onDrag(offset: Offset, viewSize: IntSize) {
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

    internal fun onDragEnd() {
        if (didDragIntoSecondTile && word.model.tiles.size == 1) {
            word.onSelectTile(word.model.tiles.first(), Game.Player.PLAYER_1)
        }
        didDragIntoSecondTile = false
    }

    internal fun playWord(player: Game.Player) {
        AudioPlayer.wordPlayed(word.model.tiles.size)
        if (!word.model.tiles.isEmpty()) {
            word.onSelectTile(word.model.tiles.first(), player)
        }
    }

    internal fun updateOwnershipsForWord(player: Game.Player) {
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

        updateSuperOwnerships()
    }

    private fun updateSuperOwnerships() {
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

    internal fun updateLettersForWord() {
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

    internal fun connectedTiles(tile: TileViewModel): List<TileViewModel> {
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

    internal fun adjacentTiles(tile: TileViewModel): List<TileViewModel> {
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
