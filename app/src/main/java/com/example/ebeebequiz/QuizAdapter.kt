package com.example.ebeebequiz

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.question_card.view.*
import java.util.*
import java.util.Arrays.toString


class QuizAdapter(
    private val questions: List<QuizQuestion>,
    private val onSelect: (Int, Boolean) -> Unit
) :
    RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    private val selectedOptions = MutableList<Int?>(questions.size) { null }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.question_card, parent, false)
        return QuizViewHolder(view)
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.bind(questions[position], selectedOptions[position], position)
    }

    inner class QuizViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(quizQuestion: QuizQuestion, selectedOption: Int?, position: Int) {
            itemView.answers.clearCheck()

            Log.d("QuizAdapter", "$position $selectedOptions")

            when (selectedOption) {
                0 -> itemView.answer1
                1 -> itemView.answer2
                2 -> itemView.answer3
                3 -> itemView.answer4
                else -> null
            }?.let { radioButton ->
                Log.d("QuizAdapter", selectedOption.toString())
                itemView.answers.check(radioButton.id)
            }

            itemView.question.text = quizQuestion.question

            itemView.answer1.text = quizQuestion.allAnswers[0]
            itemView.answer2.text = quizQuestion.allAnswers[1]
            itemView.answer3.text = quizQuestion.allAnswers[2]
            itemView.answer4.text = quizQuestion.allAnswers[3]

            itemView.answers.setOnCheckedChangeListener { _, id ->
                val answerIndex = when (id) {
                    itemView.answer1.id -> 0
                    itemView.answer2.id -> 1
                    itemView.answer3.id -> 2
                    itemView.answer4.id -> 3
                    else -> null
                }
                Log.d("QuizAdapter AnswerIndex", answerIndex.toString())
                selectedOptions[position] = answerIndex
                onSelect(
                    position,
                    answerIndex != null && quizQuestion.allAnswers[answerIndex] == quizQuestion.answer
                )

            }
        }
    }
}