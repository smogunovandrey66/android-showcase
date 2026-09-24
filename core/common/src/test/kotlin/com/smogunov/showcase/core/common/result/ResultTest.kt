package com.smogunov.showcase.core.common.result

import app.cash.turbine.test
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ResultTest {

    @Test
    fun asResultEmitsLoadingSuccessAndError() = runTest {
        val error = IllegalStateException("boom")
        flow {
            emit(1)
            throw error
        }.asResult().test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(1), awaitItem())
            assertEquals(Result.Error(error), awaitItem())
            awaitComplete()
        }
    }
}
