package com.myapplication.ui.game

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myapplication.R
import com.myapplication.utils.inflate

class BoardAdapter(
    private val boardCellClickListener: (position: Int) -> Unit
    ) : RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {


    private var gameBoard: IntArray = intArrayOf()


    fun submitNewBoard(newBoard: IntArray) {
        gameBoard = newBoard
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        return BoardViewHolder(parent.inflate(R.layout.game_board_cell))
    }

    override fun getItemCount(): Int {
        return gameBoard.size
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {

        val value = gameBoard[position]

        holder.boardCellText.text = if (value == 1) "x" else if (value == 2) "o" else ""

        holder.boardCellText.setOnClickListener {
            boardCellClickListener(position)
        }
    }


    inner class BoardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val boardCellText: TextView = view.findViewById(R.id.board_cell)


        /*init {




        }*/

        /*fun bind(value: Int) {

            boardCellText.text = if (value == 1) "x" else if (value == 2) "o" else ""
        }*/

    }


}
