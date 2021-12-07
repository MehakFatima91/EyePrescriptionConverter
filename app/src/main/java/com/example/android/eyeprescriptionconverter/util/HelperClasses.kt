package com.example.android.eyeprescriptionconverter.util

import com.example.android.eyeprescriptionconverter.R

enum class EyeSide{
    RIGHT, LEFT
}

enum class ListTypes(val itemType: Int){
    SPH(R.string.sphere),
    CYL(R.string.cylinder),
    AXS(R.string.axis),
    ADD(R.string.add)

}

enum class ConversionType{
    READING, COMPUTER, DISTANCE
}

enum class ErrorType{
    SPHERE_EMPTY,
    ADD_EMPTY,
    LEFT_VALUES_EMPTY,
    ADD_RIGHT_EMPTY,
    ADD_LEFT_EMPTY,
    RIGHT_VALUES_EMPTY
}