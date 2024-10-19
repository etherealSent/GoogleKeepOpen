package com.example.petproject

import androidx.navigation.NavHostController
import com.example.petproject.PetScreens.MAIN_SCREEN

private object PetScreens {
    const val MAIN_SCREEN = "main"
}

object PetDestinations {
    const val MAIN_ROUTE = MAIN_SCREEN
}

class PetNavigationActions(private val navController: NavHostController) {
    fun navigateToMainScreen() {
        navController.navigate(PetScreens.MAIN_SCREEN)
    }
}
