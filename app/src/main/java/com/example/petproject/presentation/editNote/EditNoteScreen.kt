package com.example.petproject.presentation.editNote

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key.Companion.I
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petproject.R
import com.example.petproject.ui.theme.PetProjectTheme

@Composable
fun EditNoteScreen(
    title: String,
    onTitleChanged: (String) -> Unit
) {
    Scaffold(
        topBar = {
            EditNoteTopBar()
        },
        bottomBar = {
            EditNoteBottomBar()
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TextField(
                value = title,
                onValueChange = onTitleChanged,
                placeholder = { Text("Название", style = MaterialTheme.typography.headlineMedium) },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                )
            )
            TextField(
                modifier = Modifier.offset(y=-20.dp),
                value = title,
                onValueChange = onTitleChanged,
                placeholder = { Text("Текст", style = MaterialTheme.typography.bodyLarge) },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteTopBar() {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = {}
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "go back")
            }
        },
        actions = {
            IconButton(
                onClick = {}
            ) {
                Icon(ImageVector.vectorResource(R.drawable.keep_24dp_5f6368_fill0_wght400_grad0_opsz24), contentDescription = "pin note")
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
fun EditNoteBottomBar() {
    BottomAppBar(
        modifier = Modifier.background(Color(0xFF)),
        actions = {
            IconButton(
                onClick = {}
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
            Text(text = "Изменено 13:47", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {}
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "more actions")
            }
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditNoteScreenPreview() {
    PetProjectTheme {
        EditNoteScreen("", {})
    }
}
