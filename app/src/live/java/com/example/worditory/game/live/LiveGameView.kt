package com.example.worditory.game.live

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.example.worditory.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
internal fun LiveGameView(viewModel: LiveGameViewModel) {
    val auth = Firebase.auth
    Box(
        modifier = Modifier
            .background(colorResource(R.color.background))
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Current user email = ${auth.currentUser?.email} and name = ${auth.currentUser?.displayName}",
            color = colorResource(R.color.test_text)
        )
    }
}