package com.example.android.eyeprescriptionconverter.util

import android.text.method.KeyListener
import android.view.View
import android.widget.NumberPicker
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("onValueEditTouch")
fun showPicker(editText: TextInputEditText, listener: View.OnFocusChangeListener) {
    editText.onFocusChangeListener = listener
}

@BindingAdapter("keyListener")
fun setKeyListener(editText: TextInputEditText, keyListener: KeyListener?) {
    editText.keyListener = keyListener
}

@BindingAdapter("bindPicker", "selectedValue")
fun setValuePicker(picker: NumberPicker, list: List<String>?, pickerValue: String?) {
    list?.let {
        val arrayList = it.toTypedArray()
        val size = arrayList.size

        picker.apply {
            displayedValues = null
            minValue = 0
            maxValue = size.minus(1)
            value = if (pickerValue == "0") {maxValue.div(2)} else if (arrayList.contains(pickerValue)) arrayList.indexOf(pickerValue) else 0


            displayedValues = arrayList
            wrapSelectorWheel = false }
    }

}

