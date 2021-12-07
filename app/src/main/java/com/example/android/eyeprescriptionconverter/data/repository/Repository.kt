package com.example.android.eyeprescriptionconverter.data.repository

import com.example.android.eyeprescriptionconverter.data.db.SelectionData
import kotlinx.coroutines.flow.Flow

interface Repository {
    val selectionDataFlow: Flow<SelectionData>
}