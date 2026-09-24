package com.smogunov.showcase.core.data.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.smogunov.showcase.core.domain.repository.FavoritesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/** Periodically refreshes favorites so they stay up to date even when the app is closed. */
@HiltWorker
internal class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val favoritesRepository: FavoritesRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result =
        if (favoritesRepository.sync()) Result.success() else Result.retry()
}
