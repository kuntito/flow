package com.example.flow.data.remote.helpers

import android.util.Log
import com.example.flow.flowDebugTag

/**
 * info about an API call.
 *
 * [fnDesc] should describe what the call does, enough to let a debugger know what it's about.
 * it's included in the log message.
 *
 * [fn] triggers the API call.
 * */
data class ApiCallInfo<T>(
    val fnDesc: String,
    val fn: suspend () -> T,
)

/**
 * wraps an API call in a try-catch block.
 *
 * if the call fails, it logs the error along with the function description
 * from [apiCallInfo].
 */
suspend fun <T> safeApiCall(
    apiCallInfo: ApiCallInfo<T>
): T? {
    return try {
        apiCallInfo.fn()
    } catch (e: Exception) {
        Log.d(
            flowDebugTag,
            "api call failed, call description: ${apiCallInfo.fnDesc}" +
                    "\n" + "errorDetails: $e"
        )
        null
    }
}