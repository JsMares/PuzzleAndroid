package com.example.puzzleandroid.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class BoardViewModel : ViewModel() {
    var board by mutableStateOf(generateBoard())
        private set

    var selected by mutableStateOf<SelectedItem?>(null)
        private set

    private val _movements = mutableIntStateOf(24)
    val movements: State<Int> = _movements

    private val _game = mutableStateOf(true)
    val game: MutableState<Boolean> = _game

    init {
        resolveCascades()
    }

    private fun generateBoard() : List<List<Item>> {
        return List(9) {
            List(9) {
                Item(type = ItemType.entries
                    .filter { it != ItemType.EMPTY }
                    .random())
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

                val matches = findMatches()

                if (matches.isEmpty()) {
                    swap(first, second)
                } else {
                    resolveCascades()
                    decreaseMovements()
                }
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

    private fun iterateRows() : List<SelectedItem> {
        val allMatches = mutableListOf<SelectedItem>()

        board.forEachIndexed { rowIndex, row ->
            val actualMatches = mutableListOf<SelectedItem>()

            var count = 1
            var lastItem = row[0].type

            actualMatches.add(SelectedItem(rowIndex, 0))

            for (colIndex in 1 until row.size) {
                if (lastItem == row[colIndex].type && lastItem != ItemType.EMPTY) {
                    count++
                    actualMatches.add(SelectedItem(rowIndex, colIndex))
                } else {
                    if (count >= 3) {
                        allMatches.addAll(actualMatches)
                    }

                    actualMatches.clear()

                    count = 1
                    lastItem = row[colIndex].type
                    actualMatches.add(SelectedItem(rowIndex, colIndex))
                }
            }

            if (count >= 3) {
                allMatches.addAll(actualMatches)
            }
        }

        return allMatches
    }

    private fun iterateColumns() : List<SelectedItem> {
        val allMatches = mutableListOf<SelectedItem>()

        // Recorrer la primera fila del tablero
        for (colIndex in board[0].indices) {
            val actualMatches = mutableListOf<SelectedItem>()

            var count = 1
            var lastItem = board[0][colIndex].type

            actualMatches.add(SelectedItem(0, colIndex))

            for (rowIndex in 1 until board.size) {
                val actualItem = board[rowIndex][colIndex].type

                if (actualItem == lastItem && lastItem != ItemType.EMPTY) {
                    count++
                    actualMatches.add(SelectedItem(rowIndex, colIndex))
                } else {
                    if (count >= 3) {
                        allMatches.addAll(actualMatches)
                    }

                    actualMatches.clear()

                    count = 1
                    lastItem = actualItem
                    actualMatches.add(SelectedItem(rowIndex, colIndex))
                }
            }

            if (count >= 3) {
                allMatches.addAll(actualMatches)
            }
        }

        return allMatches
    }

    private fun findMatches() : List<SelectedItem> {
        return (iterateRows() + iterateColumns()).distinct()
    }

    private fun removeMatches(matches: List<SelectedItem>) {
        val newBoard = board.map { it.toMutableList() }.toMutableList()

        /*matches.forEach { match ->
            newBoard[match.row][match.col] = Item(ItemType.entries
                .filter { it != ItemType.EMPTY }
                .random())
        }*/

        matches.forEach { match ->
            newBoard[match.row][match.col] = Item(ItemType.EMPTY)
        }

        /*matches.forEach { match ->
            newBoard[match.row][match.col] = Item(ItemType.EMPTY)
        }*/

        board = newBoard
    }

    private fun resolveCascades() {
        var matches = findMatches()

        while (matches.isNotEmpty()) {
            removeMatches(matches)

            matches = findMatches()
        }
    }

    private fun decreaseMovements() {
        _movements.intValue -= 1

        if (_movements.intValue == 0) {
            endGame()
        }
    }

    fun playGame() {
        _game.value = true
        _movements.intValue = 24
    }

    private fun endGame() {
        _game.value = false
    }
}

data class SelectedItem(
    val row: Int,
    val col: Int
)