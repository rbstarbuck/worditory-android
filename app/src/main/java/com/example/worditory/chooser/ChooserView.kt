package com.example.worditory.chooser

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import com.example.worditory.R
import com.example.worditory.chooser.npcopponent.NpcChooserView

@Composable
fun ChooserView(navController: NavController) {
    Column(Modifier.fillMaxSize().background(colorResource(R.color.background))) {
        NpcChooserView(navController)
    }
}