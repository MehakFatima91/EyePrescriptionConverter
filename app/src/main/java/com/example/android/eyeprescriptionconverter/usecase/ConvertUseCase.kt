package com.example.android.eyeprescriptionconverter.usecase

import android.content.Context
import com.example.android.eyeprescriptionconverter.util.ConversionType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ConvertUseCase @Inject constructor(@ApplicationContext private val context: Context): UseCase<ConvertUseCaseInput, Map<String, String>> (
    Dispatchers.IO){

    override fun execute(parameters: ConvertUseCaseInput): Map<String,String>  =
        parameters.let {
            val sphereRightDouble = it.sphereRight.toDouble()
            val sphereLeftDouble = it.sphereLeft.toDouble()
            val addRightDouble = it.addRight.toDoubleOrNull()
            val addLeftDouble = it.addLeft.toDoubleOrNull()
            val convertedAddRight = addRightDouble ?: 0.00
            val convertedAddLeft = addLeftDouble ?: 0.00

            it.conversionType.let { type ->
                when(type) {
                    ConversionType.READING -> calculateReading(sphereRightDouble, convertedAddRight, sphereLeftDouble, convertedAddLeft)
                    ConversionType.COMPUTER -> calculateComputer(sphereRightDouble, convertedAddRight, sphereLeftDouble, convertedAddLeft, it.addList)
                    ConversionType.DISTANCE -> mapOf("sphereRight" to it.sphereRight, "sphereLeft" to it.sphereLeft)
                }
            }
        }
}

fun calculateReading(sphereRight: Double, addRight: Double, sphereLeft: Double, addLeft: Double) =
    mapOf(
        "sphereRight" to formatValue(sphereRight.plus(addRight)),
        "sphereLeft" to formatValue(sphereLeft.plus(addLeft)))




fun calculateComputer(sphereRight: Double, addRight: Double, sphereLeft: Double, addLeft: Double, addList: List<String>) =
    mapOf(
        "sphereRight"  to formatValue(adjustAdd(addRight, addList).let { if (it == 0.00) sphereRight else sphereRight.plus(it)}),
        "sphereLeft"  to formatValue(adjustAdd(addLeft, addList).let { if (it == 0.00) sphereLeft else sphereLeft.plus(it)})
    )





data class ConvertUseCaseInput (
    val sphereRight: String,
    val addRight: String,
    val sphereLeft: String,
    val addLeft: String,
    val conversionType: ConversionType,
    val addList: List<String>
)

private fun formatValue(x: Double) = "%+.2f".format(x).replace("+0.00", "0.00")

private fun adjustAdd(add: Double, addList: List<String>)  =
    calculateAdd(add).let { calculatedAdd ->
        formatValue(calculatedAdd).let {
            if (it in addList || it == "0.00") it.toDouble() else calculatedAdd.minus(0.125)

        }

    }


private fun calculateAdd(add: Double) = if (add == 0.00) 0.00 else add.div(2)
