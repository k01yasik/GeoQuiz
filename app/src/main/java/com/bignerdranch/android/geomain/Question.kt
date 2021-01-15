package com.bignerdranch.android.geomain

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(@StringRes val textResId: Int, val answer: Boolean, var given: Boolean = false, var correct: Boolean = false): Parcelable