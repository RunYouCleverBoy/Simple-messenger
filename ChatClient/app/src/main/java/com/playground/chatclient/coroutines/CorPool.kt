package com.snap.android.apis.utils.coroutines

/**
 * Description:
 * Created by shmuel on 2019-04-29.
 */
class CorPool<T>(val size: Int, val name: String, val hashFunc: (key: T, poolSize: Int) -> Int = ::defaultHash) {
    private val pool = Array(size) { i -> Cor(Cor.singleThreadContext("$name:$i")) }
    operator fun get(key: T) = pool[hashFunc(key, size)]

    fun shutdown() {
        pool.forEach(Cor::shutdown)
    }

    companion object {
        fun <T> defaultHash(key: T, size: Int): Int = when (key) {
            is Int -> key % size
            is Long -> (key % size).toInt()
            is String -> key.hashCode() % size
            else -> key.hashCode() % size
        }
    }
}