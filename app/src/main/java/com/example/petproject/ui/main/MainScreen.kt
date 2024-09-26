package com.example.petproject.ui.main

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.petproject.R
import com.example.petproject.model.NoteUi
import com.example.petproject.model.TagUi
import com.example.petproject.ui.theme.PetProjectTheme

@Composable
fun MainScreen() {
    Scaffold(
        bottomBar = { BottomBar() }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            SearchBar(modifier = Modifier.padding(horizontal = 16.dp))

            NotesCategoryName(
                name = "Pinned",modifier = Modifier.padding(horizontal = 16.dp)
            )

            CategoryNotesBlock(listOf())

            NotesCategoryName(
                name = "Other",modifier = Modifier.padding(horizontal = 16.dp)
            )

            CategoryNotesBlock(listOf())
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    Row(modifier = modifier
        .fillMaxWidth()
    ) {

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
fun Note(noteUi: NoteUi) {
    BoxWithConstraints {
        val parentWidth = maxWidth
        Column(
            modifier = Modifier
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
                text = noteUi.name,
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

            TagsLayout(tags = noteUi.tags)
        }
    }
}

@Composable
fun TagsLayout(tags: List<TagUi>) {
    SubcomposeLayout { constraints ->
        var currentX = 0
        var currentY= 0

        val tagPlaceables = tags.map { tag ->
            subcompose(tag) { Tag(name = tag.name) }.first().measure(constraints)
        }

        // all has one height
        val height = tagPlaceables[0].height

        layout(constraints.maxWidth, height) {
            tagPlaceables.forEach { placeable ->

                if (currentX + placeable.width <= constraints.maxWidth) {
                    placeable.placeRelative(currentX, currentY)

                    currentX += placeable.width + 8.dp.roundToPx()
                } else {
                    return@forEach
                }
            }
        }
    }
}

@Composable
fun CategoryNotesBlock(
    noteUis: List<NoteUi>,
) {
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        items(noteUis) { note ->
            Note(note)
        }
    }
}

@Composable
fun BottomBar() {
    BottomAppBar(
        floatingActionButton = { FAB() },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
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
    name: String
) {
    Text(
        modifier = Modifier
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
fun FAB() {
    FloatingActionButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "create note")
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchBarPreview() {
    PetProjectTheme {
        SearchBar()
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BottomBarPreview() {
    PetProjectTheme {
        BottomBar()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FABPreview() {
    PetProjectTheme {
        FAB()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotePreview() {
    PetProjectTheme {
        Note(
            noteUi = NoteUi(
                0,
                "title",
                "content",
                listOf(
                    TagUi(0, "tag"),
                    TagUi(1, "tag"),
                    TagUi(2, "tagaaaaaaaaaffffffffffffff"),
                    TagUi(3, "jkkkk")
                )
            )
        )
    }
}

@Preview
@Composable
fun TagPreview() {
    PetProjectTheme {
        Tag(name = "hah")
    }
}
