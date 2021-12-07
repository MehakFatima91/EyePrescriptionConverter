package com.example.android.eyeprescriptionconverter.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SelectionDataDao {
    @Insert
    suspend fun insertData(data: SelectionData)

    @Query("SELECT * FROM selection_data_table WHERE id = 2")
    fun getSelectionDataFlow(): Flow<SelectionData>


}