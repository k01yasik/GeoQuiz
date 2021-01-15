package com.bignerdranch.android.geomain

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "MainActivity"

class QuizViewModel: ViewModel() {
    private var questionBank = listOf(
            Question(R.string.question_australia, true),
            Question(R.string.question_oceans, true),
            Question(R.string.question_mideast, false),
            Question(R.string.question_africa, false),
            Question(R.string.question_americas, true),
            Question(R.string.question_asia, true)
    )

    var currentIndex = 0
    var isCheater = false
    var answerIsTrue = false

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val currentQuestionGiven: Boolean
        get() = questionBank[currentIndex].given

    val size: Int
        get() = questionBank.size

    val questions: List<Question>
        get() = questionBank

    fun setQuestionBank(questions: List<Question>) {
        questionBank = questions
    }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun decreaseCurrentIndex() {
        currentIndex -= 1
    }

    fun setGivenTrue() {
        questionBank[currentIndex].given = true
    }

    fun setCorrectTrue() {
        questionBank[currentIndex].correct = true
    }

    fun calculateResult(): Int {
        var count = 0

        for (item in questionBank) {
            if (item.given and item.correct) {
                count += 1
            }
        }

        Log.d(TAG, count.toString())

        return count
    }
}