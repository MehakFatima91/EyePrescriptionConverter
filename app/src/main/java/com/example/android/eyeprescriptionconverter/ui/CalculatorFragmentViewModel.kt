package com.example.android.eyeprescriptionconverter.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.android.eyeprescriptionconverter.R
import com.example.android.eyeprescriptionconverter.data.db.SelectionData
import com.example.android.eyeprescriptionconverter.data.repository.Repository
import com.example.android.eyeprescriptionconverter.usecase.ConvertUseCase
import com.example.android.eyeprescriptionconverter.usecase.ConvertUseCaseInput
import com.example.android.eyeprescriptionconverter.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.android.eyeprescriptionconverter.util.Result


@HiltViewModel
class CalculatorFragmentViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val convertUseCase: ConvertUseCase,
    repository: Repository
): ViewModel(){

    var select: String = context.resources.getString(R.string.select)

    val sphereRight = MutableLiveData("0.00")
    val cylRight = MutableLiveData("0.00")
    val axisRight = MutableLiveData("0")
    val addRight = MutableLiveData(select)

    val sphereLeft = MutableLiveData("0.00")
    val cylLeft = MutableLiveData("0.00")
    val axisLeft = MutableLiveData("0")
    val addLeft = MutableLiveData(select)

    val eyeSide = MutableLiveData(EyeSide.RIGHT)
    val pickerListType = MutableLiveData<ListTypes>()
    val pickerValue = MutableLiveData<String>()

    val selectionLists: LiveData<SelectionData> = repository.selectionDataFlow.asLiveData()
    val sameAsRightCheckBoxState = MutableLiveData(false)
    val calculateState = MutableLiveData(false)
    val conversionType = MutableLiveData(ConversionType.READING)
    val errorEvent= MutableLiveData<Event<ErrorType>>()
    val valueEnabled = MutableLiveData(true)

    val convertedSphereRight = MutableLiveData<String>()
    val convertedSphereLeft = MutableLiveData<String>()

    val bottomSheetDismissed = MutableLiveData(Event(false))

    val calculateEvent = MutableLiveData(Event(false))
    val calculateButtonClicked = MutableLiveData(false)

    fun onResetClick() {
        calculateState.value = false
        sameAsRightCheckBoxState.value = false
        sphereRight.value = "0.00"
        cylRight.value = "0.00"
        axisRight.value = "0"
        addRight.value = select
        sphereLeft.value = "0.00"
        cylLeft.value = "0.00"
        axisLeft.value = "0"
        addLeft.value = select
    }

    fun onSameCheckBoxCLick(state: Boolean){
        if (state) {
            sphereLeft.value = sphereRight.value
            cylLeft.value = cylRight.value
            axisLeft.value = axisRight.value
            addLeft.value = addRight.value
            valueEnabled.value = eyeSide.value != EyeSide.LEFT

        } else {
            valueEnabled.value = true
        }


    }

    fun onCalculateClick() {
        calculateState.value = false
        calculateButtonClicked.value = true

        if (sphereRight.value == "0.00" && sphereLeft.value == "0.00") {
            errorEvent.value = Event(ErrorType.SPHERE_EMPTY)
            return
        }

        if (sameAsRightCheckBoxState.value == true) {
            sphereLeft.value = sphereRight.value
            cylLeft.value = cylRight.value
            axisLeft.value = axisRight.value
            addLeft.value = addRight.value
        }

        when (eyeSide.value) {
            EyeSide.RIGHT -> {
                if( sphereRight.value != "0.00" && (addRight.value == select || addRight.value == "—")) {
                    errorEvent.value = Event(ErrorType.ADD_EMPTY)
                } else if (sphereLeft.value != "0.00" && (addLeft.value == select || addLeft.value == "—")) {
                    errorEvent.value = Event(ErrorType.ADD_LEFT_EMPTY)
                } else if (sphereRight.value == "0.00" && (addRight.value == select || addRight.value == "—")
                    && sphereLeft.value != "0.00" && (addLeft.value != select || addLeft.value != "—")) {
                    errorEvent.value = Event(ErrorType.RIGHT_VALUES_EMPTY)
                }  else if (sphereLeft.value == "0.00" && (addLeft.value == select || addLeft.value == "—")) {
                    errorEvent.value = Event(ErrorType.LEFT_VALUES_EMPTY)
                } else {
                    calculateResult()

                }

            }
            EyeSide.LEFT -> {
               if (sphereLeft.value != "0.00" && (addLeft.value == select || addLeft.value == "—")) {
                    errorEvent.value = Event(ErrorType.ADD_EMPTY)
                } else if (sphereRight.value != "0.00" && (addRight.value == select || addRight.value == "—")) {
                    errorEvent.value = Event(ErrorType.ADD_RIGHT_EMPTY)
                } else if (sphereRight.value != "0.00" && (addRight.value != select || addRight.value != "—")
                    && sphereLeft.value == "0.00" && (addLeft.value == select || addLeft.value == "—")) {
                    errorEvent.value = Event(ErrorType.LEFT_VALUES_EMPTY)
                }  else if (sphereRight.value == "0.00" && (addRight.value == select || addRight.value == "—")) {
                    errorEvent.value = Event(ErrorType.RIGHT_VALUES_EMPTY)
                }   else {
                    calculateResult()

                }

            }
        }




    }

    fun calculateResult() {
        calculateState.value = true

        viewModelScope.launch { convertUseCase.invoke(
            ConvertUseCaseInput(sphereRight.value!!,addRight.value!!,sphereLeft.value!!,
            addLeft.value!!,conversionType.value!!,selectionLists.value?.addList!!)
        ).let {
            when (it) {
                is Result.Success -> {

                    it.data.let { map ->
                        convertedSphereRight.value = map["sphereRight"]
                        convertedSphereLeft.value = map["sphereLeft"]
                    }

                    calculateEvent.value = Event(true)

                }

               is Result.Error -> {

                  Log.i("Error","result Error converting")

              }
            }



        }
        }




    }

}