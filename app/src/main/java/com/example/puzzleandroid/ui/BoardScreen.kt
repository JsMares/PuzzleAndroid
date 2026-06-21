package com.example.puzzleandroid.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Preview(showBackground = true)
@Composable
fun BoardScreen(boardViewModel: BoardViewModel = viewModel()) {
    LaunchedEffect(true) {
        //boardViewModel.iterateColumns()
    }

    val movements by boardViewModel.movements

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        val itemSize = maxWidth / 9

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ControlCustom(movements = movements)

            GameBoard(
                boardViewModel = boardViewModel,
                itemSize = itemSize
            )


        }
    }
}

@Composable
private fun GameBoard(
    boardViewModel: BoardViewModel,
    itemSize: Dp
) {
    val board = boardViewModel.board

    Column {
        board.forEachIndexed { rowIndex, row ->
            Row {
                row.forEachIndexed { colIndex, item ->
                    val isSelected =
                        boardViewModel.selected?.row == rowIndex && boardViewModel.selected?.col == colIndex

                    Box(
                        modifier = Modifier
                            .size(itemSize)
                            .clip(
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(2.dp)
                            .background(item.type.toColor().copy(alpha = 0.6f))
                            .border(
                                width = if (isSelected) 3.dp else 0.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                boardViewModel.clickItem(rowIndex, colIndex)
                            }
                    ) {
                        Image(
                            painter = painterResource(item.type.toImage()),
                            contentDescription = null,
                            modifier = Modifier.padding(2.dp)
                        )
                    }
                }
            }
        }
    }
}