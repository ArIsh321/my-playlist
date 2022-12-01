package com.test.myplaylist.common

import android.content.Context
import com.test.myplaylist.R

fun Throwable.userReadableMessage(context: Context): String {
    return context.getString(R.string.error_generic)
}
