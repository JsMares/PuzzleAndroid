package com.example.puzzleandroid.ui

import androidx.compose.ui.graphics.Color

enum class ItemType {
    BLUE,
    GREEN,
    PINK,
    RED
}

fun ItemType.toColor() : Color {
    return when (this) {
        ItemType.BLUE -> Color.Blue
        ItemType.GREEN -> Color.Green
        ItemType.PINK -> Color.Magenta
        ItemType.RED -> Color.Red
    }
}