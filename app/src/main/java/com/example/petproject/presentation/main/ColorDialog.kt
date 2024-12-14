package com.example.petproject.presentation.main

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petproject.R
import com.example.petproject.ui.theme.PetProjectTheme

@Composable
fun ColorDialog(
    onDismissRequest: () -> Unit,
    onColorPicked: (Color) -> Unit,
    pickedColor: Color = MaterialTheme.colorScheme.background
) {
    val defaultBackground = MaterialTheme.colorScheme.background
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text("Выберете цвет")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(modifier = Modifier.padding(bottom = 5.dp)) {
                    ColorCircle(
                        modifier = Modifier.padding(end = 5.dp),
                        picked = (pickedColor == defaultBackground),
                        color = defaultBackground,
                        onColorPicked = {
                            onColorPicked(defaultBackground)
                        }
                    )
                    ColorCircle(
                        modifier = Modifier.padding(end = 5.dp),
                        picked = (pickedColor == Color(0xFF77172F)),
                        color = Color(0xFF77172F),
                        onColorPicked = {
                            onColorPicked(Color(0xFF77172F))
                        }
                    )
                    ColorCircle(
                        modifier = Modifier.padding(end = 5.dp),
                        picked = (pickedColor == Color(0xFF672C19)),
                        color = Color(0xFF672C19),
                        onColorPicked = {
                            onColorPicked(Color(0xFF672C19))
                        }
                    )
                    ColorCircle(
                        modifier = Modifier.padding(end = 5.dp),
                        picked = (pickedColor == Color(0xFF7C4A02)),
                        color = Color(0xFF7C4A02),
                        onColorPicked = {
                            onColorPicked(Color(0xFF7C4A02))
                        }
                    )
                }
                Row(modifier = Modifier.padding(bottom = 5.dp)) {
                    ColorCircle(
                        modifier = Modifier.padding(end = 5.dp),
                        picked = (pickedColor == Color(0xFF284254)),
                        color = Color(0xFF284254),
                        onColorPicked = {
                            onColorPicked(Color(0xFF284254))
                        }
                    )
                    ColorCircle(
                        modifier = Modifier.padding(end = 5.dp),
                        picked = (pickedColor == Color(0xFF226377)),
                        color = Color(0xFF226377),
                        onColorPicked = {
                            onColorPicked(Color(0xFF226377))
                        }
                    )
                    ColorCircle(
                        modifier = Modifier.padding(end = 5.dp),
                        picked = (pickedColor == Color(0xFF0F625C)),
                        color = Color(0xFF0F625C),
                        onColorPicked = {
                            onColorPicked(Color(0xFF0F625C))
                        }
                    )
                    ColorCircle(
                        modifier = Modifier.padding(end = 5.dp),
                        picked = (pickedColor == Color(0xFF274D3C)),
                        color = Color(0xFF274D3C),
                        onColorPicked = {
                            onColorPicked(Color(0xFF274D3C))
                        }
                    )
                }
                Row {
                    ColorCircle(
                        modifier = Modifier.padding(end = 5.dp),
                        picked = (pickedColor == Color(0xFF482E5B)),
                        color = Color(0xFF482E5B),
                        onColorPicked = {
                            onColorPicked(Color(0xFF482E5B))
                        }
                    )
                    ColorCircle(
                        modifier = Modifier.padding(end = 5.dp),
                        picked = (pickedColor == Color(0xFF6C3A4F)),
                        color = Color(0xFF6C3A4F),
                        onColorPicked = {
                            onColorPicked(Color(0xFF6C3A4F))
                        }
                    )
                    ColorCircle(
                        modifier = Modifier.padding(end = 5.dp),
                        picked = (pickedColor == Color(0xFF4D4439)),
                        color = Color(0xFF4D4439),
                        onColorPicked = {
                            onColorPicked(Color(0xFF4D4439))
                        }
                    )
                    ColorCircle(
                        modifier = Modifier.padding(end = 5.dp),
                        picked = (pickedColor == Color(0xFF232428)),
                        color = Color(0xFF232428),
                        onColorPicked = {
                            onColorPicked(Color(0xFF232428))
                        }
                    )
                }
            }
        },
        confirmButton = { /*TODO*/ },
        dismissButton = { /*TODO*/ }
    )
}

@Composable
fun ColorCircle(
    picked: Boolean,
    color: Color,
    onColorPicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (picked) {
        Box(
            modifier = modifier.clickable { onColorPicked() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(color, CircleShape)
                    .border(1.dp, Color(0xFF96CFF0), CircleShape)
            )
            Icon(
                modifier = Modifier.size(36.dp),
                imageVector = ImageVector.vectorResource(R.drawable.check_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                contentDescription = null,
                tint = Color(0xFF96CFF0)
            )
        }
    } else {
        Box(
            modifier = modifier.clickable { onColorPicked() }
                .size(50.dp)
                .background(color, CircleShape)
                .border(1.dp, Color(0xFF607380), CircleShape)
        )
    }
}

@Preview
@Composable
fun PickedColorCirclePreview() {
    ColorCircle(true, Color(0xFF77172F), onColorPicked = {})
}

@Preview
@Composable
fun ColorCirclePreview() {
    ColorCircle(false, Color(0xFF77172F), onColorPicked = {})
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ColorDialogPreview() {
    PetProjectTheme {
        ColorDialog({}, {})
    }
}