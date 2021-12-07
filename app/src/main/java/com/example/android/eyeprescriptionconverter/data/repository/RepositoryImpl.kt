package com.example.android.eyeprescriptionconverter.data.repository

import com.example.android.eyeprescriptionconverter.data.db.SelectionData
import com.example.android.eyeprescriptionconverter.data.db.SelectionDataDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val selectionDataDao: SelectionDataDao,
) : Repository {

    override val selectionDataFlow: Flow<SelectionData>
        get() = selectionDataDao.getSelectionDataFlow()
}