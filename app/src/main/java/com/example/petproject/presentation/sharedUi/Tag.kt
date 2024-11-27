package com.example.petproject.presentation.sharedUi

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petproject.R
import com.example.petproject.presentation.model.TagUi
import com.example.petproject.ui.theme.PetProjectTheme

@Composable
fun Tag(
    modifier: Modifier = Modifier,
    name: String
) {
    Text(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = Color(0xFF40464A),
                shape = RoundedCornerShape(12.dp)
            )
            .background(Color(0xFF3D4548))
            .padding(vertical = 4.dp, horizontal = 8.dp),
        text = name,
        color = Color(0xFFBAC0C7)
    )
}

@Composable
fun AndMoreTag(
    moreTagsCount: Int
) {
    Tag(
        name = stringResource(R.string.and_more_tag, moreTagsCount)
    )
}

@Composable
fun TagRow(
    tags: List<TagUi>
) {
    SubcomposeLayout { constraints ->

        val andMoreTagMeasurement = subcompose("AndMoreTag") { AndMoreTag(tags.size - 1) }.first().measure(constraints)

        val tagsMeasurement = tags.map { tag ->
            subcompose(tag) { Tag(name = tag.name) }.first().measure(constraints)
        }

        val stack = ArrayDeque<Placeable>()

        var xPosition = 0
        val maxWidth = constraints.maxWidth
        var needToAddMoreTag = false

        tagsMeasurement.forEach { measurement ->
            if (xPosition + measurement.width > maxWidth) {
                needToAddMoreTag = true
                return@forEach
            }
            stack.add(measurement)
            xPosition += measurement.width + 8.dp.roundToPx()
        }

        if (needToAddMoreTag) {
            while (andMoreTagMeasurement.width + 8.dp.roundToPx() + xPosition > maxWidth) {
                val removedTagMeasurement = stack.removeLastOrNull()
                if (removedTagMeasurement != null) {
                    xPosition -= removedTagMeasurement.width + 8.dp.roundToPx()
                } else {
                    break
                }
            }
            val countedAndMoreTagMeasurement = subcompose("AndMoreTag2") { AndMoreTag(tags.size - stack.size) }.first().measure(constraints)

            stack.add(countedAndMoreTagMeasurement)
        }

        val height = tagsMeasurement[0].height

        layout(constraints.maxWidth, height) {
            var x2Position = 0
            stack.forEach { measurement ->
                measurement.placeRelative(x = x2Position, y = 0)
                x2Position += measurement.width + 8.dp.roundToPx()
            }
        }

    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TagPreview() {
    PetProjectTheme {
        Tag(name = "Tag")
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AndMoreTagPreview() {
    PetProjectTheme {
        AndMoreTag(5)
    }
}