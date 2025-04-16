package com.economist.demo.pip.screens.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LaunchScreen(
    onStartClicked: () -> Unit,
    onHomeScreenClicked: () -> Unit,
    onSettingsScreenClicked: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
       Column(
           modifier = Modifier.fillMaxSize(),
           horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.Center
       ) {
           Button(onClick = onStartClicked) {
               Text("Start Video")
           }

           Spacer(modifier = Modifier.height(10.dp))

           Button(onClick = onHomeScreenClicked) {
               Text("Navigate to Home Screen")
           }

           Spacer(modifier = Modifier.height(10.dp))

           Button(onClick = onSettingsScreenClicked) {
               Text("Navigate to Settings Screen")
           }
       }
    }
}
