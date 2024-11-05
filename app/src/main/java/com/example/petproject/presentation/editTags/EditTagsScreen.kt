package com.example.petproject.presentation.editTags

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.petproject.R
import com.example.petproject.presentation.model.TagUi
import kotlinx.coroutines.Dispatchers

@Composable
fun EditTagsScreenWrapper(
    viewModel: EditTagsViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    EditTagsScreen(
        tags = state.tags,
        editing = state.editingId.isNotEmpty(),
        onEditTagClicked = viewModel::onTagEdit,
        onBack = onBack,
        newTagName = state.newTagName,
        onNewTagNameChanged = viewModel::onNewTagNameChanged,
        onAddClicked = viewModel::createTag,
        onCloseClicked = viewModel::clearNewTag,
        onTagChanged = viewModel::onTagChanged
    )
}

@Composable
fun EditTagsScreen(
    tags: List<TagUi>,
    editing: Boolean,
    onEditTagClicked: (String) -> Unit,
    onBack: () -> Unit,
    newTagName: String,
    onNewTagNameChanged: (String) -> Unit,
    onAddClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    onTagChanged: (TagUi, String) -> Unit
) {
    Scaffold(
        topBar = {
            EditTagsTopBar(onBack)
        }
    ) { innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)) {
            LazyColumn {
                item {
                    CreateTagOption(
                        onTagChange = onNewTagNameChanged,
                        onCloseClicked = onCloseClicked,
                        onAddClicked = onAddClicked,
                        onEditNewTagClicked = { /*TODO*/ },
                        editing = newTagName.isNotEmpty(),
                        tagName = newTagName
                    )
                }
                items(tags, key = { it.id }) { tag ->
                    TagOption(
                        tagUi = tag,
                        onTagChange = {
                            onTagChanged(tag, it)
                        },
                        onDeleteClicked = {},
                        onCompleteClicked = {},
                        editing = editing,
                        onEditTagClicked = { onEditTagClicked(tag.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun CreateTagOption(
    onTagChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onAddClicked: () -> Unit,
    onEditNewTagClicked: () -> Unit,
    editing: Boolean,
    tagName: String
) {
    Row(modifier = Modifier
        .clickable { onEditNewTagClicked() }
        .fillMaxWidth()
        .padding(horizontal = 15.dp), verticalAlignment = Alignment.CenterVertically) {

        if (editing) {
            IconButton(onClick = onCloseClicked) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.close_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                    contentDescription = "tag"
                )
            }
        } else {
            Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "tag"
            )
        }
        TextField(
            value = tagName,
            onValueChange = onTagChange,
            placeholder = { Text("Создать ярлык", style = MaterialTheme.typography.bodyLarge) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            )
        )
        if (editing) {
            IconButton(onClick = onAddClicked) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.check_24dp_e8eaed_fill0_wght400_grad0_opsz24), contentDescription = "edit")
            }
        }
    }
}

@Composable
fun TagOption(
    tagUi: TagUi,
    onTagChange: (String) -> Unit,
    onDeleteClicked: () -> Unit,
    onCompleteClicked: () -> Unit,
    editing: Boolean,
    onEditTagClicked: () -> Unit
) {
    Row(modifier = Modifier
        .clickable { onEditTagClicked() }
        .fillMaxWidth()
        .padding(horizontal = 15.dp), verticalAlignment = Alignment.CenterVertically) {

        if (editing) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.delete_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                    contentDescription = "tag"
                )
            }
        } else {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.label_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                    contentDescription = "tag"
                )
            }
        }
        TextField(
            value = tagUi.name,
            onValueChange = onTagChange,
            placeholder = { Text("Текст", style = MaterialTheme.typography.bodyLarge) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            )
        )
        if (editing) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.check_24dp_e8eaed_fill0_wght400_grad0_opsz24), contentDescription = "edit")
            }
        } else {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.edit_24dp_e8eaed_fill0_wght400_grad0_opsz24), contentDescription = "edit")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTagsTopBar(
    onBackClicked: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "Изменить ярлыки") },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "go back")
            }
        }
    )
}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun EditTagsScreenPreview() {
//    EditTagsScreen(tags = listOf(
//        TagUi("", "ab"),
//        TagUi("", "ab")
//    ),
//        editing = false,
//        onEditTagClicked = {},
//        onBack = {},
//        newTagName = "",
//        onNewTagNameChanged = {},
//        onAddClicked = {},
//        onCloseClicked = {},
//        onTagChanged ={
//
//        },
//    )
//}