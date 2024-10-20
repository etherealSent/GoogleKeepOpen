package com.example.petproject.presentation.main

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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

@Composable
fun MainScreenWrapper(
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    
    MainNavigationDrawer(notes = state.notesWithTags, createNote = viewModel::createNote, tags = emptyList())
}


@Composable
fun MainScreen(
    notes: List<NoteUi>,
    createNote: () -> Unit,
    onNavigationIconClicked: () -> Unit
) {

    Scaffold(
        bottomBar = { BottomBar(createNote) },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(18.dp))
                }
                item {
                    NotesCategoryName(
                        name = "Закрепленные",modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 52.dp)
                    )
                }

                categoryNotesBlock(notes.filter { it.pinned })

                item {
                    NotesCategoryName(
                        name = "Другие",modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                    )
                }
                categoryNotesBlock(notes.filter { !it.pinned })
            }
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically { fullHeight -> -fullHeight },
                exit = slideOutVertically { fullHeight -> -fullHeight }
            ) {
                SearchBar(modifier = Modifier
                    .background(Color.Transparent.copy(0.1f))
                    .padding(start = 20.dp, end = 20.dp, top = 12.dp),
                    onNavigationIconClicked = onNavigationIconClicked
                )
            }
        }
    }
}

inline fun LazyListScope.categoryNotesBlock(noteUis: List<NoteUi>) {
    items(noteUis) { note ->
        Note(modifier = Modifier.padding(bottom = 8.dp),note)
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onNavigationIconClicked: () -> Unit
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

        IconButton(onClick = { /*TODO*/ }) {
            Icon(tint = Color(0xFFC0CBD1),
                imageVector = ImageVector.vectorResource(id = R.drawable.splitscreen_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                contentDescription = "change note display type"
            )

        }

        IconButton(onClick = { /*TODO*/ }) {
            Icon(tint = Color(0xFFC0CBD1),
                imageVector = ImageVector.vectorResource(id = R.drawable.account_circle_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                contentDescription = "google profiles"
            )

        }
    }
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

@Composable
fun Note(
    modifier: Modifier = Modifier,
    noteUi: NoteUi
) {
    BoxWithConstraints {
        val parentWidth = maxWidth
        Column(
            modifier = modifier
                .width(parentWidth)
                .clip(RoundedCornerShape(5.dp))
                .border(
                    width = 1.dp,
                    color = Color(0xFF40464A),
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(vertical = 8.dp, horizontal = 8.dp)
        ) {
            Text(
                text = noteUi.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFDBE1E5)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = noteUi.content,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFDBE1E5)
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (noteUi.tags.size != 0) {
                // TODO: add tags
                TagsLayout(tags = noteUi.tags, parentWidth + 32.dp)
            }
        }
    }
}

@Composable
fun TagsLayout(tags: List<TagUi>, parentWidth: Dp) {
    // currentX and width
    val lastPlaceableValues = remember {
        mutableStateOf(Pair(0, 0))
    }
    var visibleTags by remember {
        mutableStateOf(0)
    }

    var visibleWidth by remember {
        mutableStateOf(mutableListOf(0))
    }


    SubcomposeLayout { constraints ->

        var currentX = 0
        var currentY= 0

        val tagsMeasurement = tags.map { tag ->
                subcompose(tag) { Tag(name = tag.name) }.first().measure(constraints)
        }

        // todo measure andMore tag

        val height = tagsMeasurement[0].height

        val tagsWidth = tagsMeasurement.map { it.width + 16.dp.roundToPx() }

        for (tagWidth in tagsWidth) {
            if (visibleWidth.sum() + tagWidth < parentWidth.roundToPx()) {
                visibleTags++
                visibleWidth.add(tagWidth)
            } else {
                break
            }
        }

        var notVisibleTagsCount = tags.size - visibleTags

        var andMoreTagMeasurement: Placeable? = null
        
        if (notVisibleTagsCount > 0) {
            andMoreTagMeasurement = subcompose(0) { Tag(name = "and ${notVisibleTagsCount} more") }.first().measure(constraints)
            val totalAMTMeasurement = andMoreTagMeasurement.width + 16.dp.roundToPx()
            
        }


        val tagPlaceables = tagsMeasurement.take(visibleTags)

        layout(constraints.maxWidth, height) {

            tagPlaceables.forEachIndexed { index, placeable ->

                placeable.placeRelative(currentX, currentY)
                lastPlaceableValues.value = Pair(currentX, placeable.width)
                currentX += placeable.width + 8.dp.roundToPx()
            }

            if (andMoreTagMeasurement != null) {
                andMoreTagMeasurement.placeRelative(currentX, currentY)
            }
        }
    }
    Text(text = "$visibleTags, ${visibleWidth},${parentWidth}")
}


@Composable
fun BottomBar(createNote: () -> Unit) {
    BottomAppBar(
        floatingActionButton = { FAB(createNote) },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = R.drawable.check_box_24dp_e8eaed_fill0_wght400_grad0_opsz24
                    ),
                    contentDescription = "Add new list"
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = R.drawable.brush_24dp_e8eaed_fill0_wght400_grad0_opsz24
                    ),
                    contentDescription = "Create a painting"
                )            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = R.drawable.mic_24dp_e8eaed_fill0_wght400_grad0_opsz24
                    ),
                    contentDescription = "Create an audio-note"
                )            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = R.drawable.image_24dp_e8eaed_fill0_wght400_grad0_opsz24
                    ),
                    contentDescription = "Create a photo-note"
                )
            }
        }
    )
}

@Composable
fun Tag(
    modifier: Modifier = Modifier,
    name: String
) {
    Text(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = Color(0xFF40464A),
                shape = RoundedCornerShape(12.dp)
            )
            .background(Color(0xFF3D4548))
            .padding(vertical = 4.dp, horizontal = 8.dp),
        text = name,
        color = Color(0xFFBAC0C7)
    )
}

@Composable
fun FAB(
    createNote: () -> Unit
) {
    FloatingActionButton(onClick = createNote) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "create note")
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchBarPreview() {
    PetProjectTheme {
        SearchBar(onNavigationIconClicked = {})
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BottomBarPreview() {
    PetProjectTheme {
        BottomBar({})
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
                "title",
                "content",
                listOf(
                    TagUi("taddk,d"),
                    TagUi("taxxxg"),
                    TagUi("tagmoment"),
                    TagUi("bbb"),
                    TagUi("aab")
                ),
                false
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
        MainScreen(
            notes = listOf(
                NoteUi( "a", "bc",
                    listOf(
                        TagUi("taddk,d"),
                        TagUi( "taxxxg"),
                        TagUi( "tagmoment"),
                        TagUi( "bbb"),
                        TagUi( "aab")
                    ), true),
                NoteUi("b", "bddc", listOf(
                    TagUi( "taddkdg"),
                    TagUi( "taxxxg"),
                    TagUi( "ss"),
                    TagUi( "a")
                ), true),
                NoteUi( "c", "bsxadcc", listOf(
                    TagUi( "taddkdg"),
                    TagUi( "taxxxg"),
                    TagUi( "ssssss"),
                    TagUi( "avdvdvdvqqqv")
                ), false),
                NoteUi("d", "bsxac", listOf(), true),
                NoteUi( "e", "bdxcxdc", listOf(), false),
                NoteUi( "f", "bsxadcc", listOf(), false),
                NoteUi( "f", "bsxadcc", listOf(), false),
                NoteUi( "f", "bsxadcc", listOf(), false),
                NoteUi( "f", "bsxajjwjwdcc", listOf(), false)

            ), createNote = {}, onNavigationIconClicked = {}
        )
    }
}