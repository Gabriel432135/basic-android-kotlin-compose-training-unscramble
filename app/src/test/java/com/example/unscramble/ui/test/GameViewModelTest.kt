package com.example.unscramble.ui.test

import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.getUnscrambledWord
import com.example.unscramble.ui.GameViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertNotEquals
import org.junit.Test

class GameViewModelTest {
    private val viewModel = GameViewModel()

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset()  {
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiState.value
        //A propriedade isGuessedWordWrong deve ser setada para false, pois o usuário acertou a palavra.
        assertFalse(currentGameUiState.isGuessedWordWrong)
        //A propriedade score deve ser atualizada.
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)

    }

    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet() {
        val incorrectPlayerWord = "and"

        viewModel.updateUserGuess(incorrectPlayerWord)
        viewModel.checkUserGuess()

        val currentGameUiState = viewModel.uiState.value

        assertEquals(0, currentGameUiState.score)
        assertTrue(currentGameUiState.isGuessedWordWrong)
    }

    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        val gameUiState = viewModel.uiState.value
        val unScrambledWord = getUnscrambledWord(gameUiState.currentScrambledWord)
        // Confirma que a palavra está embaralhada
        assertNotEquals(unScrambledWord, gameUiState.currentScrambledWord)
        // Confirma que o jogo começa com a primeira palavra
        assertTrue(gameUiState.currentWordCount == 1)
        // Confirma que o score começa em 0
        assertTrue(gameUiState.score == 0)
        // Confirma que o erro começa em false
        assertFalse(gameUiState.isGuessedWordWrong)
        // Confirma que o jogo não está finalizado
        assertFalse(gameUiState.isGameOver)
        // Confirma que o usuário ainda não digitou nenhuma palavra
        assertEquals("", viewModel.userGuess)
    }


    companion object{
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }
}