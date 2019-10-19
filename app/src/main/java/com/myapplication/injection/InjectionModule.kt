package com.myapplication.injection

import com.myapplication.ui.game.BoardAdapter
import com.myapplication.ui.game.TicTacToeViewModel
import com.myapplication.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val gameModule = module {

    viewModel { TicTacToeViewModel() }

    factory {(lambda : (Int) -> Unit) ->
        BoardAdapter(lambda)
    }
}

val homeModule = module {

    viewModel { HomeViewModel() }


}