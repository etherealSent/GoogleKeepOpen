package com.example.petproject.presentation.sharedUi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petproject.R
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TooltipIconButton(
    tooltipText: String,
    iconContentDescription: String,
    iconResource: Int,
    tooltipModifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val tooltipState = rememberTooltipState()
    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(),
        tooltip = {
            PlainTooltip(modifier = tooltipModifier) { Text(text = tooltipText, fontSize = 14.sp, modifier = Modifier.padding(4.dp)) }
                  },
        state = tooltipState
    ) {
        IconButton(onClick = onClick) {
            Icon(imageVector = ImageVector.vectorResource(id = iconResource), contentDescription = iconContentDescription)
        }
    }
}

@Preview
@Composable
fun TooltipIconButtonPreview() {
    Scaffold {
        Column(Modifier.padding(it)) {
            TooltipIconButton(
                tooltipText = "Один столбец",
                iconContentDescription = "Один столбец",
                iconResource = R.drawable.splitscreen_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                tooltipModifier = Modifier.padding(start = 60.dp),
                onClick = {}
            )
        }
    }
}