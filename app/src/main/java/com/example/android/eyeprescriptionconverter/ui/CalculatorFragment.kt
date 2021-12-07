package com.example.android.eyeprescriptionconverter.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.android.eyeprescriptionconverter.R
import com.example.android.eyeprescriptionconverter.databinding.FragmentCalculatorBinding
import com.example.android.eyeprescriptionconverter.util.*
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CalculatorFragment : Fragment() {

    private lateinit var binding: FragmentCalculatorBinding

    private val viewmodel: CalculatorFragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentCalculatorBinding>(
            inflater, R.layout.fragment_calculator, container, false
        ).apply {
            lifecycleOwner = this@CalculatorFragment
            viewModel = viewmodel

        }

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        binding.toggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked){
                when (group.checkedButtonId) {
                    R.id.rightButton ->  {
                        viewmodel.valueEnabled.value = true
                        viewmodel.eyeSide.value = EyeSide.RIGHT}
                    R.id.leftButton -> {
                        viewmodel.eyeSide.value = EyeSide.LEFT
                        viewmodel.sameAsRightCheckBoxState.value?.let {
                            viewmodel.onSameCheckBoxCLick(it)
                        }}
                }


            } else {
                //Something is unchecked, we need to make sure that all the buttons are not un-selected
                if(-1 == group.checkedButtonId){
                    //All buttons are unselected
                    //So now we will select the button which was unselected right now
                    group.check(checkedId)
                }
            }

        }

        binding.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                navigateToBottomSheetPickerFragment(v)
            }

        }

        binding.edittextClickListener = View.OnClickListener {
            navigateToBottomSheetPickerFragment(it)

        }

        binding.infoButtonClickListener = View.OnClickListener {
            showDefinitionDialogue(it)
        }

        binding.disclaimer.setOnClickListener {
            showInfoDialogue(getString(R.string.note), getString(R.string.disclaimer))
        }

        binding.infoComputer.setOnClickListener {
            showInfoDialogue(getString(R.string.note), getString(R.string.info_computer))
        }

        binding.convertToChips.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                group.findViewById<Chip>(checkedId).text.toString().let {
                    when(it) {
                        getString(R.string.reading) -> viewmodel.conversionType.value = ConversionType.READING
                        getString(R.string.computer) -> viewmodel.conversionType.value = ConversionType.COMPUTER
                        getString(R.string.distance) -> viewmodel.conversionType.value = ConversionType.DISTANCE
                    }
                }
            }

        }



        viewmodel.conversionType.observe(viewLifecycleOwner, {
            if (viewmodel.calculateState.value == true) viewmodel.calculateResult()
        })

        viewmodel.selectionLists.observe(viewLifecycleOwner, {
            //  Timber.i("Selection List $it")
        })

        viewmodel.sameAsRightCheckBoxState.observe(viewLifecycleOwner, {
            viewmodel.onSameCheckBoxCLick(it)

        })

        viewmodel.errorEvent.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                ErrorType.SPHERE_EMPTY ->  showSnackBar(getString(R.string.error_type_sphere_empty))
                ErrorType.ADD_EMPTY ->  showSnackBar(getString(R.string.error_type_add_empty))
                ErrorType.LEFT_VALUES_EMPTY -> showErrorDialogue(getString(R.string.error_type_left_values_empty), getString(R.string.confirm)) {viewmodel.calculateResult()}
                ErrorType.ADD_RIGHT_EMPTY -> showSnackBar(getString(R.string.error_type_add_right_empty))
                ErrorType.ADD_LEFT_EMPTY -> showSnackBar(getString(R.string.error_type_add_left_empty))
                ErrorType.RIGHT_VALUES_EMPTY -> showErrorDialogue(getString(R.string.error_type_right_values_empty), getString(R.string.confirm)) {viewmodel.calculateResult()}

            }

        })

        viewmodel.calculateEvent.observe(viewLifecycleOwner, EventObserver { state ->
            if (state) {
                if (viewmodel.calculateButtonClicked.value == true) {
                    binding.scrollview.let {
                        it.smoothScrollTo(0, it.getChildAt(0).height).also {
                            viewmodel.calculateButtonClicked.value = false
                        }
                    }


                }
            }
        })

        viewmodel.bottomSheetDismissed.observe(viewLifecycleOwner, EventObserver {
            viewmodel.let { vm ->
                if (vm.calculateState.value == true) {
                    if (it) {
                        vm.calculateResult()
                    }
                }

            }
        })


    }

    private fun navigateToBottomSheetPickerFragment(v: View?) {
        when (v) {
            binding.sphereLayout.selectedValue  -> {
                viewmodel.pickerListType.value = ListTypes.SPH
                viewmodel.pickerValue.value = if (viewmodel.eyeSide.value == EyeSide.RIGHT)
                    viewmodel.sphereRight.value else viewmodel.sphereLeft.value
            }

            binding.cylinderLayout.selectedValue -> {
                viewmodel.pickerListType.value = ListTypes.CYL
                viewmodel.pickerValue.value = if (viewmodel.eyeSide.value == EyeSide.RIGHT)
                    viewmodel.cylRight.value else viewmodel.cylLeft.value
            }

            binding.axisLayout.selectedValue -> {
                viewmodel.pickerListType.value = ListTypes.AXS
                viewmodel.pickerValue.value = if (viewmodel.eyeSide.value == EyeSide.RIGHT)
                    viewmodel.axisRight.value else viewmodel.axisLeft.value
            }

            binding.addLayout.selectedValue -> {
                viewmodel.pickerListType.value = ListTypes.ADD
                viewmodel.pickerValue.value = if (viewmodel.eyeSide.value == EyeSide.RIGHT)
                    viewmodel.addRight.value else viewmodel.addLeft.value
            }

        }

        try {
            this.findNavController().navigate(R.id.action_calculatorFragment_to_bottomSheetPickerFragment)}
        catch (e: Exception) {
            Log.i("Info","User clicked too fast")

        }



    }

    private fun showDefinitionDialogue(it: View?) {
        when (it) {
            binding.sphereLayout.infoButton -> {
                showInfoDialogue(getString(R.string.sphere),
                    getString(R.string.sphere_definition))
            }
            binding.cylinderLayout.infoButton -> {
                showInfoDialogue(
                    getString(R.string.cylinder),
                    getString(R.string.cylinder_definition)
                )
            }
            binding.axisLayout.infoButton -> {
                showInfoDialogue(getString(R.string.axis),
                    getString(R.string.axis_definition))
            }
            binding.addLayout.infoButton -> {
                showInfoDialogue(
                    getString(R.string.add),
                    getString(R.string.add_definition)
                )
            }
        }
    }


}