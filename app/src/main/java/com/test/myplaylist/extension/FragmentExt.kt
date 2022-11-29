package com.test.myplaylist.extension

import androidx.fragment.app.Fragment
import com.test.myplaylist.common.AlertDialogFragment
import com.test.myplaylist.common.AlertDialogMessageData

fun Fragment.showAlertDialogFragment(alertDialogMessageData: AlertDialogMessageData) {
    if (childFragmentManager.findFragmentByTag(null) != null) {
        return
    }
    AlertDialogFragment.getInstance(alertDialogMessageData).also {
        it.show(childFragmentManager, null)
    }
}