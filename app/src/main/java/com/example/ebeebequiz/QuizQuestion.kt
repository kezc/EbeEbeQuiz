package com.example.ebeebequiz

import androidx.annotation.DrawableRes

data class QuizQuestion(
    val question: String,
    val answer: String,
    val otherAnswers: List<String>,
    @DrawableRes
    val image: Int
) {
    val allAnswers = (otherAnswers + answer).shuffled()
}