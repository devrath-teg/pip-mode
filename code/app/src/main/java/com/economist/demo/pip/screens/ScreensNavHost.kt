package com.economist.demo.pip.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.economist.demo.pip.screens.composables.HomeScreen
import com.economist.demo.pip.screens.composables.LaunchScreen
import com.economist.demo.pip.screens.composables.SettingsScreen
import com.economist.demo.pip.screens.composables.VideoPlayerScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreensNavHost(
    launchPlayerScreen: () -> Unit,
    onBackPressed: () -> Unit

) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "launch"
    ) {
        composable("launch") {
            LaunchScreen(
                onStartClicked = {
                    launchPlayerScreen.invoke()
                },
                onHomeScreenClicked = {
                    navController.navigate("home")
                },
                onSettingsScreenClicked = {
                    navController.navigate("settings")
                }
            )
        }

        composable("home") {
            HomeScreen (homeButtonClicked = {
                onBackPressed.invoke()
            })
        }

        composable("settings") {
            SettingsScreen(settingsClicked = {
                onBackPressed.invoke()
            })
        }
    }
}
