package com.example.ramcb.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ramcb.presentation.screens.Screens
import com.example.ramcb.presentation.screens.characterdetails.CharacterDetailScreen
import com.example.ramcb.presentation.screens.characterdetails.CharacterDetailsScreen
import com.example.ramcb.presentation.screens.dashboard.DashboardScreen

@Composable
fun App(
    onBackPressed: () -> Unit
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = Screens.Dashboard(), builder = {
            composable(route = Screens.Dashboard()) {
                DashboardScreen(
                    onGoToCharacterDetails = { id ->
                        navController.navigate(Screens.CharacterDetails.withArgs(id))
                    },
                )
            }

            composable(
                route = Screens.CharacterDetails(), arguments = listOf(
                    navArgument(CharacterDetailsScreen.CHARACTER_ID_BUNDLE_KEY) {
                        type = NavType.StringType
                    })
            ) {
                CharacterDetailScreen(
                    onBackPressed = navController::navigateUp
                )
            }
        })
}