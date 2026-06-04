package com.example.puzzleandroid.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.jvm.internal.Intrinsics.Kotlin

class BoardViewModel : ViewModel() {
    var board by mutableStateOf(generateBoard())
        private set

    var selected by mutableStateOf<SelectedItem?>(null)
        private set

    private fun generateBoard() : List<List<Item>> {
        return List(9) {
            List(9) {
                Item(type = ItemType.entries.random())
            }
        }
    }

    fun clickItem(row: Int, col: Int) {
        // Comprobar que no hay un item seleccionado
        if (selected == null) {
            selected = SelectedItem(row, col)
        } else {
            val first = selected!! // Asignar la posición del primer item seleccionado
            val second = SelectedItem(row, col) // Asignar la posición del segundo item seleccionado

            // Verificar si los items seleccionados son "vecinos"
            if (areNeighbors(first, second)) {
                swap(first, second)
            }

            // Reiniciar el valor del item seleccionado
            selected = null
        }
    }

    private fun areNeighbors(a: SelectedItem, b: SelectedItem) : Boolean {
        val rowDiff = kotlin.math.abs(a.row - b.row)
        val colDiff = kotlin.math.abs(a.col - b.col)

        return (rowDiff + colDiff) == 1
    }

    private fun swap(first: SelectedItem, second: SelectedItem) {
        val newBoard = board.map { it.toMutableList() }.toMutableList()

        // Guardar posición del primer item seleccionado
        val temp = newBoard[first.row][first.col]

        // Asignar posición del segundo item al primer item
        newBoard[first.row][first.col] = newBoard[second.row][second.col]

        // Asignar el primer item guardado en el segundo item
        newBoard[second.row][second.col] = temp

        // Guardar cambios
        board = newBoard
    }
}

data class SelectedItem(
    val row: Int,
    val col: Int
)