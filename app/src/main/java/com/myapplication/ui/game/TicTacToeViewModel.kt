package com.myapplication.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myapplication.ui.home.HomeActivity
import kotlin.random.Random


/*
*
* This viewmodel will do the following things,
* - represent the current state of the game
* - will handle player move
* - will check if game won
* - will reset the game if one of the following conditions are met
*   - if any player wins
*   - if the game is drawn
* */
class TicTacToeViewModel : ViewModel() {

    private val winCombinations = arrayOf(
        intArrayOf(0, 1, 2),
        intArrayOf(3, 4, 5),
        intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6),
        intArrayOf(1, 4, 7),
        intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8),
        intArrayOf(2, 4, 6)
    )

    private val random = Random.Default

   // private var currentPlayer = 1

    private val _gameBoard = MutableLiveData<IntArray>()
    private val _wonOrDrawn = MutableLiveData<GameState>()

    init {
        _gameBoard.value = IntArray(9) { 0 }
        _wonOrDrawn.value = GameState.ONGOING
    }


    val gameBoard: LiveData<IntArray>
        get() = _gameBoard


    // val currentPlayer: LiveData<Int>
    //get() = currentPlayer

    val wonOrDrawn: LiveData<GameState>
        get() = _wonOrDrawn


    fun userClicked(position: Int, gameMode: String) {

        _gameBoard.value?.let { curBoard ->

            //If the slot is empty in the game state board, then fill with the current player
            if (curBoard[position] == 0) {

                //val currentPlayerV = currentPlayer.value ?: 1

                play(position, HUMAN)

                if(_wonOrDrawn.value == GameState.ONGOING) {
                    play(bestPosition(curBoard, gameMode), AI)
                }
            }

        }
    }



    private fun play(position: Int, player: Int) {

        _gameBoard.value?.let { curBoard ->

            curBoard[position] = player

            _gameBoard.value = curBoard
            _wonOrDrawn.value = checkGameStatus(curBoard, player)

        }
    }

    private fun bestPosition(board: IntArray, gameMode: String): Int {
        return if(gameMode == HomeActivity.HARD_MODE) {
            calculateBestPosition(board)
        } else {
            calculateRandomPosition(board)
        }
    }


    //The fun stuff, using minimax algorithm
    private fun calculateBestPosition(board: IntArray): Int {
        val pos =  minimax(board, AI).index
        Log.d("TictactoeViewModel", "pos by minimax is $pos")
        return pos
    }


    //We will evaluate the board, and maximize for the
    private fun minimax(board: IntArray, player: Int): Move {
        val emptySlots = getEmptySlots(board)

        when {
            checkGameStatus(board, HUMAN) == GameState.PLAYER_HUMAN -> return Move(score = -10)
            checkGameStatus(board, AI) == GameState.PLAYER_AI -> return Move(score = 10)
            emptySlots.size == 0 -> return Move(score = 0)
        }


        val possMoves = mutableListOf<Move>()

        //Evaluate score for every possible move left in the board
        for(i in emptySlots.indices) {
            val move = Move()

            move.index = emptySlots[i] //board[emptySlots[i]]

            //Take turn in stead of current player, and call minimax for other player
            board[emptySlots[i]] = player

            if(player == HUMAN) {
                val result = minimax(board, AI)
                move.score = result.score
            } else {
                val result = minimax(board, HUMAN)
                move.score = result.score
            }

            //Undo the move made in stead of current player
            board[emptySlots[i]] = 0

            possMoves.add(move)

        }

        //For all the moves generated using this previous loop, we calculate the best possible one for the AI
        var bestMove = 0
        if(player == AI) {
            var bestScore = -10000

            for(i in possMoves.indices) {
                if(possMoves[i].score > bestScore) {
                    bestScore = possMoves[i].score
                    bestMove = i
                }
            }
        } else {
            var bestScore = 10000

            for(i in possMoves.indices) {
                if(possMoves[i].score < bestScore) {
                    bestScore = possMoves[i].score
                    bestMove = i
                }
            }
        }

        /*possMoves.forEach {
            Log.d("TictactoeViewModel", "Possible move is $it")
        }

        Log.d("TictactoeViewModel", "Possible best move is $bestMove")*/

        return possMoves[bestMove]
    }


    private fun calculateRandomPosition(board: IntArray): Int {

        val emptySlotList : MutableList<Int> = getEmptySlots(board)

        val id = random.nextInt(emptySlotList.size)

        return emptySlotList[id]
    }


    private fun getEmptySlots(board: IntArray): MutableList<Int> {
        val emptySlotList : MutableList<Int> = mutableListOf()

        for(i in board.indices) {
            if(board[i] == 0) {
                emptySlotList.add(i)
            }
        }
        return emptySlotList
    }


    private fun checkGameStatus(board: IntArray, player: Int): GameState {


        winCombinations.forEach { combo ->
            if (combo.all { i -> board[i] == player }) {
                return if (player == 1) GameState.PLAYER_HUMAN else GameState.PLAYER_AI
            }
        }

        //If control comes here, game must either be drawn or still ongoing
        return if (board.any { i -> i == 0 }) {
            GameState.ONGOING
        } else {
            GameState.DRAWN
        }

    }

    fun resetGame() {

        val reset = IntArray(9) {
            0
        }

        _gameBoard.postValue(reset)
        _wonOrDrawn.postValue(GameState.ONGOING)
        //currentPlayer = 1

    }


    data class Move(var score: Int = 0, var index: Int = -1)

    companion object {
        private const val HUMAN = 1
        private const val AI = 2
    }


}

enum class GameState {
    ONGOING, PLAYER_HUMAN, PLAYER_AI, DRAWN
}
