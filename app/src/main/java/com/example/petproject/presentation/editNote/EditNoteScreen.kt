package com.example.petproject.presentation.editNote

import android.content.res.Configuration
import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key.Companion.I
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.petproject.R
import com.example.petproject.ui.theme.PetProjectTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreenWrapper(
    onBack: () -> Unit,
    sheetState: SheetState,
    viewModel: EditNoteViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    EditNoteScreen(
        title = state.title,
        onTitleChanged = viewModel::updateTitle,
        content = state.content,
        onContentChanged = viewModel::updateContent,
        saveNote = viewModel::saveNote,
        onBack = onBack,
        lastUpdate = state.lastUpdate,
        pinned = state.pinned,
        onPinned = viewModel::updatePinned,
        updateShowBottomSheet = viewModel::updateShowBottomSheet,
        showBottomSheet = state.showBottomSheet,
        sheetState = sheetState,
        coroutineScope = coroutineScope,
        updateLastUpdateTime = viewModel::updateLastUpdateTime,
        formatLastUpdateTime = viewModel::formatLastUpdateTime
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    title: String,
    onTitleChanged: (String) -> Unit,
    content: String,
    onContentChanged: (String) -> Unit,
    saveNote: () -> Unit,
    onBack: () -> Unit,
    pinned: Boolean,
    onPinned: () -> Unit,
    updateShowBottomSheet: (Boolean) -> Unit,
    showBottomSheet: Boolean,
    sheetState: SheetState,
    lastUpdate: Date,
    coroutineScope: CoroutineScope,
    updateLastUpdateTime: (Date) -> Unit,
    formatLastUpdateTime: (Date) -> Unit
) {
    Scaffold(
        topBar = {
            EditNoteTopBar(
                saveNote = saveNote,
                onBack = onBack,
                pinned = pinned,
                onPinned = onPinned,
                updateLastUpdateTime = updateLastUpdateTime
            )
        },
        bottomBar = {
            EditNoteBottomBar(
                updateShowBottomSheet,
                lastUpdate
            )
        }
    ) { innerPadding ->

        LaunchedEffect(coroutineScope) {
            formatLastUpdateTime(Calendar.getInstance().time)
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    updateShowBottomSheet(false)
                },
                sheetState = sheetState,
                dragHandle = {},
            ) {
                Column(
                    modifier = Modifier.padding(top = 10.dp, start = 5.dp)
                ) {
                    BottomSheetOption(
                        icon = ImageVector.vectorResource(id = R.drawable.photo_camera_24dp_5f6368_fill0_wght400_grad0_opsz24),
                        contentDescription = "Делать снимок",
                        text = "Делать снимок"
                    )
                    BottomSheetOption(
                        icon = ImageVector.vectorResource(id = R.drawable.image_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                        contentDescription = "Добавить картинку",
                        text = "Добавить картинку"
                    )
                    BottomSheetOption(
                        icon = ImageVector.vectorResource(id = R.drawable.brush_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                        contentDescription = "Рисунок",
                        text = "Рисунок"
                    )
                    BottomSheetOption(
                        icon = ImageVector.vectorResource(id = R.drawable.mic_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                        contentDescription = "Аудиозапись",
                        text = "Аудиозапись"
                    )
                    BottomSheetOption(
                        icon = ImageVector.vectorResource(id = R.drawable.check_box_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                        contentDescription = "Несколько из списка",
                        text = "Несколько из списка"
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(innerPadding)) {
            TextField(
                value = title,
                onValueChange = onTitleChanged,
                placeholder = { Text("Название", style = MaterialTheme.typography.headlineMedium) },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                )
            )
            TextField(
                modifier = Modifier.offset(y=-20.dp),
                value = content,
                onValueChange = onContentChanged,
                placeholder = { Text("Текст", style = MaterialTheme.typography.bodyLarge) },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                )
            )
        }
    }

}

@Composable
fun BottomSheetOption(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String,
    text: String
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier
                .padding(10.dp)
                .size(28.dp),
            imageVector = icon,
            contentDescription = contentDescription
        )
        Text(text = text)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteTopBar(
    saveNote: () -> Unit,
    onBack: () -> Unit,
    pinned: Boolean,
    onPinned: () -> Unit,
    updateLastUpdateTime: (Date) -> Unit
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = {
                    updateLastUpdateTime(Calendar.getInstance().time)
                    saveNote()
                    onBack()
                }
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "go back")
            }
        },
        actions = {
            IconButton(
                onClick = onPinned
            ) {
                if (pinned) {
                    Icon(ImageVector.vectorResource(R.drawable.keep_24dp_5f6368_fill1_wght400_grad0_opsz24), contentDescription = "pinned note")
                } else {
                    Icon(ImageVector.vectorResource(R.drawable.keep_24dp_5f6368_fill0_wght400_grad0_opsz24), contentDescription = "not pin note")
                }
            }
            IconButton(
                onClick = {}
            ) {
                Icon(ImageVector.vectorResource(R.drawable.add_alert_24dp_5f6368_fill0_wght400_grad0_opsz24), contentDescription = "pin note")
            }
            IconButton(
                onClick = {}
            ) {
                Icon(ImageVector.vectorResource(R.drawable.archive_24dp_e8eaed_fill0_wght400_grad0_opsz24), contentDescription = "pin note")
            }
        }
    )
}

@Composable
fun EditNoteBottomBar(
    updateShowBottomSheet: (Boolean) -> Unit,
    lastUpdate: Date
) {
    BottomAppBar(
        modifier = Modifier.background(Color(0xFF)),
        actions = {
            IconButton(
                onClick = {
                    updateShowBottomSheet(true)
                }
            ) {
                Icon(ImageVector.vectorResource(R.drawable.add_box_24dp_5f6368_fill0_wght400_grad0_opsz24), contentDescription = "pin note")
            }
            IconButton(
                onClick = {}
            ) {
                Icon(ImageVector.vectorResource(R.drawable.palette_24dp_5f6368_fill0_wght400_grad0_opsz24), contentDescription = "pin note")
            }
            IconButton(
                onClick = {}
            ) {
                Icon(ImageVector.vectorResource(R.drawable.text_format_24dp_5f6368_fill0_wght400_grad0_opsz24), contentDescription = "pin note")
            }
            Text(text = "Изменено $lastUpdate", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {}
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "more actions")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditNoteScreenPreview() {
    PetProjectTheme {
        EditNoteScreen(
            title = "",
            onTitleChanged = {},
            content = "",
            onContentChanged = {},
            saveNote = {},
            onBack = {},
            onPinned = {},
            pinned = true,
            showBottomSheet = true,
            updateShowBottomSheet = {},
            sheetState = rememberModalBottomSheetState(),
            lastUpdate = Date(100L),
            coroutineScope = rememberCoroutineScope(),
            updateLastUpdateTime = {},
            formatLastUpdateTime = {}
        )
    }
}
