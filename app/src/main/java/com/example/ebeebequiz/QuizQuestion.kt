package com.example.ebeebequiz

data class QuizQuestion(
    val question: String,
    val answer: String,
    val otherAnswers: List<String>
) {
    val allAnswers = (otherAnswers + answer).shuffled()
}