package com.myapplication

import android.app.Application
import com.myapplication.injection.gameModule
import com.myapplication.injection.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TicTacToe: Application() {

    override fun onCreate() {
        super.onCreate()


        startKoin {
            androidLogger()

            //For the heck of it
            androidContext(this@TicTacToe)

            modules(listOf(gameModule, homeModule))
        }
    }

}