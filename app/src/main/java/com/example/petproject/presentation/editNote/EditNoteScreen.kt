package com.example.petproject.presentation.editNote

import android.content.Intent
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.example.petproject.R
import com.example.petproject.presentation.main.ColorCircle
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
    uri: String?,
    navigateToCopiedNote: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(coroutineScope) {
        if (uri != null) {
            launch {
                viewModel.addNewPhotoPath(uri)
            }
        }
    }

    if (state.copiedId.isNotEmpty()) {
        LaunchedEffect(coroutineScope) {
            launch {
                navigateToCopiedNote(state.copiedId)
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
        addNewPhotoPath = viewModel::addNewPhotoPath,
        bottomSheetType = state.bottomSheetType,
        deleteNote = viewModel::deleteNote,
        archiveNote = viewModel::archiveNote,
        copyNote = viewModel::copyNote,
        color = state.color
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
    updateShowBottomSheet: (Boolean, BottomSheetType) -> Unit,
    showBottomSheet: Boolean,
    sheetState: SheetState,
    lastUpdate: Date,
    coroutineScope: CoroutineScope,
    openCamera: () -> Unit,
    deleteNote: () -> Unit,
    archiveNote: () -> Unit,
    photoPaths: List<String>?,
    isNewNote: Boolean,
    addNewPhotoPath: (String) -> Unit,
    bottomSheetType: BottomSheetType,
    copyNote: () -> Unit,
    color: Color
) {
    val defaultBackground = MaterialTheme.colorScheme.background

    val pickPhoto = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                addNewPhotoPath(uri.toString())
            }
        }
    }

    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, content)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    val context = LocalContext.current


    Scaffold(
        containerColor = if(color == Color.Transparent) defaultBackground else color,
        topBar = {
            EditNoteTopBar(
                color = if(color == Color.Transparent) defaultBackground else color,
                saveNote = saveNote,
                onBack = onBack,
                pinned = pinned,
                onPinned = onPinned,
                archiveNote = archiveNote
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
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp),
                onDismissRequest = {
                    updateShowBottomSheet(false, BottomSheetType.Add)
                },
                sheetState = sheetState,
                dragHandle = {},
            ) {
                when(bottomSheetType) {
                    BottomSheetType.Add -> {
                        Column(
                            modifier = Modifier.padding(top = 5.dp, start = 5.dp)
                        ) {
                            BottomSheetOption(
                                icon = ImageVector.vectorResource(id = R.drawable.photo_camera_24dp_5f6368_fill0_wght400_grad0_opsz24),
                                contentDescription = "Сделать снимок",
                                text = "Сделать снимок",
                                onClick = {
                                    openCamera()
                                    updateShowBottomSheet(false, BottomSheetType.Add)
                                }
                            )
                            BottomSheetOption(
                                icon = ImageVector.vectorResource(id = R.drawable.image_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                                contentDescription = "Добавить картинку",
                                text = "Добавить картинку",
                                onClick = {
                                    updateShowBottomSheet(false, BottomSheetType.Add)
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
                    BottomSheetType.MoreVert -> {
                        Column(
                            modifier = Modifier.padding(top = 5.dp, start = 5.dp)
                        ) {
                            BottomSheetOption(
                                icon = ImageVector.vectorResource(id = R.drawable.delete_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                                contentDescription = "Удалить",
                                text = "Удалить",
                                onClick = {
                                    updateShowBottomSheet(false, BottomSheetType.Add)
                                    deleteNote()
                                    onBack()
                                    saveNote(Calendar.getInstance().time)
                                }
                            )
                            BottomSheetOption(
                                icon = ImageVector.vectorResource(id = R.drawable.content_copy_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                                contentDescription = "Скопировать",
                                text = "Скопировать",
                                onClick = {
                                    copyNote()
                                }
                            )
                            BottomSheetOption(
                                icon = ImageVector.vectorResource(id = R.drawable.share_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                                contentDescription = "Отправить",
                                text = "Отправить",
                                onClick = {
                                    context.startActivity(shareIntent)
                                    updateShowBottomSheet(false, BottomSheetType.Add)
                                }
                            )
                            BottomSheetOption(
                                icon = ImageVector.vectorResource(id = R.drawable.person_add_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                                contentDescription = "Соавторы",
                                text = "Соавторы"
                            )
                            BottomSheetOption(
                                icon = ImageVector.vectorResource(id = R.drawable.label_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                                contentDescription = "Ярлыки",
                                text = "Ярлыки"
                            )
                            BottomSheetOption(
                                icon = ImageVector.vectorResource(id = R.drawable.help_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                                contentDescription = "Справка/отзыв",
                                text = "Справка/отзыв"
                            )
                        }
                    }
                    BottomSheetType.Palette -> {
                        val items = listOf(
                            MaterialTheme.colorScheme.background,
                            Color(0xFF77172F),
                            Color(0xFF672C19),
                            Color(0xFF7C4A02),
                            Color(0xFF284254),
                            Color(0xFF226377),
                            Color(0xFF0F625C),
                            Color(0xFF274D3C),
                            Color(0xFF482E5B),
                            Color(0xFF6C3A4F),
                            Color(0xFF4D4439),
                            Color(0xFF232428)
                        )
                        Column(
                            modifier = Modifier.padding(top = 5.dp, start = 5.dp)
                        ) {
                            Text(modifier = Modifier.padding(10.dp), text = "Цвет", style = MaterialTheme.typography.labelMedium)
                            LazyRow {
                               items(items) { color ->
                                   ColorCircle(
                                       modifier = Modifier.padding(end = 15.dp),
                                       picked = color == Color(0xFF6C3A4F),
                                       color = color,
                                       onColorPicked = {
                                           updateShowBottomSheet(false, BottomSheetType.Add)
                                       }
                                   )
                               }
                            }
                            Text(modifier = Modifier.padding(10.dp), text = "Фон", style = MaterialTheme.typography.labelMedium)
                            LazyRow {
                                items(items) { color ->
                                    ColorCircle(
                                        modifier = Modifier.padding(end = 15.dp),
                                        picked = color == Color(0xFF6C3A4F),
                                        color = color,
                                        onColorPicked = {
                                            updateShowBottomSheet(false, BottomSheetType.Add)
                                        }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
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
        .padding(bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier
                .padding(10.dp)
                .size(24.dp),
            imageVector = icon,
            contentDescription = contentDescription
        )
        Text(text = text, fontSize = 16.sp, style = MaterialTheme.typography.labelMedium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteTopBar(
    color: Color,
    saveNote: (Date) -> Unit,
    onBack: () -> Unit,
    pinned: Boolean,
    onPinned: () -> Unit,
    archiveNote: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = color),
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
                onClick = {
                    archiveNote()
                    saveNote(Calendar.getInstance().time)
                    onBack()
                }
            ) {
                Icon(ImageVector.vectorResource(R.drawable.archive_24dp_e8eaed_fill0_wght400_grad0_opsz24), contentDescription = "pin note")
            }
        }
    )
}

@Composable
fun EditNoteBottomBar(
    updateShowBottomSheet: (Boolean, BottomSheetType) -> Unit,
    lastUpdate: Date,
    isNewNote: Boolean
) {
    BottomAppBar(
        actions = {
            IconButton(
                onClick = {
                    updateShowBottomSheet(true, BottomSheetType.Add)
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
                onClick = {
                    updateShowBottomSheet(true, BottomSheetType.Palette)
                }
            ) {
                Icon(ImageVector.vectorResource(R.drawable.text_format_24dp_5f6368_fill0_wght400_grad0_opsz24), contentDescription = "pin note")
            }
            IconButton(
                onClick = {
                    updateShowBottomSheet(true, BottomSheetType.MoreVert)
                }
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "more actions")
            }
            if (!isNewNote) {
                Text(text = "Изменено $lastUpdate", style = MaterialTheme.typography.bodySmall)
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
            updateShowBottomSheet = { _, _ -> },
            sheetState = rememberModalBottomSheetState(),
            lastUpdate = Date(100L),
            coroutineScope = rememberCoroutineScope(),
            openCamera = {},
            photoPaths = emptyList(),
            isNewNote = false,
            addNewPhotoPath = {},
            bottomSheetType = BottomSheetType.Add,
            deleteNote = {},
            archiveNote = {},
            copyNote = {},
            color = Color(0)
//            formatLastUpdateTime = {}
        )
    }
}
