package com.ntut.madd.finalproject.data.model

import androidx.annotation.StringRes

sealed class ErrorMessage {
    class StringError(val message: String) : ErrorMessage()
    class IdError(@StringRes val message: Int) : ErrorMessage()
}
