package com.example.petproject.presentation.editNote

import android.content.res.Configuration
import android.graphics.drawable.Icon
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key.Companion.I
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.example.petproject.R
import com.example.petproject.presentation.model.NoteUi
import com.example.petproject.ui.theme.PetProjectTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
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
    coroutineScope: CoroutineScope,
    openCamera: (String?, String) -> Unit,
    uri: String?
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(coroutineScope) {
        if (uri != null) {
            launch {
                viewModel.addNewPhotoPath(uri)
            }
        }
    }

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
        openCamera = { openCamera(state.noteId, state.title) },
        photoPaths = state.photoPaths,
        isNewNote = state.noteId == "",
        addNewPhotoPath = viewModel::addNewPhotoPath
//        formatLastUpdateTime = viewModel::formatLastUpdateTime
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun EditNoteScreen(
    title: String,
    onTitleChanged: (String) -> Unit,
    content: String,
    onContentChanged: (String) -> Unit,
    saveNote: (Date) -> Unit,
    onBack: () -> Unit,
    pinned: Boolean,
    onPinned: () -> Unit,
    updateShowBottomSheet: (Boolean) -> Unit,
    showBottomSheet: Boolean,
    sheetState: SheetState,
    lastUpdate: Date,
    coroutineScope: CoroutineScope,
    openCamera: () -> Unit,
    photoPaths: List<String>?,
    isNewNote: Boolean,
    addNewPhotoPath: (String) -> Unit
//    formatLastUpdateTime: (Date) -> Unit
) {

    val pickPhoto = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                addNewPhotoPath(uri.toString())
            }
        }
    }


    Scaffold(
        topBar = {
            EditNoteTopBar(
                saveNote = saveNote,
                onBack = onBack,
                pinned = pinned,
                onPinned = onPinned,
            )
        },
        bottomBar = {
            EditNoteBottomBar(
                updateShowBottomSheet,
                lastUpdate,
                isNewNote
            )
        }
    ) { innerPadding ->
//
//        LaunchedEffect(coroutineScope) {
//            formatLastUpdateTime(Calendar.getInstance().time)
//        }

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
                        contentDescription = "Сделать снимок",
                        text = "Делать снимок",
                        onClick = {
                            openCamera()
                            updateShowBottomSheet(false)
                        }
                    )
                    BottomSheetOption(
                        icon = ImageVector.vectorResource(id = R.drawable.image_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                        contentDescription = "Добавить картинку",
                        text = "Добавить картинку",
                        onClick = {
                            pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
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

        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            item {
                photoPaths?.forEach {
//                    Text(text = it)
                    Image(
                        painter = rememberAsyncImagePainter(
                            model  = Uri.parse(it)  // or ht
                        ),
                        contentDescription = "takenPhoto",
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
            item {
                TextField(
                    value = title,
                    onValueChange = onTitleChanged,
                    textStyle = MaterialTheme.typography.headlineMedium,
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
}

@Composable
fun HorizontalUncontainedImageCarousel() {

}

@Composable
fun BottomSheetOption(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String,
    text: String,
    onClick: () -> Unit = {}
) {
    Row(modifier = modifier
        .clickable { onClick() }
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
    saveNote: (Date) -> Unit,
    onBack: () -> Unit,
    pinned: Boolean,
    onPinned: () -> Unit,
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = {
                    saveNote(Calendar.getInstance().time)
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
    lastUpdate: Date,
    isNewNote: Boolean
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
            if (!isNewNote) {
                Text(text = "Изменено $lastUpdate", style = MaterialTheme.typography.bodySmall)
            }
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
            openCamera = {},
            photoPaths = emptyList(),
            isNewNote = false,
            addNewPhotoPath = {}
//            formatLastUpdateTime = {}
        )
    }
}
