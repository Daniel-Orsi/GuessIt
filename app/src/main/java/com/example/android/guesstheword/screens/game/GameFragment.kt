/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding


/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    //inicializar ViewModel
    lateinit var gameViewModel: GameViewModel

    //inicializar el objeto "gameBinding" que tiene referencia a todas las views
    lateinit var gameBinding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        gameBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        //"gameViewModel=" Create the ViewModel of this fragment (game Fragment)
        //ViewModelProviders crea una instancia del viewModel asociado (el que esta atras del .get)
        //lo asocia al "this" que en este caso es el Fragment
        gameViewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)
        Log.i("GameViewModel", "Called ViewModelProviders.of")

        //seteo que el bindingObject este linkeado con el gameViewModel para poder interactuar XML a GameViewModel
        gameBinding.gameViewModel = gameViewModel

        //alow to use LiveData to automatic update by databinding layouts instead of "gameViewModel.word.observe(viewLifecycleOwner, Observer{ newWord ->
        //            gameBinding.wordText.text = newWord"
        gameBinding.setLifecycleOwner(this)


        gameViewModel.currentTime.observe(viewLifecycleOwner, Observer { newTime ->
            gameBinding.timerText.text = DateUtils.formatElapsedTime(newTime)
        })


        gameViewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { hasFinished ->
            if (hasFinished) {
                gameFinished()
                gameViewModel.onGameFinishComplete()
            }

        })


        return gameBinding.root

    }


    //Called when the game is finished, si bien es una decision, se deja en el fragment porque tiene referencia
    //al navControler y eso el viewModel no lo tiene

    private fun gameFinished() {
        val currentScore = gameViewModel.score.value
                ?: 0 //the Elvis operator return 0 if score.value is null
        val action = GameFragmentDirections.actionGameToScore(currentScore)
        findNavController(this).navigate(action)
    }


}
