package com.bignerdranch.android.geomain

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val LIST_INDEX = "list"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var resultTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        Log.d(TAG, "onRestoreInstanceState called")

        quizViewModel.currentIndex = savedInstanceState.getInt(KEY_INDEX)
        savedInstanceState.getParcelableArrayList<Question>(LIST_INDEX)?.let { quizViewModel.setQuestionBank(it.toList()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)
        resultTextView = findViewById(R.id.result_text_view)

        setText()

        changeButtonStatusIfCurrentQuestionIsGiven()

        cheatButton.setOnClickListener{_: View ->
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        trueButton.setOnClickListener{ _: View ->
            checkAnswer(true)
            changeButtonState(false)
            setGiven()
            setText()
        }

        falseButton.setOnClickListener{ _: View ->
            checkAnswer(false)
            changeButtonState(false)
            setGiven()
            setText()
        }

        nextButton.setOnClickListener { _: View ->
            quizViewModel.moveToNext()
            updateQuestion()
            changeButtonState(true)
            changeButtonStatusIfCurrentQuestionIsGiven()
            resetCheaterState()
        }

        prevButton.setOnClickListener { _: View ->
            quizViewModel.decreaseCurrentIndex()
            if (quizViewModel.currentIndex == -1) quizViewModel.currentIndex = quizViewModel.size - 1
            updateQuestion()
            changeButtonState(true)
            changeButtonStatusIfCurrentQuestionIsGiven()
            resetCheaterState()
        }

        questionTextView.setOnClickListener { _: View ->
            quizViewModel.moveToNext()
            updateQuestion()
        }

        updateQuestion()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.d(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        val casted = ArrayList<Question>(quizViewModel.questions)
        savedInstanceState.putParcelableArrayList(LIST_INDEX, casted)
    }

    private fun resetCheaterState() {
        quizViewModel.isCheater = false
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun changeButtonStatusIfCurrentQuestionIsGiven() {
        if (quizViewModel.currentQuestionGiven) {
            changeButtonState(false)
        }
    }

    private fun setGiven() {
        quizViewModel.setGivenTrue()
    }

    private fun setText() {
        resultTextView.text = getString(R.string.correct_answers, quizViewModel.calculateResult(), quizViewModel.size)
    }

    private fun changeButtonState(state: Boolean) {
        trueButton.isEnabled = state
        falseButton.isEnabled = state
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        if (userAnswer == correctAnswer) {
            quizViewModel.setCorrectTrue()
        }

        var messageResId = when (userAnswer) {
            correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        if (quizViewModel.isCheater) {
            messageResId = R.string.judgment_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}