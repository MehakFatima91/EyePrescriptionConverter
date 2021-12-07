package com.example.android.eyeprescriptionconverter.ui

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.android.eyeprescriptionconverter.R
import com.example.android.eyeprescriptionconverter.databinding.FragmentBottomSheetPickerBinding
import com.example.android.eyeprescriptionconverter.util.Event
import com.example.android.eyeprescriptionconverter.util.EyeSide
import com.example.android.eyeprescriptionconverter.util.ListTypes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetPickerFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetPickerBinding

    private val viewmodel: CalculatorFragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentBottomSheetPickerBinding>(
            inflater, R.layout.fragment_bottom_sheet_picker, container, false
        ).apply {
            lifecycleOwner = this@BottomSheetPickerFragment

        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPicker()


        binding.okButton.setOnClickListener {
            dismiss()

        }
        binding.cancelButton.setOnClickListener {
            dismiss()
        }


        viewmodel.pickerListType.value?.let {
            binding.itemHeader.text =
                if (viewmodel.eyeSide.value == EyeSide.RIGHT) getString(it.itemType)+"- Right"
                else getString(it.itemType)+"- Left"

        }
    }

    private fun setPicker() {
        binding.pickerValue = viewmodel.pickerValue.value

        binding.pickerList =  when (viewmodel.pickerListType.value) {
            ListTypes.SPH -> viewmodel.selectionLists.value?.sphereList
            ListTypes.CYL -> viewmodel.selectionLists.value?.cylinderList
            ListTypes.AXS -> viewmodel.selectionLists.value?.axisList
            ListTypes.ADD -> viewmodel.selectionLists.value?.addList
            else ->  viewmodel.selectionLists.value?.sphereList
        }

        binding.picker.setOnValueChangedListener { picker, oldVal, newVal ->
            binding.pickerList?.get(newVal).let { value ->
                when (viewmodel.eyeSide.value) {
                    EyeSide.RIGHT -> {
                        when (viewmodel.pickerListType.value) {
                            ListTypes.SPH -> viewmodel.sphereRight.value = value
                            ListTypes.CYL -> viewmodel.cylRight.value = value
                            ListTypes.AXS -> viewmodel.axisRight.value = value
                            ListTypes.ADD -> viewmodel.addRight.value = value
                        }
                    }
                    EyeSide.LEFT -> {
                        when (viewmodel.pickerListType.value) {
                            ListTypes.SPH -> viewmodel.sphereLeft.value = value
                            ListTypes.CYL -> viewmodel.cylLeft.value = value
                            ListTypes.AXS -> viewmodel.axisLeft.value = value
                            ListTypes.ADD -> viewmodel.addLeft.value = value

                        }
                    }
                }
            }
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        viewmodel.bottomSheetDismissed.value = Event(true)

    }


}