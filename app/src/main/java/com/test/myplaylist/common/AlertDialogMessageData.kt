package com.test.myplaylist.common

import androidx.annotation.StringRes
import com.test.myplaylist.R
import java.io.Serializable

data class AlertDialogMessageData(
    @StringRes val titleResId: Int? = null,
    val titleString: String? = null,
    @StringRes val messageResId: Int? = null,
    val messageString: String? = null,
    @StringRes val positiveTextRestId: Int? = R.string.ok,
    @StringRes val negativeTextResId: Int? = null,
    @StringRes val neutralTextRestId: Int? = null,
    val isCancellable: Boolean = false,
    val tag: String? = null
) : Serializable {
    companion object {
        fun dataDialogSettings(tagPositive: String?): AlertDialogMessageData {
            return AlertDialogMessageData(
                titleResId = R.string.settings,
                messageResId = R.string.change_storage_permission_on_device_setting,
                positiveTextRestId = R.string.go_settings,
                negativeTextResId = R.string.cancel,
                tag = tagPositive
            )
        }
    }
}
