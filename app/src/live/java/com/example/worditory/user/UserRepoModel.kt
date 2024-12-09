package com.example.worditory.user

import kotlin.math.max
import kotlin.math.pow

data class UserRepoModel(
    val email: String? = null,
    val displayName: String? = null,
    val avatarId: Int? = null,
    val rank: Int? = null,
    val gamesPlayed: Int? = null,
    val gamesWon: Int? = null
)

internal fun UserRepoModel.rank(opponent: UserRepoModel, didWin: Boolean) =
    eloRating(rank ?: 1500, opponent.rank ?: 1500, gamesPlayed ?: 0, if (didWin) 1f else 0f)

private fun eloRating(rankA: Int, rankB: Int, gamesPlayed: Int, outcome: Float) =
    rankA + kValue(gamesPlayed) * (outcome - probability(rankB, rankA))

private fun probability(rankA: Int, rankB: Int) =
    1.0 / (1.0 + 10.0.pow((rankA.toDouble() - rankB.toDouble()) / 400.0))

private fun kValue(gamesPlayed: Int) = max(30f, 450f / (gamesPlayed + 5f))