package com.snap.android.apis.utils.coroutines

import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

/**
 * Description: Missing methods in Continuation
 * Created by shmuel on 8.8.18.
 */
/**
 * Resume - with no crashes
 * @param t the return value to resume
 *
 * @return a string with the exception description, if any
 */
fun <T> Continuation<T>.safeResume(t: T): String? = try {
    resume(t)
    null
} catch (e: Exception) {
    e.toString()
}

class ContHelper <T>(continuation: Continuation<T>) {
    private val ref = AtomicReference(continuation)
    fun resume(v:T) {
        ref.getAndSet(null)?.resume(v)
    }
}

fun launchAsync(f:suspend CoroutineScope.()->Unit) = GlobalScope.launch(block = f)
fun launchUI(f: suspend CoroutineScope.() -> Unit) = GlobalScope.launch(Dispatchers.Main, block = {
    try {
        f()
    } catch (ignored: Exception) {
    }
})

fun Job.safeCancel() = this.apply { if (isActive) cancel() }

fun Job.cancelAll() {
    try {
        safeCancel()
        cancelChildren()
    } catch (ignored: Exception) {
    }
}
