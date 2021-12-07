package com.example.android.eyeprescriptionconverter.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "selection_data_table")
data class SelectionData(
    @PrimaryKey(autoGenerate = true) val id: Int = 1,
    var sphereList: List<String> = emptyList(),
    var cylinderList: List<String> = emptyList(),
    var axisList: List<String> = emptyList(),
    var addList: List<String> = emptyList(),
) {

    fun generateAllLists(){
        generateSphereList()
        generateCylinderList()
        generateAxisList()
        generateAddList()

    }

    fun generateSphereList(){
        val sphereSelectionList: MutableList<String> = mutableListOf()
        var x = -20.00
        while (x >= -20.00 && x <= +20.00) {
            sphereSelectionList.add(transformListItems(x))
            x += 0.25
        }
        sphereList = sphereSelectionList.toList()
    }


    fun generateCylinderList() {
        val cylinderSelectionList: MutableList<String> = mutableListOf()
        var x = -6.00
        while (x >= -6.00 && x <= +6.00) {
            cylinderSelectionList.add(transformListItems(x))
            x += 0.25
        }
        cylinderList = cylinderSelectionList.toList()

    }

    private fun generateAxisList() {
        val axisSelectionList: MutableList<String> = mutableListOf()
        for (i in 0..180) {
            axisSelectionList.add(i.toString())
        }
        axisList = axisSelectionList.toList()
    }

    private fun generateAddList() {
        val addSelectionList: MutableList<String> = mutableListOf("—")
        var x = 0.25
        while (x >= 0.25 && x <= +8.00) {
            addSelectionList.add(transformListItems(x))
            x += 0.25
        }

        addList= addSelectionList.toList()

    }

}


private fun transformListItems(x: Double) = "%+.2f".format(x).replace("+0.00", "0.00")