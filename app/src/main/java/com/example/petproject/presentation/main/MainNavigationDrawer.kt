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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petproject.presentation.model.NoteUi
import com.example.petproject.presentation.model.TagUi
import com.example.petproject.ui.theme.PetProjectTheme
import kotlinx.coroutines.launch
import com.example.petproject.R

data class NavigationItems(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)

@Composable
fun MainNavigationDrawer(
    notes: List<NoteUi>,
    onAddNote: () -> Unit,
    onEditTags : () -> Unit,
    onNoteClick: (NoteUi) -> Unit,
    tags: List<TagUi>,
    drawerState: DrawerState
) {
    val upItems = listOf(
        NavigationItems(
            title = "Заметки",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.lightbulb_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.lightbulb_24dp_e8eaed_fill0_wght400_grad0_opsz24),
        ),
        NavigationItems(
            title = "Напоминания",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.notifications_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.notifications_24dp_e8eaed_fill0_wght400_grad0_opsz24),
        )
    )

    val bottomItems = listOf(
        NavigationItems(
            title = "Архив",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.archive_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.archive_24dp_e8eaed_fill0_wght400_grad0_opsz24),
        ),
        NavigationItems(
            title = "Корзина",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.delete_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.delete_24dp_e8eaed_fill0_wght400_grad0_opsz24),
        ),
        NavigationItems(
            title = "Настройки",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.settings_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.settings_24dp_e8eaed_fill0_wght400_grad0_opsz24),
        ),
        NavigationItems(
            title = "Справка/отзыв",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.help_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.help_24dp_e8eaed_fill0_wght400_grad0_opsz24),
        ),
    )

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    val scope = rememberCoroutineScope()

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
                    upItems.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            label = { Text(text = item.title) },
                            selected = index == selectedItemIndex,
                            onClick = {
                                //  navController.navigate(item.route)
                                selectedItemIndex = index
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            badge = {  // Show Badge
                                item.badgeCount?.let {
                                    Text(text = item.badgeCount.toString())
                                }
                            },
                            modifier = Modifier
                                .padding(NavigationDrawerItemDefaults.ItemPadding) //padding between items
                        )
                    }
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

                    tags.forEachIndexed { index, tag ->
                        NavigationDrawerItem(
                            label = { Text(text = tag.name) },
                            selected = index + upItems.size == selectedItemIndex,
                            onClick = { /*TODO*/ },
                            icon = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.label_24dp_e8eaed_fill0_wght400_grad0_opsz24), contentDescription = "Tag ${tag.name}")}
                        )
                    }

                    NavigationDrawerItem(
                        label = { Text(text = "Создать ярлык") },
                        selected = tags.size + upItems.size == selectedItemIndex,
                        onClick = { /*TODO*/ },
                        icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Create Tag")}
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    HorizontalDivider(Modifier.padding(vertical = 10.dp))
                    bottomItems.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            label = { Text(text = item.title) },
                            selected = index == selectedItemIndex,
                            onClick = {
                                //  navController.navigate(item.route)
                                selectedItemIndex = index
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (index + upItems.size + tags.size + 1 == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            badge = {  // Show Badge
                                item.badgeCount?.let {
                                    Text(text = item.badgeCount.toString())
                                }
                            },
                            modifier = Modifier
                                .padding(NavigationDrawerItemDefaults.ItemPadding) //padding between items
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        },
        gesturesEnabled = true
    ) {
        MainScreen(
            notes = notes,
            onAddNote = onAddNote,
            onNoteClick = onNoteClick,
            onNavigationIconClicked = {
                scope.launch {
                    drawerState.apply {
                       if (isClosed) open() else close()
                    }
                }
            }
        )
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
            onEditTags = {}
        )
    }
}