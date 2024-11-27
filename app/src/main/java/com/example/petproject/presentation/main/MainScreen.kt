package com.example.petproject.presentation.main

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.petproject.presentation.model.NoteUi
import com.example.petproject.presentation.model.TagUi
import com.example.petproject.ui.theme.PetProjectTheme
import kotlinx.coroutines.launch
import com.example.petproject.R
import kotlinx.coroutines.CoroutineScope

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int = 0,
    val tagId: String? = null
)

const val UP_NAVIGATION_ITEMS_SIZE = 2

@Composable
fun MainScreenWrapper(
    drawerState: DrawerState,
    onAddNote: () -> Unit,
    onNoteClick: (NoteUi) -> Unit,
    onEditTags : () -> Unit,
    coroutineScope: CoroutineScope,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    MainNavigationDrawer(
        notes = state.notesWithTags,
        tags = state.tags,
        onNoteClick = onNoteClick,
        onAddNote = onAddNote,
        drawerState = drawerState,
        onEditTags = onEditTags,
        mainScreenType = state.uiMainState.screenType,
        changeScreenType = viewModel::changeScreenType,
        coroutineScope = coroutineScope,
        onHelpClicked = {},
        onSettingsClicked = {},
        selectedIndex = state.uiMainState.selectedIndex,
        selectIndex = viewModel::selectNavDrawer,
        selectedTagUi = state.uiMainState.selectedTagUi,
        onTagUiSelected = viewModel::selectTag
    )
}

