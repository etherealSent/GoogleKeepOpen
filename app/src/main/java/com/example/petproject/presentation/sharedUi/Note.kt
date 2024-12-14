package com.example.petproject.presentation.sharedUi

import android.content.ClipData
import android.content.Context
import android.content.res.Configuration
import android.text.Layout
import android.util.Log
import android.view.WindowManager
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.petproject.presentation.main.NotesViewType
import com.example.petproject.presentation.model.NoteUi
import com.example.petproject.presentation.model.TagUi
import com.example.petproject.ui.theme.PetProjectTheme

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun Note(
    modifier: Modifier = Modifier,
    noteUi: NoteUi,
    isSelected: Boolean,
    notesViewType: NotesViewType = NotesViewType.Column,
    ) {

    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val heightPx = displayMetrics.heightPixels

    val windowSizeClass = WindowSizeClass.calculateFromSize(size =DpSize(displayMetrics.widthPixels.dp, displayMetrics.heightPixels.dp))

    val noteHeight = when(windowSizeClass.heightSizeClass) {
        WindowHeightSizeClass.Compact -> {
            heightPx.div(1.5).dp
        }
        WindowHeightSizeClass.Medium -> {
            heightPx.div(1.5).dp
        }
        WindowHeightSizeClass.Expanded -> {
            heightPx.div(3).dp
        }
        else -> {
            heightPx.div(3).dp
        }
    }

    NoteContainer(
        modifier = modifier,
        isSelected = isSelected,
        noteUi = noteUi,
        noteHeight = noteHeight
    )
}

@Composable
fun NoteContainer(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    noteUi: NoteUi,
    noteHeight: Dp
) {

    val textStyle = MaterialTheme.typography.bodySmall
    val textMeasurer = rememberTextMeasurer()
    val noteHeightMeasurement = remember(textStyle, textMeasurer) {
        textMeasurer.measure(
            text = "a",
            style = textStyle.copy(textAlign = TextAlign.Center)
        ).size.height
    }

    val contentHeight = (noteHeight - 36.dp - noteHeightMeasurement.dp)
    val maxLine = contentHeight.div( noteHeightMeasurement).value.toInt()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(noteUi.color)
            .let {
                if (isSelected) {
                    it.border(
                        width = 3.dp,
                        color = Color(0xFF2379B7),
                        shape = RoundedCornerShape(5.dp)
                    )
                } else {
                    it.border(
                        width = 1.dp,
                        color = Color(0xFF40464A),
                        shape = RoundedCornerShape(5.dp)
                    )
                }
            }
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
            color = Color(0xFFDBE1E5),
            maxLines = maxLine,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (noteUi.tags.isNotEmpty()) {
            TagRow(noteUi.tags)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DragNote(
    noteUi: NoteUi,
    modifier: Modifier = Modifier,
    isSelected: Boolean
) {
    Note(
        noteUi = noteUi,
        modifier = modifier
//            .dragAndDropSource {
//            detectTapGestures(
//                onLongPress = {
//                    startTransfer(
//                        DragAndDropTransferData(
//                            ClipData.newPlainText("note", "note")
//                        )
//                    )
//                }
//            )
//            },
        ,isSelected = isSelected,
    )
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotePreview() {
    PetProjectTheme {
        Note(
            noteUi = NoteUi(
                id = "1",
                title = "Title",
                content = "Now in Android uses the Gradle build system and can be imported directly into Android Studio (make sure you are using the latest stable version available",
                tags = listOf()
            ),
            isSelected = false
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SelectedNotePreview() {
    PetProjectTheme {
        Note(
            noteUi = NoteUi(
                id = "1",
                title = "Title",
                content = "Now in Android uses the Gradle build system and can be imported directly into Android Studio (make sure you are using the latest stable version available",
                tags = listOf(),
            ),
            isSelected = true
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DragNotePreview() {
    PetProjectTheme {
        Scaffold {
            Column(Modifier.padding(it)) {
                DragNote(
                    noteUi = NoteUi(
                        id = "1",
                        title = "Title",
                        content = "Now in Android uses the Gradle build system and can be imported directly into Android Studio (make sure you are using the latest stable version available",
                        tags = listOf()
                    ),
                    isSelected = false
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NoteWithTagsPreview() {
    PetProjectTheme {
        Note(
            noteUi = NoteUi(
                id = "1",
                title = "Title",
                content = "Now in Android uses the Gradle build system and can be imported directly into Android Studio (make sure you are using the latest stable version available",
                tags = listOf(
                    TagUi(
                        id = "1",
                        name = "Tag 1"
                    ),
                    TagUi(
                        id = "2",
                        name = "Tag 2"
                    ),
                    TagUi(
                        id = "3",
                        name = "Tag 3"
                    ),
                    TagUi(
                        id = "4",
                        name = "Tag 4"
                    ),
                    TagUi(
                        id = "5",
                        name = "Tag 5"
                    ),
                    TagUi(
                        id = "6",
                        name = "Tag 6"
                    ),
                    TagUi(
                        id = "7",
                        name = "Tag 7"
                    )
                )
            ),
            isSelected = false
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)@Composable
fun NoteScreenPreview() {
    PetProjectTheme {
        Scaffold {
            Column(Modifier.padding(it)) {
                Note(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                    noteUi = NoteUi(
                        id = "1",
                        title = "Title",
                        content = "Now in Android uses the Gradle build system and can be imported directly into Android Studio (make sure you are using the latest stable version available",
                        tags = listOf(
                            TagUi(
                                id = "1",
                                name = "Tag 1"
                            ),
                            TagUi(
                                id = "2",
                                name = "Tag 2"
                            ),
                            TagUi(
                                id = "3",
                                name = "Tag 3"
                            ),
                            TagUi(
                                id = "4",
                                name = "Tag 4"
                            ),
                            TagUi(
                                id = "5",
                                name = "Tag 5"
                            ),
                            TagUi(
                                id = "6",
                                name = "Tag 6"
                            ),
                            TagUi(
                                id = "7",
                                name = "Tag 7"
                            )
                        )
                    ),
                    isSelected = false
                )
            }
        }
    }
}
