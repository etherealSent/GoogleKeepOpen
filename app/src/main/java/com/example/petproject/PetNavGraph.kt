package com.example.petproject

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.petproject.PetDestinationsArgs.NOTE_ID_ARG
import com.example.petproject.presentation.editNote.EditNoteScreen
import com.example.petproject.presentation.editNote.EditNoteScreenWrapper
import com.example.petproject.presentation.editTags.EditTagsScreenWrapper
import com.example.petproject.presentation.main.MainNavigationDrawer
import com.example.petproject.presentation.main.MainScreenWrapper
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    startDestination: String = PetDestinations.MAIN_ROUTE,
    navActions: PetNavigationActions = remember(navController) {
        PetNavigationActions(navController)
    }
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(PetDestinations.MAIN_ROUTE) {
            MainScreenWrapper(
                drawerState = drawerState,
                onNoteClick = { noteUi -> navActions.navigateToEditNote(noteUi.id) },
                onAddNote = { navActions.navigateToEditNote(null)},
                onEditTags = {navActions.navigateToEditTags()}
            )
        }
        composable(
            "note_details/{noteId}",
            arguments = listOf(
                navArgument("noteId") { type = NavType.StringType; nullable = true }
            )
        ) { entry ->
            val noteId = entry.arguments?.getString("noteId")
            val sheetState = rememberModalBottomSheetState()

            EditNoteScreenWrapper(
                onBack = { navController.popBackStack() },
                sheetState = sheetState,
                coroutineScope = coroutineScope
            )
        }
        composable(PetDestinations.EDIT_TAGS_ROUTE) {
            EditTagsScreenWrapper(
                onBack = { navController.popBackStack() }
            )
        }
    }
}