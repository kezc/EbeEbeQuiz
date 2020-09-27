package com.example.ebeebequiz

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.quiz_fragment.*

class QuizFragment : Fragment() {

    companion object {
        fun newInstance() = QuizFragment()
    }

    private val viewModel: QuizViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.quiz_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var questionPos = 0

        progressBar.max = quizQuestions.size * 100
        setProgressAnimate(questionPos)

        showQuestion(quizQuestions[questionPos])

        button.setOnClickListener {
            if (++questionPos >= quizQuestions.size) questionPos = 0
            setProgressAnimate(questionPos)
            Log.d("QuizFragment", questionPos.toString())
            card.animate().translationX(-view.width.toFloat()).apply { duration = 150 }
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        showQuestion(quizQuestions[questionPos])
                        card.translationX = view.width.toFloat()
                        card.animate().translationX(0f).apply { duration = 150 }.setListener(null)
                    }
                })
        }

    }

    private fun setProgressAnimate(questionPos: Int) {
        ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, (questionPos + 1) * 100)
            .apply {
                duration = 100
                interpolator = DecelerateInterpolator()
                start()
            }
    }

    private fun showQuestion(quizQuestion: QuizQuestion) {
        image.setImageDrawable(ContextCompat.getDrawable(requireContext(), quizQuestion.image))
        question.text = quizQuestion.question
        answers.clearCheck()
        answers.jumpDrawablesToCurrentState()
        answer0.text = quizQuestion.allAnswers[0]
        answer1.text = quizQuestion.allAnswers[1]
        answer2.text = quizQuestion.allAnswers[2]
        answer3.text = quizQuestion.allAnswers[3]
    }


}