@Composable
fun MainNavigationDrawer(
    notes: List<NoteUi>,
    onAddNote: () -> Unit,
    onEditTags : () -> Unit,
    onNoteClick: (NoteUi) -> Unit,
    tags: List<TagUi>,
    drawerState: DrawerState,
    mainScreenType: MainScreenType,
    changeScreenType: (MainScreenType) -> Unit,
    selectedTagUi: TagUi,
    selectIndex: (Int) -> Unit,
    selectedIndex: Int,
    coroutineScope: CoroutineScope,
    onSettingsClicked: () -> Unit,
    onHelpClicked: () -> Unit,
    onTagUiSelected: (TagUi) -> Unit
) {
    val upNavigationItems = listOf(
        NavigationItem(
            title = "Заметки",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.lightbulb_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.lightbulb_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            badgeCount = 0
        ),
        NavigationItem(
            title = "Напоминания",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.notifications_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.notifications_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            badgeCount = 1
        )
    )

    val tagNavigationItems = tags.mapIndexed { index, tag ->
        NavigationItem(
            title = tag.name,
            selectedIcon = ImageVector.vectorResource(id = R.drawable.label_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.label_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            badgeCount = UP_NAVIGATION_ITEMS_SIZE - 1 + index,
            tagId = tag.id
        )
    }


    val bottomItems = listOf(
        NavigationItem(
            title = "Архив",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.archive_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.archive_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            badgeCount = UP_NAVIGATION_ITEMS_SIZE + tags.size
        ),
        NavigationItem(
            title = "Корзина",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.delete_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.delete_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            badgeCount = UP_NAVIGATION_ITEMS_SIZE + tags.size + 1
        ),
        NavigationItem(
            title = "Настройки",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.settings_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.settings_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            badgeCount = UP_NAVIGATION_ITEMS_SIZE + tags.size + 2
        ),
        NavigationItem(
            title = "Справка/отзыв",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.help_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.help_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            badgeCount = UP_NAVIGATION_ITEMS_SIZE + tags.size + 3
        ),
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val width = (maxWidth * 8) / 10
                ModalDrawerSheet(modifier = Modifier.width(width)) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Google Keep",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    NavigationDrawerItem(
                        label = { Text(text = upNavigationItems[0].title) },
                        selected = (selectedIndex == upNavigationItems[0].badgeCount),
                        onClick = {
                            selectIndex(upNavigationItems[0].badgeCount)
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            changeScreenType(MainScreenType.Notes)
                        },
                        icon = {
                            Icon(
                                imageVector = if (selectedIndex == upNavigationItems[0].badgeCount) {
                                    upNavigationItems[0].selectedIcon
                                } else upNavigationItems[0].unselectedIcon,
                                contentDescription = upNavigationItems[0].title
                            )
                        },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text(text = upNavigationItems[1].title) },
                        selected = (selectedIndex == upNavigationItems[1].badgeCount),
                        onClick = {
                            selectIndex(upNavigationItems[1].badgeCount)
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            changeScreenType(MainScreenType.Reminder)
                        },
                        icon = {
                            Icon(
                                imageVector = if (selectedIndex == upNavigationItems[1].badgeCount) {
                                    upNavigationItems[1].selectedIcon
                                } else upNavigationItems[1].unselectedIcon,
                                contentDescription = upNavigationItems[1].title
                            )
                        },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    HorizontalDivider(modifier = Modifier.padding(top = 15.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                        ,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        NavigationDrawerTextButton(text = "Ярлыки", onClick = {})
                        NavigationDrawerTextButton(text = "Изменить", onClick = onEditTags)
                    }

                    tagNavigationItems.forEach {
                        NavigationDrawerItem(
                            label = { Text(text = it.title) },
                            selected = (selectedIndex == it.badgeCount),
                            onClick = {
                                onTagUiSelected(
                                    TagUi(
                                        id = it.tagId ?: "",
                                        name = it.title
                                    )
                                )
                                selectIndex(it.badgeCount)
                            },
                            icon = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.label_24dp_e8eaed_fill0_wght400_grad0_opsz24), contentDescription = "Tag ${it.title}")},
                            modifier = Modifier
                                .padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    HorizontalDivider(Modifier.padding(vertical = 10.dp))

                    NavigationDrawerItem(
                        label = { Text(text = "Создать ярлык") },
                        selected = false,
                        onClick = {
                            onEditTags()
                            coroutineScope.launch {
                                drawerState.close()
                            }
                        },
                        icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Create Tag")},
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    bottomItems.forEach {
                        NavigationDrawerItem(
                            label = { Text(text = it.title) },
                            selected = (selectedIndex == it.badgeCount),
                            onClick = {
                                when(it.badgeCount) {
                                    // archive
                                    UP_NAVIGATION_ITEMS_SIZE + tags.size -> {
                                        changeScreenType(MainScreenType.Archive)
                                    }
                                    // bucket
                                    UP_NAVIGATION_ITEMS_SIZE + tags.size + 1 -> {
                                        changeScreenType(MainScreenType.Bucket)
                                    }
                                    // settings
                                    UP_NAVIGATION_ITEMS_SIZE + tags.size + 2 -> {
                                        onSettingsClicked()
                                    }

                                    // help
                                    UP_NAVIGATION_ITEMS_SIZE + tags.size + 3 -> {
                                        onHelpClicked()
                                    }

                                    else -> {}
                                }
                                selectIndex(it.badgeCount)
                                coroutineScope.launch {
                                    drawerState.close()
                                }
                            },
                            modifier = Modifier
                                .padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        },
        gesturesEnabled = true
    ) {
        when(mainScreenType) {
            MainScreenType.Notes -> {
                NotesScreen(
                    notes = notes.filter { !it.isArchived && !it.isDeleted },
                    onAddNote = onAddNote,
                    onNoteClick = onNoteClick,
                    onNavigationIconClicked = {
                        coroutineScope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            }
            MainScreenType.ByTag -> {
                NotesByTagScreen(
                    onNavigationIconClicked = {
                        coroutineScope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                    onFABClicked = onAddNote,
                    notes = notes.filter { it.tags.contains(selectedTagUi) },
                    onNoteClicked = onNoteClick,
                    tagUi = selectedTagUi
                )
            }
            MainScreenType.Bucket -> {
                BucketScreen(
                    onNavigationIconClicked = { /*TODO*/ },
                    onFABClicked = { /*TODO*/ },
                    onNoteClicked = {},
                    notes = notes.filter { it.isDeleted }
                )
            }
            MainScreenType.Archive -> {

            }
            MainScreenType.Reminder -> {

            }
        }
    }
}

@Composable
fun NavigationDrawerTextButton(
    text: String,
    onClick: () -> Unit
) {
    Box {
        Text(
            modifier = Modifier
                .clickable { onClick() }
                .padding(vertical = 10.dp, horizontal = 5.dp)
            , text = text
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainNavigationDrawerPreview() {
    PetProjectTheme {
        MainNavigationDrawer(
            notes = emptyList(),
            onAddNote = {},
            onNoteClick = {},
            tags = listOf(
                TagUi(name = "ha"),
                TagUi(name = "wa")
            ),
            drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
            onEditTags = {},
            mainScreenType = MainScreenType.ByTag,
            changeScreenType = {},
            selectedTagUi = TagUi(),
            coroutineScope = rememberCoroutineScope(),
            onSettingsClicked = {},
            onHelpClicked = {},
            selectedIndex = 0,
            selectIndex = {},
            onTagUiSelected = {}
        )
    }
}