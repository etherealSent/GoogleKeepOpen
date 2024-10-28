package com.example.petproject

import androidx.navigation.NavHostController
import com.example.petproject.PetDestinationsArgs.NOTE_ID_ARG
import com.example.petproject.PetScreens.EDIT_NOTE_SCREEN
import com.example.petproject.PetScreens.EDIT_TAGS_SCREEN
import com.example.petproject.PetScreens.MAIN_SCREEN

private object PetScreens {
    const val MAIN_SCREEN = "main"
    const val EDIT_NOTE_SCREEN = "edit_note"
    const val EDIT_TAGS_SCREEN = "edit_tags"
}

object PetDestinations {
    const val MAIN_ROUTE = MAIN_SCREEN
    const val EDIT_NOTE_ROUTE = EDIT_NOTE_SCREEN
    const val EDIT_TAGS_ROUTE = EDIT_TAGS_SCREEN

}

object PetDestinationsArgs {
    const val NOTE_ID_ARG = "noteId"
}

class PetNavigationActions(private val navController: NavHostController) {
    fun navigateToMainScreen() {
        navController.navigate(MAIN_SCREEN)
    }

    fun navigateToEditNote(noteId: String?) {
        navController.navigate(
            "note_details/${noteId}"
        )
    }

    fun navigateToEditTags() {
        navController.navigate(
            EDIT_TAGS_SCREEN
        )
    }
}
