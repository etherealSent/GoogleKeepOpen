package com.example.petproject.presentation.main

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petproject.R
import com.example.petproject.presentation.model.NoteUi
import com.example.petproject.presentation.model.TagUi
import com.example.petproject.ui.theme.PetProjectTheme
import androidx.compose.ui.unit.sp
import com.example.petproject.presentation.sharedUi.DragNote
import com.example.petproject.presentation.sharedUi.FAB
import com.example.petproject.presentation.sharedUi.Note
import com.example.petproject.presentation.sharedUi.Tag
import com.example.petproject.presentation.sharedUi.TooltipIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    notes: List<NoteUi>,
    onNavigationIconClicked: () -> Unit,
    onAddNote: () -> Unit,
    onNoteClick: (NoteUi) -> Unit,
    notesViewType: NotesViewType,
    changeNotesViewType: () -> Unit,
    onSwap: (Int, Int) -> Unit,
    onNoteSelected: (NoteUi) -> Unit,
    noteSelected: Boolean,
    selectedNotes: List<NoteUi>,
    closeNoteSelection: () -> Unit,
    pinSelectedNotes: () -> Unit
) {
    Scaffold(
        floatingActionButton = { FAB(onAddNote) },
        topBar = {
            if (noteSelected) {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = closeNoteSelection
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "close"
                            )
                        }
                    },
                    title = { Text(text = "${selectedNotes.size}") },
                    actions = {
                        IconButton(
                            onClick = pinSelectedNotes
                        ) {
                            if (selectedNotes.any {  !it.pinned }) {
                                Icon(
                                    ImageVector.vectorResource(R.drawable.keep_24dp_5f6368_fill0_wght400_grad0_opsz24), contentDescription = null
                                )
                            } else {
                                Icon(
                                    ImageVector.vectorResource(R.drawable.keep_24dp_5f6368_fill1_wght400_grad0_opsz24), contentDescription = null
                                )
                            }
                        }
                        IconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                ImageVector.vectorResource(R.drawable.add_alert_24dp_5f6368_fill0_wght400_grad0_opsz24), contentDescription = null
                            )
                        }
                        IconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                ImageVector.vectorResource(R.drawable.palette_24dp_5f6368_fill0_wght400_grad0_opsz24), contentDescription = null
                            )
                        }
                        IconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                ImageVector.vectorResource(R.drawable.label_24dp_e8eaed_fill0_wght400_grad0_opsz24), contentDescription = null
                            )
                        }
                        IconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                Icons.Default.MoreVert, contentDescription = null
                            )
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            val pinnedNotes = notes.filter { it.pinned }
            val otherNotes = notes.filter { !it.pinned }

            when (notesViewType) {
                NotesViewType.Column -> {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        if (pinnedNotes.isNotEmpty()) {
                            item {
                                NotesCategoryName(
                                    name = "Закреплённые",
                                    modifier = Modifier.padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = 12.dp,
                                        top = if (!noteSelected) 76.dp else 8.dp
                                    )
                                )
                            }
                            categoryNotesBlock(
                                noteUis = pinnedNotes,
                                onNoteClick = onNoteClick,
                                onNoteSelected = onNoteSelected,
                                noteSelected = noteSelected,
                                selectedNotes = selectedNotes
                            )
                        }

                        if (otherNotes.isNotEmpty() && pinnedNotes.isNotEmpty()) {
                            item {
                                NotesCategoryName(
                                    name = "Другие",
                                    modifier = Modifier.padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = 12.dp,
                                        top = 6.dp
                                    )
                                )
                            }
                            categoryNotesBlock(
                                noteUis = otherNotes,
                                onNoteClick = onNoteClick,
                                onNoteSelected = onNoteSelected,
                                noteSelected = noteSelected,
                                selectedNotes = selectedNotes
                            )
                        } else if (otherNotes.isNotEmpty()) {
                            item {
                                NotesCategoryName(
                                    name = "Другие",
                                    modifier = Modifier.padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = 12.dp,
                                        top = if (!noteSelected) 76.dp else 8.dp
                                    )
                                )
                            }
                            categoryNotesBlock(
                                noteUis = otherNotes,
                                onNoteClick = onNoteClick,
                                onNoteSelected = onNoteSelected,
                                noteSelected = noteSelected,
                                selectedNotes = selectedNotes
                            )
                        }
                    }
                }
                NotesViewType.Grid -> {
                    Column(Modifier.padding(horizontal = 8.dp)) {
                        if (pinnedNotes.isNotEmpty()) {
                            Column {
                                NotesCategoryName(
                                    name = "Закреплённые", modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp, top = if (!noteSelected) 76.dp else 8.dp)
                                )
                                LazyVerticalStaggeredGrid(
                                    columns = StaggeredGridCells.Fixed(2),
                                    content = {
                                        categoryNotesBlock(
                                            noteUis = pinnedNotes,
                                            onNoteClick = onNoteClick,
                                            onNoteSelected = onNoteSelected,
                                            noteSelected = noteSelected,
                                            selectedNotes = selectedNotes
                                        )
                                    },
                                    verticalItemSpacing = 8.dp,
                                    horizontalArrangement =  Arrangement.spacedBy(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                        }

                        if (otherNotes.isNotEmpty() && pinnedNotes.isNotEmpty()) {
                            Column {
                                NotesCategoryName(
                                    name = "Другие", modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp, top = 10.dp)
                                )
                                LazyVerticalStaggeredGrid(
                                    columns = StaggeredGridCells.Fixed(2),
                                    content = {
                                        categoryNotesBlock(
                                            noteUis = otherNotes,
                                            onNoteClick = onNoteClick,
                                            onNoteSelected = onNoteSelected,
                                            noteSelected = noteSelected,
                                            selectedNotes = selectedNotes)
                                    },
                                    verticalItemSpacing = 8.dp,
                                    horizontalArrangement =  Arrangement.spacedBy(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                        } else if (otherNotes.isNotEmpty()) {
                            Column {
                                NotesCategoryName(
                                    name = "Другие", modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp, top = if (!noteSelected) 76.dp else 8.dp)
                                )
                                LazyVerticalStaggeredGrid(
                                    columns = StaggeredGridCells.Fixed(2),
                                    content = {
                                        categoryNotesBlock(
                                            noteUis = otherNotes,
                                            onNoteClick = onNoteClick,
                                            onNoteSelected = onNoteSelected,
                                            noteSelected = noteSelected,
                                            selectedNotes = selectedNotes)
                                    },
                                    verticalItemSpacing = 8.dp,
                                    horizontalArrangement =  Arrangement.spacedBy(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = !noteSelected,
                enter = slideInVertically { fullHeight -> -fullHeight },
                exit = slideOutVertically { fullHeight -> -fullHeight }
            ) {
                SearchBar(modifier = Modifier
                    .background(Color.Transparent.copy(0.1f))
                    .padding(start = 20.dp, end = 20.dp, top = 12.dp),
                    onNavigationIconClicked = onNavigationIconClicked,
                    notesViewType = notesViewType,
                    changeNotesViewType = changeNotesViewType
                )
            }
        }
    }
}

@Composable
fun NotesTopBar() {

}

@OptIn(ExperimentalFoundationApi::class)
inline fun LazyListScope.categoryNotesBlock(
    noteUis: List<NoteUi>,
    crossinline onNoteClick: (NoteUi) -> Unit = {},
    crossinline onNoteSelected: (NoteUi) -> Unit = {},
    noteSelected: Boolean,
    selectedNotes: List<NoteUi>
) {
    items(noteUis) { note ->
        DragNote(modifier = Modifier
            .padding(bottom = 8.dp)
            .combinedClickable(
                onClick = {
                    if (noteSelected) {
                        onNoteSelected(note)
                    } else {
                        onNoteClick(note)
                    }
                },
                onLongClick = {
                    onNoteSelected(note)
                }
            ),
            noteUi = note,
            isSelected = selectedNotes.contains(note)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
inline fun LazyStaggeredGridScope.categoryNotesBlock(
    noteUis: List<NoteUi>,
    crossinline onNoteClick: (NoteUi) -> Unit,
    selectedNotes: List<NoteUi>,
    crossinline onNoteSelected: (NoteUi) -> Unit = {},
    noteSelected: Boolean,
) {
    items(noteUis) { note ->
        DragNote(modifier = Modifier
            .padding(bottom = 8.dp)
            .combinedClickable(
                onClick = {
                    if (noteSelected) {
                        onNoteSelected(note)
                    } else {
                        onNoteClick(note)
                    }
                },
                onLongClick = {
                    onNoteSelected(note)
                }
            ),
            noteUi = note,
            isSelected = selectedNotes.contains(note)
        )
    }
}


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onNavigationIconClicked: () -> Unit,
    notesViewType: NotesViewType,
    changeNotesViewType: () -> Unit
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
                TooltipIconButton(
                    tooltipText = "Один столбец",
                    iconContentDescription = "change note display type to one column",
                    iconResource = R.drawable.splitscreen_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                    onClick = changeNotesViewType,
                    tooltipModifier = Modifier.padding(top = 8.dp)
                )
            }
            NotesViewType.Grid -> {
                TooltipIconButton(
                    tooltipText = "Несколько столбцов",
                    iconContentDescription = "change note display type to several columns",
                    iconResource = R.drawable.grid_view_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                    onClick = changeNotesViewType,
                    tooltipModifier = Modifier.padding(top = 8.dp)
                )
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
        Text(text = name, fontSize = 12.sp)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchBarPreview() {
    PetProjectTheme {
        SearchBar(onNavigationIconClicked = {}, notesViewType = NotesViewType.Column, changeNotesViewType = {})
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
            ), isSelected = false
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
                NoteUi(title = "d", content = "bsxac", tags = listOf(), pinned = false),
                NoteUi(title = "d", content = "bsxac", tags = listOf(), pinned = true),
                NoteUi(title = "d", content = "bsxac", tags = listOf(), pinned = true),
                NoteUi(title = "d", content = "bsxac", tags = listOf(), pinned = true),


            ), onNavigationIconClicked = {}, onAddNote = {}, onNoteClick = {}, notesViewType = NotesViewType.Column, changeNotesViewType = {},
            onSwap = {i,j ->}, onNoteSelected = {}, noteSelected = false, selectedNotes = listOf(), closeNoteSelection = {}, pinSelectedNotes = {}
        )
    }
}