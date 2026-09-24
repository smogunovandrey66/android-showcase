package com.smogunov.showcase.core.common.result

import app.cash.turbine.test
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

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
            val errorItem = awaitItem()
            assertIs<Result.Error>(errorItem)
            // Compare by message: coroutines may copy exceptions for stack-trace recovery.
            assertEquals(error.message, errorItem.exception.message)
            awaitComplete()
        }
    }
}
