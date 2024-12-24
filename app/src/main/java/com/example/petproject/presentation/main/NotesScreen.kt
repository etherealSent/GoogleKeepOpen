package com.example.petproject.presentation.main

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.TopAppBarExpandedHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.petproject.R
import com.example.petproject.presentation.model.NoteUi
import com.example.petproject.presentation.model.TagUi
import com.example.petproject.ui.theme.PetProjectTheme
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.example.petproject.presentation.sharedUi.DragNote
import com.example.petproject.presentation.sharedUi.FAB
import com.example.petproject.presentation.sharedUi.Note
import com.example.petproject.presentation.sharedUi.Tag
import com.example.petproject.presentation.sharedUi.TooltipIconButton
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    pinSelectedNotes: () -> Unit,
    menuExpanded: Boolean,
    onExpandMenu: () -> Unit,
    archiveNotes: () -> Unit,
    deleteNotes: () -> Unit,
    copyNote: () -> Unit,
    updateColorDialog: () -> Unit,
    onColorPicked: (Color) -> Unit,
    isColorDialog: Boolean,
    pickedColor: Color
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val context = LocalContext.current

    Scaffold(
        floatingActionButton = { FAB(onAddNote) },
        topBar = {
            if (noteSelected) {
                Box(Modifier.fillMaxWidth()) {
                    // offset не дает выйти за пределы топ бар нужен другой способ
                    DropdownMenu(
                        modifier = Modifier.width(IntrinsicSize.Max),
                        expanded = menuExpanded,
                        onDismissRequest = onExpandMenu,
                        offset = DpOffset(screenWidth, -(TopAppBarDefaults.TopAppBarExpandedHeight)),
                    ) {
                        DropdownMenuItem(
                            text = { Text("Поместить в архив", style = MaterialTheme.typography.bodyLarge) },
                            onClick = {
                                archiveNotes()
                                onExpandMenu()
                            },
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Удалить", style = MaterialTheme.typography.bodyLarge) },
                            onClick = {
                                deleteNotes()
                                onExpandMenu()
                            },
                        )
                        if (selectedNotes.size == 1) {
                            DropdownMenuItem(
                                text = { Text("Скопировать", style = MaterialTheme.typography.bodyLarge) },
                                onClick = {
                                    copyNote()
                                    onExpandMenu()
                                },
                            )
                        }
                        if (selectedNotes.size == 1) {
                            DropdownMenuItem(
                                text = { Text("Отправить", style = MaterialTheme.typography.bodyLarge) },
                                onClick = {
                                    context.startActivity( Intent.createChooser(
                                        Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(Intent.EXTRA_TEXT, selectedNotes[0].content)
                                            type = "text/plain"
                                        },
                                        null
                                    ))
                                    onExpandMenu()
                                },
                            )
                        }
                        DropdownMenuItem(
                            text = { Text("Скопировать в Google Документы", style = MaterialTheme.typography.bodyLarge, maxLines = 1) },
                            onClick = { /* Handle send feedback! */ },
                        )
                    }
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
                            Box {
                                Row {
                                    IconButton(
                                        onClick = pinSelectedNotes
                                    ) {
                                        if (selectedNotes.any { !it.pinned }) {
                                            Icon(
                                                ImageVector.vectorResource(R.drawable.keep_24dp_5f6368_fill0_wght400_grad0_opsz24),
                                                contentDescription = null
                                            )
                                        } else {
                                            Icon(
                                                ImageVector.vectorResource(R.drawable.keep_24dp_5f6368_fill1_wght400_grad0_opsz24),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                    IconButton(
                                        onClick = { /*TODO*/ }
                                    ) {
                                        Icon(
                                            ImageVector.vectorResource(R.drawable.add_alert_24dp_5f6368_fill0_wght400_grad0_opsz24),
                                            contentDescription = null
                                        )
                                    }
                                    IconButton(
                                        onClick = updateColorDialog
                                    ) {
                                        Icon(
                                            ImageVector.vectorResource(R.drawable.palette_24dp_5f6368_fill0_wght400_grad0_opsz24),
                                            contentDescription = null
                                        )
                                    }
                                    IconButton(
                                        onClick = { /*TODO*/ }
                                    ) {
                                        Icon(
                                            ImageVector.vectorResource(R.drawable.label_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                                            contentDescription = null
                                        )
                                    }
                                    IconButton(
                                        onClick = onExpandMenu
                                    ) {
                                        Icon(
                                            Icons.Default.MoreVert, contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            val pinnedNotes = notes.filter { it.pinned }
            val otherNotes = notes.filter { !it.pinned }
            val appBarHeight = 200.dp
            if (isColorDialog) {
                ColorDialog(
                    onColorPicked = onColorPicked,
                    onDismissRequest = updateColorDialog,
                    pickedColor = pickedColor
                )
            }
            val appBarMaxHeightPx = with(LocalDensity.current) { appBarHeight.roundToPx() }
            val connection = remember(appBarMaxHeightPx) {
                CollapsingAppBarNestedScrollConnection(appBarMaxHeightPx)
            }

            when (notesViewType) {
                NotesViewType.Column -> {
                    val lazyListState = rememberLazyListState()
                    val textFieldState = remember { mutableStateOf("") }
                    var expanded by rememberSaveable { mutableStateOf(false) }

                    Box(Modifier.padding(horizontal = 10.dp).nestedScroll(connection)) {
                        if (selectedNotes.isEmpty()) {
                            SearchBar (
                                modifier = Modifier.offset { IntOffset(0,-45 + connection.appBarOffset) },
                                inputField = {
                                    SimpleTextField(
                                        modifier = Modifier.height(45.dp).padding(start = 10.dp),
                                        value = textFieldState.value,
                                        onValueChange = { textFieldState.value = it },
                                        placeholderText = "Искать в заметках",
                                        leadingIcon = {
                                            IconButton(onClick = onNavigationIconClicked) {
                                                Icon(modifier = Modifier.padding(end = 10.dp).size(20.dp), tint = Color(0xFFC0CBD1 ),
                                                    imageVector = ImageVector.vectorResource(id = R.drawable.menu_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                                                    contentDescription = "open side-bar"
                                                )
                                            }
                                        },
                                        trailingIcon = {
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
                                            Icon(modifier = Modifier.padding(end = 10.dp).size(20.dp),
                                                imageVector = Icons.Default.AccountCircle,
                                                contentDescription = null
                                            )
                                        },
                                    )
//                                    SearchBarDefaults.InputField(
//                                        state = textFieldState,
//                                        c,
//                                        onSearch = { expanded = false },
//                                        expanded = expanded,
//                                        onExpandedChange = { expanded = it },
//                                        placeholder = { Text("Hinted search text") },
//                                        leadingIcon = { Icon(modifier = Modifier.size(20.dp), imageVector = Icons.Default.Search, contentDescription = null) },
//                                        trailingIcon = { Icon(modifier = Modifier.size(20.dp), imageVector = Icons.Default.MoreVert, contentDescription = null) },
//                                    )
                                },
                                expanded = expanded,
                                onExpandedChange = { expanded = it },
                            ) {
                                // #TODO()
                            }
                        }
                        LazyColumn(
                            state = lazyListState,
                            modifier = Modifier
                                .fillMaxWidth()
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
                        ScrollBar(lazyListState = lazyListState)
                    }
                }
                NotesViewType.Grid -> {
                    Box {
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
            }
//            AnimatedVisibility(
//                visible = !noteSelected,
//                enter = slideInVertically { fullHeight -> -fullHeight },
//                exit = slideOutVertically { fullHeight -> -fullHeight }
//            ) {
//                SearchBar(modifier = Modifier
//                    .background(Color.Transparent.copy(0.1f))
//                    .padding(start = 20.dp, end = 20.dp, top = 12.dp),
//                    onNavigationIconClicked = onNavigationIconClicked,
//                    notesViewType = notesViewType,
//                    changeNotesViewType = changeNotesViewType
//                )
//            }
        }
    }
}

@Composable
fun SimpleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    placeholderText: String = "",
    fontSize: TextUnit = MaterialTheme.typography.bodyMedium.fontSize,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    cursorBrush: Brush = SolidColor(Color.Black),
) {
    BasicTextField(modifier = modifier
        .fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        maxLines = maxLines,
        enabled = enabled,
        readOnly = readOnly,
        interactionSource = interactionSource,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        onTextLayout = onTextLayout,
        cursorBrush = cursorBrush,
        decorationBox = { innerTextField ->
            Row(
                modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(Modifier.weight(1f)) {
                    if (value.isEmpty()) Text(
                        placeholderText,
                        style = textStyle
                    )
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        }
    )
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
            onSwap = {i,j ->}, onNoteSelected = {}, noteSelected = true, selectedNotes = listOf(), closeNoteSelection = {}, pinSelectedNotes = {},
            menuExpanded = false, onExpandMenu = {}, archiveNotes = {}, deleteNotes = {}, copyNote = {}, onColorPicked = {}, isColorDialog = true,
            pickedColor = Color(0xFF77172F), updateColorDialog = {}
        )
    }
}