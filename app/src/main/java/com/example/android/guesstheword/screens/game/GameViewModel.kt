package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    // The current word, MutableLiveData tipo String para que el fragment pueda observar
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get()= _word

    // The current _score, MutableLiveData para operaciones internas tipo Int y un score LiveData para que observe el Fragment
    private val _score = MutableLiveData<Int>()
        //Create a _score to use internal the viewModel, not modificable from the fragment, and a copi of _score
        // "score" that is LiveData, for being observable by the fragment but not modificable
    val score: LiveData<Int>
        get()= _score

    // un "swich" para que el fragmen pueda observar si termino o no, y en base a eso pasar de fragment
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>


    //For the timerc

    private var timer: CountDownTimer
    private val _currentTime = MutableLiveData<Long>()

    val currentTime: LiveData<Long>
        get() = _currentTime

//    //this is a transformation of _currentTime LONG tu currentTime to String for the view
//    val currentTimeString = Transformations.map(_currentTime, {time ->
//        DateUtils.formatElapsedTime(time)
//    })



    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 60000L
    }







    init {
        Log.i("GameViewModel", "GameViewModel Created")

        //Al iniciar, resetea la lista de palabras, setea la primer palabra,  pone el Score en 0, y pone el swich "eventGameFinish" en false
        resetList()
        nextWord()

        _score.value = 0

        _eventGameFinish.value = false

        //For the timer
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
               _currentTime.value = (millisUntilFinished / ONE_SECOND)
            }

            override fun onFinish() {
                _currentTime.value = DONE
                _eventGameFinish.value = true
            }
        }

        timer.start()

    }



    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel Destroyed")
        timer.cancel()

    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    //Moves to the next word in the list
     private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            _eventGameFinish.value = true
        } else {
            _word.value = wordList.removeAt(0)

        }}

        // Methods for buttons presses
        fun onSkip() {
            _score.value = (score.value)?.minus(1)
            nextWord()
        }

        fun onCorrect() {
            _score.value = (score.value)?.plus(1)
            nextWord()
        }

        fun onGameFinishComplete() {
            _eventGameFinish.value = false
        }

}