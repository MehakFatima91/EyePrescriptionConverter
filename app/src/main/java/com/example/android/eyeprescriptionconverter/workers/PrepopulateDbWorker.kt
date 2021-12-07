package com.example.android.eyeprescriptionconverter.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.eyeprescriptionconverter.data.db.AppDatabase
import com.example.android.eyeprescriptionconverter.data.db.SelectionData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PrepopulateDbWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val database = AppDatabase.getInstance(applicationContext)
            val data = SelectionData(id = 2)
            data.generateAllLists()

            database.selectionDataDao().insertData(data)
            Result.success()
        } catch (ex: Exception) {
            Result.failure()
        }

    }
}