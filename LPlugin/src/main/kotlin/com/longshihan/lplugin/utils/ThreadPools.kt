package com.longshihan.lplugin.utils

import java.util.concurrent.*

object ThreadPools {
    var exec: ExecutorService = ThreadPoolExecutor(
        20,
        300,
        0L,
        TimeUnit.MILLISECONDS,
        LinkedBlockingQueue(1024),
        ThreadFactory { r -> Thread(r) },
        ThreadPoolExecutor.AbortPolicy()
    )
}