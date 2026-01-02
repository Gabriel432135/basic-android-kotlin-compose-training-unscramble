package com.example.unscramble.ui.test

import com.example.unscramble.data.MAX_NO_OF_WORDS
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

        // Confirma que o score continua como zero
        assertEquals(0, currentGameUiState.score)

        // Confirma que o erro está setado como true
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

    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly() {
        var expectedScore = 0
        var currentGameUiState = viewModel.uiState.value
        var correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        repeat(MAX_NO_OF_WORDS) {
            expectedScore += SCORE_INCREASE
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()

            currentGameUiState = viewModel.uiState.value
            correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

            // Confirma que depois de cada resposta correta, o score aumenta conforme o esperado
            assertEquals(expectedScore, currentGameUiState.score)
        }
        // Confirma que após o número máximo de respostas, o número de palavras é setado para MAX_NO_OF_WORDS
        assertEquals(MAX_NO_OF_WORDS, currentGameUiState.currentWordCount)

        // Confirma que depois de 10 respostas corretas, o jogo está finalizado
        assertTrue(currentGameUiState.isGameOver)

    }


    companion object{
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }
}