package com.snap.android.apis.utils.coroutines

import kotlinx.coroutines.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext

/**
 * Description:
 * Created by shmuel on 18.11.18.
 */
@Suppress("RemoveRedundantQualifierName")
class Cor(private val managedPool: ManagedPool) {
    constructor(ctx: CoroutineContext) : this(ManagedPool(null, ctx))
    constructor(numThreads: Int, name: String) : this(fixedThreadContext(numThreads, name))

    private val coroutineContext get() = managedPool.ctx
    val scope = CoroutineScope(coroutineContext)
    private val validRef = AtomicBoolean(true)
    val valid
        get() = validRef.get()

    suspend fun <T> withContext(f: suspend CoroutineScope.() -> T): T = if (valid) kotlinx.coroutines.withContext(managedPool.ctx, f) else throw RuntimeException("Not valid")
    fun launch(f: suspend CoroutineScope.()->Unit) = try {
        if (valid) scope.launch(block=f) else throw RuntimeException("Not valid")
    } catch (throwable:Throwable) {
        throw throwable
    }

    fun <T> async (f: suspend  CoroutineScope.()->T) = scope.async(block = f)

    fun shutdown() {
        validRef.set(false)
        cancel()
        try {
            managedPool.pool?.shutdown()
        } catch (exception: Exception) {
        }
    }

    fun cancel() {
        with(managedPool.ctx) {
            cancelChildren()
            cancel()
        }
    }

    companion object {
        data class ManagedPool(val pool: ExecutorService?, val ctx: CoroutineContext)

        private fun ExecutorService.stampWithName(name: String): ExecutorService {
            submit { Thread.currentThread().name = name }
            return this
        }

        private fun ExecutorService.toManagedPool() = ManagedPool(this, this.asCoroutineDispatcher())

        fun singleThreadContext(name: String) = Executors.newSingleThreadExecutor().stampWithName(name).toManagedPool()
        fun fixedThreadContext(nThreads: Int, name: String) = Executors.newFixedThreadPool(nThreads).stampWithName(name).toManagedPool()
    }
}