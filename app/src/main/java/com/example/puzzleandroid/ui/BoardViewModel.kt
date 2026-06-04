package com.example.puzzleandroid.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class BoardViewModel : ViewModel() {
    var board = mutableStateOf(generateBoard())
        private set

    private fun generateBoard() : List<List<Item>> {
        return List(9) {
            List(9) {
                Item(type = ItemType.entries.random())
            }
        }
    }
}