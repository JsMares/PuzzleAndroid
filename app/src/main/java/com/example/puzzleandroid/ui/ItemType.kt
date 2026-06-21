package com.example.puzzleandroid.ui

import androidx.compose.ui.graphics.Color
import com.example.puzzleandroid.R

enum class ItemType {
    BLUE,
    GREEN,
    PINK,
    RED,
    EMPTY
}

fun ItemType.toColor() : Color {
    return when (this) {
        ItemType.BLUE -> Color.Blue
        ItemType.GREEN -> Color.Green
        ItemType.PINK -> Color.Magenta
        ItemType.RED -> Color.Red
        ItemType.EMPTY -> Color.White
    }
}

fun ItemType.toImage() : Int? {
    return when (this) {
        ItemType.BLUE -> R.drawable.moon
        ItemType.GREEN -> R.drawable.bubble
        ItemType.PINK -> R.drawable.rabbit
        ItemType.RED -> R.drawable.scorpion
        ItemType.EMPTY -> null
    }
}