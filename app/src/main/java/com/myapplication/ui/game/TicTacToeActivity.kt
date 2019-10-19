package com.myapplication.ui.game

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myapplication.R
import com.myapplication.ui.home.HomeActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TicTacToeActivity : AppCompatActivity() {


    //private val tag = "TictactoeActivity"

    private val boardClickListener: (position: Int) -> Unit = {
       // Working fine Log.d(tag, "board click listener called???")
        ticTacToeViewModel.userClicked(it, gameMode)
    }

    private val ticTacToeViewModel: TicTacToeViewModel by viewModel()

    private val boardAdapter: BoardAdapter by inject {
        parametersOf(boardClickListener)
    }


    //Textview
    private lateinit var playerTurnText: TextView

    //RecyclerView
    private lateinit var gameBoard: RecyclerView

    private lateinit var gameMode: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tictactoe)

        gameMode = intent.getStringExtra(HomeActivity.GAME_MODE) ?: HomeActivity.EASY_MODE

        bindViews()

        setupViewModel()

        setupBoard()
    }



    private fun bindViews() {
        playerTurnText = findViewById(R.id.player_turn_text)

        gameBoard = findViewById(R.id.main_game_board)
    }



    private fun setupViewModel() {
        ticTacToeViewModel.gameBoard.observe(this, Observer {
            it?.let { state ->
                boardAdapter.submitNewBoard(state)
            }
        })

        /*ticTacToeViewModel.currentPlayer.observe(this, Observer {
            it?.let { cur ->
                playerTurnText.text = getString(R.string.player_x, cur)
            }
        })*/

        ticTacToeViewModel.wonOrDrawn.observe(this, Observer {
            when(it) {
                GameState.PLAYER_HUMAN -> {
                    showToast("You won!")
                    ticTacToeViewModel.resetGame()
                }
                GameState.PLAYER_AI -> {
                    showToast("You lost!")
                    ticTacToeViewModel.resetGame()
                }
                GameState.DRAWN -> {
                    showToast("Game drawn")
                    ticTacToeViewModel.resetGame()
                }
                else -> {}
            }


        })

    }




    private fun setupBoard() {

        gameBoard.layoutManager = GridLayoutManager(this, 3)
        gameBoard.adapter = boardAdapter

    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

}
