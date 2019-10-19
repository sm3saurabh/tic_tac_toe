package com.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class HomeViewModel : ViewModel() {

    private val titles = arrayOf("Tic", "Tac", "Toe", "Tic-Tac-Toe")

    private var counter = 0

    private val observableTitle = MutableLiveData<String>()


    private val job = SupervisorJob()

    private val homeScope = CoroutineScope(job + Dispatchers.Main)

    init {
        delayAndChangeTitle()
    }

    private fun delayAndChangeTitle() = homeScope.launch {

        //Delay for 1 second, and then change the title, keep doing it repeatedly
        while (true) {
            withContext(Dispatchers.Default) { delay(1000) }

            observableTitle.postValue(titles[counter])
            counter = (counter + 1) % titles.size
        }

    }

    fun getTitle() : LiveData<String> = observableTitle





    override fun onCleared() {
        job.cancel()
    }


}