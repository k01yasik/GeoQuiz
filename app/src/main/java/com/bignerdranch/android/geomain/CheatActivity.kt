package com.bignerdranch.android.geomain

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geomain.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geomain.answer_is_true"
const val EXTRA_CHEAT = "com.bignerdranch.android.geomain.cheat_clicked"
const val EXTRA_CHEAT_COUNT = "com.bignerdranch.android.geomain.cheat_count"
const val EXTRA_MAX_CHEAT_COUNT = "com.bignerdranch.android.geomain.max_cheat"
private const val TAG = "MainActivity"

class CheatActivity : AppCompatActivity() {

    private var answerIsTrue = false
    private var cheatCount = 0
    private var maxCheat = 0
    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var apiLevelTextView: TextView
    private lateinit var cheatTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        apiLevelTextView = findViewById(R.id.api_level_text_view)
        cheatTextView = findViewById(R.id.cheat_count_text_view)

        apiLevelTextView.text = getString(R.string.api_level, Build.VERSION.SDK_INT)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        cheatCount = intent.getIntExtra(EXTRA_CHEAT_COUNT, 0)
        maxCheat = intent.getIntExtra(EXTRA_MAX_CHEAT_COUNT, 3)

        setTextForCheatTextView()

        checkCheatButton()

        showAnswerButton.setOnClickListener{_: View ->

            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }

            cheatCount += 1
            answerTextView.setText(answerText)
            setTextForCheatTextView()
            showAnswerButton.isEnabled = false
            setAnswerShownResult(true)
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "CheatActivity onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "CheatActivity onDestroy() called")
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean, cheatCount: Int, maxCheatCount: Int): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                putExtra(EXTRA_CHEAT_COUNT, cheatCount)
                putExtra(EXTRA_MAX_CHEAT_COUNT, maxCheatCount)
            }
        }
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
            putExtra(EXTRA_CHEAT, cheatCount)
        }
        setResult(Activity.RESULT_OK, data)
    }

    private fun setTextForCheatTextView() {
        cheatTextView.text = getString(R.string.cheat_text, cheatCount, maxCheat)
    }

    private fun checkCheatButton() {
        if (cheatCount == maxCheat) {
            showAnswerButton.isEnabled = false
        }
    }
}