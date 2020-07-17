package com.longshihan.lplugin

/**
 * Created by longhe on 2020-07-17.
 */
object TimeCache {
    var sStartTime: MutableMap<String, Long> = HashMap()
    var sEndTime: MutableMap<String, Long> = HashMap()
    fun setStartTime(methodName: String, time: Long) {
        sStartTime[methodName] = time
    }

    fun setEndTime(methodName: String, time: Long) {
        sEndTime[methodName] = time
    }

    fun getCostTime(methodName: String): String {
        var start:Long? = sStartTime[methodName]
        if (start==null){
            start=0L
        }
        var end:Long? = sEndTime[methodName]
        if (end==null){
            end=0L
        }
        return "method: " + methodName + " main " + (end.minus(start)) + " ns"
    }
}