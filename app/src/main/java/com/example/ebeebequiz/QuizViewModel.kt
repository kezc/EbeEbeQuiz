package com.example.ebeebequiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {
    var quizPosition = 0

    private val _progress = MutableLiveData<Int>(quizPosition)
    val progress: LiveData<Int>
        get() = _progress

    private val _question = MutableLiveData<QuizQuestion>(quizQuestions[quizPosition])
    val question: LiveData<QuizQuestion>
        get() = _question

    private val _answerResult = MutableLiveData<Boolean>()
    val answerResult: LiveData<Boolean>
        get() = _answerResult

    var wasAnswerChecked = false

    fun buttonClick(answer: String) {
        if (wasAnswerChecked) {
            nextQuestion()
        } else {
            _answerResult.value = checkAnswer(answer)
        }
    }

    fun nextQuestion() {
        quizPosition++
        if (quizPosition == quizQuestions.size) quizPosition = 0
        wasAnswerChecked = false
        _progress.value = quizPosition
        _question.value = quizQuestions[quizPosition]
    }

    private fun checkAnswer(answer: String): Boolean {
        wasAnswerChecked = true
        return quizQuestions[quizPosition].answer == answer
    }


}