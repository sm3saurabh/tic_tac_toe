package com.myapplication.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.myapplication.R
import com.myapplication.ui.game.TicTacToeActivity
import com.myapplication.utils.takeMeThere
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity() {

    private lateinit var titleView: TextView

    private val homeViewModel: HomeViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bindViews()

        //setupSwitcher()

        processViewModel()
    }


    private fun bindViews() {
        titleView = findViewById(R.id.home_title)

        val hardModeButton = findViewById<Button>(R.id.home_selector_hard)

        hardModeButton.setOnClickListener {
            takeMeThere<TicTacToeActivity>(
                iWillBeNoMore = false,
                bundle = Bundle().apply {
                    putString(GAME_MODE, HARD_MODE)
                }
            )
        }

        val easyModeButton = findViewById<Button>(R.id.home_selector_easy)

        easyModeButton.setOnClickListener {
            takeMeThere<TicTacToeActivity>(
                iWillBeNoMore = false,
                bundle = Bundle().apply {
                    putString(GAME_MODE, EASY_MODE)
                }
            )
        }

    }

    /*private fun setupSwitcher() {
        titleSwitcher.setFactory {

            val switchView = TextView(this)

            switchView.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                switchView.setTextAppearance(R.style.HomeTitleAppearance)
            }

            switchView
        }

        titleSwitcher.setInAnimation(this, android.R.anim.fade_in)
        titleSwitcher.setOutAnimation(this, android.R.anim.fade_out)

        titleSwitcher.setCurrentText("A Game")
    }*/

    private fun processViewModel() {

        homeViewModel.getTitle().observe(this, Observer {

            if(!it.isNullOrEmpty()) {
                titleView.text = it
            }

        })
    }


    companion object {
        const val HARD_MODE = "Hard"
        const val EASY_MODE = "Easy"
        const val GAME_MODE = "Game Mode"
    }
}
