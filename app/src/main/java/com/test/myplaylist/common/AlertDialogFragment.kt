package com.test.myplaylist.common

import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs

class AlertDialogFragment : DialogFragment() {
    private val safeArgs: AlertDialogFragmentArgs by navArgs()
    private val alertDialogMessageData by lazy {
        safeArgs.alertDialogMessageData
    }

    private var onDialogClickListener: OnDialogClickListener? = null
    private var onDialogButtonClickListener: OnDialogButtonClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onDialogClickListener = when {
            parentFragment is OnDialogClickListener -> parentFragment as OnDialogClickListener
            context is OnDialogClickListener -> context
            else -> null
        }
        onDialogButtonClickListener = when {
            parentFragment is OnDialogButtonClickListener -> parentFragment as OnDialogButtonClickListener
            context is OnDialogButtonClickListener -> context
            else -> null
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext()).apply {
            this@AlertDialogFragment.isCancelable = alertDialogMessageData.isCancellable
            val titleResId = alertDialogMessageData.titleResId

            val title = TextView(requireContext())
            title.text = titleResId?.let { requireContext().getString(it) }
            title.setPadding(50, 50, 50, 0)
            title.typeface = Typeface.DEFAULT_BOLD
            title.textSize = 16f

            if (titleResId != null) {
                setCustomTitle(title)
            } else {
                alertDialogMessageData.titleString?.let {
                    title.text = it
                    setCustomTitle(title)
                }
            }

            val messageResId = alertDialogMessageData.messageResId
            val message = TextView(requireContext())
            message.text = messageResId?.let { requireContext().getString(it) }
            message.setPadding(50, 30, 50, 0)
            message.textSize = 14f

            alertDialogMessageData.positiveTextRestId?.let {
                setPositiveButton(it) { _, _ ->
                    onDialogClickListener?.onDialogClick(alertDialogMessageData.tag, true)
                    onDialogButtonClickListener?.onDialogButtonClick(
                        alertDialogMessageData.tag,
                        OnDialogButtonClickListener.ButtonType.POSITIVE
                    )
                    dismiss()
                }
            }
            alertDialogMessageData.negativeTextResId?.let {
                setNegativeButton(it) { _, _ ->
                    onDialogClickListener?.onDialogClick(alertDialogMessageData.tag, false)
                    onDialogButtonClickListener?.onDialogButtonClick(
                        alertDialogMessageData.tag,
                        OnDialogButtonClickListener.ButtonType.NEGATIVE
                    )
                    dismiss()
                }
            }

        }.create()
    }

    override fun onDetach() {
        onDialogClickListener = null
        onDialogButtonClickListener = null
        super.onDetach()
    }

    companion object {
        fun getInstance(alertDialogMessageData: AlertDialogMessageData): AlertDialogFragment {
            return AlertDialogFragment()
                .also { fragment ->
                    fragment.arguments =
                        AlertDialogFragmentArgs(alertDialogMessageData = alertDialogMessageData).toBundle()
                }
        }
    }

    interface OnDialogClickListener {
        fun onDialogClick(tag: String?, isPositive: Boolean)
    }

    interface OnDialogButtonClickListener {
        enum class ButtonType {
            POSITIVE, NEGATIVE, NEUTRAL
        }

        fun onDialogButtonClick(tag: String?, buttonType: ButtonType)
    }
}
