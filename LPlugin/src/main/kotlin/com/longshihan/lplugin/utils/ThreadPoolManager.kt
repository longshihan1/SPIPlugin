package com.longshihan.lplugin.utils

import java.util.concurrent.*

class ThreadPoolManager {

    companion object {
        @Volatile
        private var mLongPool: ThreadPoolProxy? = null
        private val mLongLock = Any()

        @Volatile
        private var mShortPool: ThreadPoolProxy? = null
        private val mShortLock = Any()
        fun getLongPool(): ThreadPoolProxy? {
            if (mLongPool == null) {
                synchronized(mLongLock) {
                    if (mLongPool == null) {
                        mLongPool = ThreadPoolProxy(3, 3, 30000)
                    }
                }
            }
            return mLongPool
        }

        fun getShortPool(): ThreadPoolProxy? {
            if (mShortPool == null) {
                synchronized(mShortLock) {
                    if (mShortPool == null) {
                        mShortPool = ThreadPoolProxy(3, 3, 0)
                    }
                }
            }
            return mShortPool
        }
    }


    class ThreadPoolProxy (private val mCoreSize: Int, private val mMaxSize: Int, private val mKeepLive: Long) {
        private var mExecutor: ThreadPoolExecutor? = null

        private fun checkPool() {
            if (mExecutor == null || mExecutor!!.isShutdown) {

                val unit = TimeUnit.MILLISECONDS
                val mWorkQueue = LinkedBlockingDeque<Runnable>()
                val mFactory = Executors.defaultThreadFactory()
                val mHandler = ThreadPoolExecutor.AbortPolicy()

                mExecutor = ThreadPoolExecutor(mCoreSize, mMaxSize, mKeepLive, unit, mWorkQueue, mFactory,
                    mHandler)
            }

        }

        fun exec(task: Runnable?) {
            if (task == null) {
                return
            }

            checkPool()

            mExecutor!!.execute(task)
        }

        fun submit(task: Runnable?): Future<*>? {
            if (task == null) {
                return null
            }

            checkPool()
            return mExecutor!!.submit(task)
        }

        fun remove(task: Runnable?) {
            if (task == null) {
                return
            }
            checkPool()
            mExecutor!!.remove(task)
        }
    }
}