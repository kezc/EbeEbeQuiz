package com.example.ebeebequiz

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.quiz_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        var cardChangerJob: Job? = null

        progressBar.max = quizQuestions.size * 100

        viewModel.progress.observe(viewLifecycleOwner, Observer { progress ->
            setProgressAnimate(progress)
        })

        viewModel.question.observe(viewLifecycleOwner, Observer { quizQuestion ->
            if (viewModel.quizPosition == 0) {
                showQuestion(quizQuestion)
            } else {
                card.animate()
                    .translationX(-view.width.toFloat())
                    .apply { duration = 150 }
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            showQuestion(quizQuestion)
                            card.translationX = view.width.toFloat()
                            card.animate()
                                .translationX(0f)
                                .apply { duration = 150 }
                                .setListener(null)
                            button.text = "Next question"
                            button.setBackgroundColor(Color.WHITE)
                        }
                    })
            }
        })

        viewModel.answerResult.observe(viewLifecycleOwner, Observer { isCorrect ->
            if (isCorrect) {
                button.text = "✔"
            } else {
                button.text = "✘"
            }
            animateButtonOnAnswer(isCorrect)

            cardChangerJob = lifecycleScope.launch(Dispatchers.Main) {
                delay(750)
                viewModel.nextQuestion()
            }
        })


        button.setOnClickListener {
            cardChangerJob?.cancel()
            val checkedRadioButtonId = answers.checkedRadioButtonId
            val checkedButton = answers.findViewById<RadioButton>(checkedRadioButtonId)
            if (checkedButton != null) {
                val answer = checkedButton.text.toString()
                Log.d("QuizFragment", answer)
                viewModel.buttonClick(answer)
            } else {
                Toast.makeText(requireContext(), "You need to select answer", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }

    private fun animateButtonOnAnswer(isCorrect: Boolean) {
        val color = if (isCorrect) {
            ColorDrawable(Color.GREEN)
        } else {
            ColorDrawable(Color.RED)
        }
        val colors = arrayOf(button.background, color)
        val transition = TransitionDrawable(colors)
        button.background = transition
        transition.startTransition(200)
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