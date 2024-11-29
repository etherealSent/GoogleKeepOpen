package com.example.petproject.presentation.sharedUi

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petproject.presentation.model.NoteUi
import com.example.petproject.presentation.model.TagUi
import com.example.petproject.ui.theme.PetProjectTheme

@Composable
fun Note(
    modifier: Modifier = Modifier,
    noteUi: NoteUi
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
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

        if (noteUi.tags.isNotEmpty()) {
            TagRow(noteUi.tags)
        }
    }
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
            )
        )
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
            )
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
                    )
                )
            }
        }
    }
}
