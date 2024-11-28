package com.example.petproject.presentation.main

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petproject.R
import com.example.petproject.presentation.model.NoteUi
import com.example.petproject.presentation.model.TagUi
import com.example.petproject.ui.theme.PetProjectTheme
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.petproject.presentation.sharedUi.FAB
import com.example.petproject.presentation.sharedUi.Note
import com.example.petproject.presentation.sharedUi.Tag
import kotlinx.coroutines.CoroutineScope

@Composable
fun NotesScreen(
    notes: List<NoteUi>,
    onNavigationIconClicked: () -> Unit,
    onAddNote: () -> Unit,
    onNoteClick: (NoteUi) -> Unit,
    notesViewType: NotesViewType
) {
    Scaffold(
        floatingActionButton = { FAB(onAddNote) },
        topBar = {
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (notesViewType) {
                NotesViewType.Column -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        categoryNotesBlock(
                            categoryName = "Закреплённые",
                            noteUis = notes.filter { it.pinned },
                            onNoteClick = onNoteClick,
                        )

                        categoryNotesBlock(
                            categoryName = "Другие",
                            notes.filter { !it.pinned },
                            onNoteClick = onNoteClick,
                        )
                    }
                }
                NotesViewType.Grid -> {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        content = {
                            categoryNotesBlock(
                                categoryName = "Закреплённые",
                                noteUis = notes.filter { it.pinned },
                                onNoteClick = onNoteClick,
                            )

                            categoryNotesBlock(
                                categoryName = "Другие",
                                notes.filter { !it.pinned },
                                onNoteClick = onNoteClick,
                            )
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp)
                    )
                }
            }
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically { fullHeight -> -fullHeight },
                exit = slideOutVertically { fullHeight -> -fullHeight }
            ) {
                SearchBar(modifier = Modifier
                    .background(Color.Transparent.copy(0.1f))
                    .padding(start = 20.dp, end = 20.dp, top = 12.dp),
                    onNavigationIconClicked = onNavigationIconClicked,
                    notesViewType = notesViewType
                )
            }
        }
    }
}

inline fun LazyListScope.categoryNotesBlock(categoryName: String = "", noteUis: List<NoteUi>, crossinline onNoteClick: (NoteUi) -> Unit) {
    if (noteUis.isNotEmpty()) {
        item {
            Spacer(modifier = Modifier.height(18.dp))
        }
        item {
            NotesCategoryName(
                name = categoryName, modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 52.dp)
            )
        }
    }
    items(noteUis) { note ->
        Note(modifier = Modifier
            .padding(bottom = 8.dp)
            .clickable { onNoteClick(note) },note)
    }
}

inline fun LazyStaggeredGridScope.categoryNotesBlock(categoryName: String = "", noteUis: List<NoteUi>, crossinline onNoteClick: (NoteUi) -> Unit) {
    if (noteUis.isNotEmpty()) {
        item {
            Spacer(modifier = Modifier.height(18.dp))
        }
        item {
            NotesCategoryName(
                name = categoryName, modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 52.dp)
            )
        }
    }
    items(noteUis) { note ->
        Note(modifier = Modifier
            .padding(bottom = 8.dp)
            .clickable { onNoteClick(note) },note)
    }
}


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onNavigationIconClicked: () -> Unit,
    notesViewType: NotesViewType
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(50))
        .background(Color(0xFF142229))
        .padding(vertical = 1.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onNavigationIconClicked) {
            Icon(tint = Color(0xFFC0CBD1),
                imageVector = ImageVector.vectorResource(id = R.drawable.menu_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                contentDescription = "open side-bar"
            )
        }

        Text(text = "Искать в заметках", color = Color(0xFFC0CBD1))

        Spacer(modifier = Modifier.weight(1f))

        when(notesViewType) {
            NotesViewType.Column -> {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(tint = Color(0xFFC0CBD1),
                        imageVector = ImageVector.vectorResource(id = R.drawable.splitscreen_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                        contentDescription = "change note display type"
                    )
                } 
            }
            NotesViewType.Grid -> {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(tint = Color(0xFFC0CBD1),
                        imageVector = ImageVector.vectorResource(id = R.drawable.grid_view_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                        contentDescription = "change note display type"
                    )
                }
            }
        }

        IconButton(onClick = { /*TODO*/ }) {
            Icon(tint = Color(0xFFC0CBD1),
                imageVector = ImageVector.vectorResource(id = R.drawable.account_circle_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                contentDescription = "google profiles"
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConcreteTopAppBar(
    title: String,
    onNavigationIconClicked: () -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClicked) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.menu_24dp_e8eaed_fill0_wght400_grad0_opsz24), contentDescription = "openMenu")
            }
        }
    )
}

@Composable
fun NotesCategoryName(
    modifier: Modifier,
    name: String
) {
    Box(modifier = modifier) {
        Text(text = name)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchBarPreview() {
    PetProjectTheme {
        SearchBar(onNavigationIconClicked = {}, notesViewType = NotesViewType.Column)
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FABPreview() {
    PetProjectTheme {
        FAB({})
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotePreview() {
    PetProjectTheme {
        Note(
            noteUi = NoteUi(
                title = "title",
                content = "content",
                tags = listOf(
                    TagUi(name = "taddk,d"),
                    TagUi(name ="taxxxg"),
                    TagUi(name ="tagmoment"),
                    TagUi(name ="bbb"),
                    TagUi(name ="aab")
                ),
                pinned = false
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TagPreview() {
    PetProjectTheme {
        Tag(name = "hah")
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainScreenPreview() {
    PetProjectTheme {
        NotesScreen(
            notes = listOf(
                NoteUi( title = "a", content = "bc",
                    tags = listOf(
                        TagUi(name = "taddk,d"),
                        TagUi( name = "taxxxg"),
                        TagUi( name = "tagmoment"),
                        TagUi( name = "bbb"),
                        TagUi( name="aab")
                    ), pinned = true),
                NoteUi( title = "a", content = "bc",
                    tags = listOf(
                        TagUi(name = "taddk,d"),
                        TagUi( name = "taxxxg"),
                        TagUi( name = "tagmoment"),
                        TagUi( name = "bbb"),
                        TagUi( name="aab")
                    ), pinned = true),
                NoteUi( title = "a", content = "bc",
                    tags = listOf(
                        TagUi(name = "taddk,d"),
                        TagUi( name = "taxxxg"),
                        TagUi( name = "tagmoment"),
                        TagUi( name = "bbb"),
                        TagUi( name="aab")
                    ), pinned = true),
                NoteUi(title = "d", content = "bsxac", tags = listOf(), pinned = true),
                NoteUi(title = "d", content = "bsxac", tags = listOf(), pinned = true),
                NoteUi(title = "d", content = "bsxac", tags = listOf(), pinned = true),
                NoteUi(title = "d", content = "bsxac", tags = listOf(), pinned = true),
                NoteUi(title = "d", content = "bsxac", tags = listOf(), pinned = true),


            ), onNavigationIconClicked = {}, onAddNote = {}, onNoteClick = {}, notesViewType = NotesViewType.Column
        )
    }
}