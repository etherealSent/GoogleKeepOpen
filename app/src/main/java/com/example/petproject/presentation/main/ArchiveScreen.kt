package com.example.petproject.presentation.main

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petproject.R
import com.example.petproject.presentation.model.NoteUi
import com.example.petproject.presentation.model.TagUi
import com.example.petproject.ui.theme.PetProjectTheme

@Composable
fun ArchiveScreen(
    onNavigationIconClicked: () -> Unit,
    onFABClicked: () -> Unit,
    onNoteClicked: (NoteUi) -> Unit,
    notes: List<NoteUi>,
) {
    Scaffold(
        topBar = {
            TopBar(title = "Архив", onNavigationIconClicked = onNavigationIconClicked)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onFABClicked) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        if (notes.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 8.dp)
            ) {
                categoryNotesBlock(
                    categoryName = "Закреплённые",
                    noteUis = notes.filter { it.pinned },
                    onNoteClick = onNoteClicked
                )

                categoryNotesBlock(
                    categoryName = "Другие",
                    notes.filter { !it.pinned },
                    onNoteClick = onNoteClicked
                )
            }
        } else {
            EmptyFiller()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    title: String,
    onNavigationIconClicked: () -> Unit,
    navIconContentDescription: String = ""
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClicked) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.menu_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                    contentDescription = navIconContentDescription
                )
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.splitscreen_24dp_e8eaed_fill0_wght400_grad0_opsz24), contentDescription = null)
            }
        }
    )
}

@Composable
private fun EmptyFiller() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.archive_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            contentDescription = "logo",
            modifier = Modifier.size(150.dp)
        )
        Text(text = "Здесь будут храниться архивированные заметки.", textAlign = TextAlign.Center)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArchiveScreenPreview() {
    PetProjectTheme {
        ArchiveScreen(
            onNavigationIconClicked = { /*TODO*/ },
            onFABClicked = { /*TODO*/ },
            onNoteClicked = {},
            notes = listOf(),
        )
    }
}