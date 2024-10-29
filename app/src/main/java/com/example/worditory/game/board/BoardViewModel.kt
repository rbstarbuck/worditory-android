package com.example.worditory.game.board

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import com.example.worditory.game.Game
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.board.tile.TileViewModel
import com.example.worditory.game.board.word.WordModel
import com.example.worditory.game.board.word.WordViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlin.Array
import kotlin.math.pow
import kotlin.math.sqrt

class BoardViewModel(
    val width: Int,
    val height: Int,
    val isPlayerTurnStateFlow: StateFlow<Boolean>
): ViewModel() {
    val tiles: Array<Array<TileViewModel>>
    val flatTiles: List<TileViewModel>
    val word = WordViewModel(width, height)
    val letterBag = LetterBag()

    var currentDragPoint = Offset.Zero

    init {
        val colorScheme = Tile.ColorScheme.random()

        tiles = Array(height) { y ->
            val ownership = when {
                y == 0 -> Tile.Ownership.OWNED_PLAYER_2
                y == height - 1 -> Tile.Ownership.OWNED_PLAYER_1
                else -> Tile.Ownership.UNOWNED
            }
            Array(width) { x ->
                val letter = letterBag.takeLetter()
                TileViewModel(x, y, letter, ownership, colorScheme)
            }
        }

        flatTiles = tiles.flatten()
    }

    fun getScore(): Game.Score {
        var scorePlayer1 = 0
        var scorePlayer2 = 0

        for (tile in flatTiles) {
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
            val tile = tiles[tileY][tileX]

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
                val wordTiles = word.model.value.tiles

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
        if (didDragIntoSecondTile && word.model.value.tiles.size == 1) {
            word.setModel(WordModel())
        }
        didDragIntoSecondTile = false
    }

    fun updateOwnershipsForWord(player: Game.Player) {
        for (tile in word.model.value.tiles) {
            if (tile.isUnowned) {
                tile.setOwnership(
                    when (player) {
                        Game.Player.PLAYER_1 -> Tile.Ownership.OWNED_PLAYER_1
                        Game.Player.PLAYER_2 -> Tile.Ownership.OWNED_PLAYER_2
                    }
                )
            } else if (!tile.isOwnedBy(player)) {
                tile.setOwnership(Tile.Ownership.UNOWNED)
            }
        }

        for (tile in flatTiles) {
            if (tile.isOwnedBy(Game.Player.PLAYER_1)) {
                tile.setOwnership(
                    if (adjacentTiles(tile).all { it.isOwnedBy(Game.Player.PLAYER_1) })
                        Tile.Ownership.SUPER_OWNED_PLAYER_1
                    else Tile.Ownership.OWNED_PLAYER_1
                )
            } else if (tile.isOwnedBy(Game.Player.PLAYER_2)) {
                tile.setOwnership(
                    if (adjacentTiles(tile).all { it.isOwnedBy(Game.Player.PLAYER_2) })
                        Tile.Ownership.SUPER_OWNED_PLAYER_2
                    else Tile.Ownership.OWNED_PLAYER_2
                )
            }
        }
    }

    fun updateLettersForWord() {
        for (tile in word.model.value.tiles) {
            val previousLetter = tile.letter.value
            tile.setLetter(letterBag.exchangeLetter(previousLetter))
        }
    }

    fun playWord(player: Game.Player) {
        updateOwnershipsForWord(player)
        updateLettersForWord()
        word.setModel(WordModel())
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
            list.add(tiles[y][x])
    }
}
