package com.example.android.eyeprescriptionconverter.util

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.android.eyeprescriptionconverter.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showSnackBar(content: String) {
    Snackbar.make(
        requireActivity().findViewById(android.R.id.content),
        content,
        Snackbar.LENGTH_LONG
    ).show()
}

fun Fragment.showErrorDialogue(error: String, buttonText: String, func: () -> Unit) {
    MaterialAlertDialogBuilder(requireContext())
        .setMessage(error)
        .setPositiveButton(buttonText) { dialog, which ->
            func()
        }
        .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
            dialog.dismiss()
        }
        .show()

}

fun Fragment.showInfoDialogue(title: String, content: String) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMessage(content)
        .setPositiveButton(getString(R.string.got_it)) { dialog, which ->
            dialog.dismiss()
        }
        .show()

}