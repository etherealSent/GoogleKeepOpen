package com.example.petproject.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.SubcomposeLayout
import com.example.petproject.presentation.model.NoteUi
import com.example.petproject.presentation.sharedUi.Note

//@Composable
//fun NoteGrid(
//    notes: List<NoteUi>
//) {
//    SubcomposeLayout { constraints ->
//        val notesMeasurement = notes.map { note ->
//            subcompose(note) { Note(noteUi = note, isSelected = false) }.first().measure(constraints)
//        }
//
//        layout(
//    }
//}