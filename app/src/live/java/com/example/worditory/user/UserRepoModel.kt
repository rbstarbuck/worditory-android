package com.example.worditory.user

data class UserRepoModel(
    val email: String? = null,
    val displayName: String? = null,
    val avatarId: Int? = null,
    val gamesPlayed: Int? = null,
    val gamesWon: Int? = null
)