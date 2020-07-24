package com.longshihan.lplugin.config

object  LConfig{
    var enabled:Boolean=false
    var includes = arrayListOf<String>()
    var excludes = arrayListOf<String>()
    var excludeJars = arrayListOf<String>()
    var blacklist = arrayListOf<String>()

    init {
        //默认不注入的包
        excludes.add("java")
        excludes.add("javax")
        excludes.add("android")
        excludes.add("androidx")
        excludes.add("sun")
        excludes.add("com.sun")
        excludes.add("okhttp3")
        excludes.add("okio")
        excludes.add("org.apache")
        //排除ArgusAPM自身代码的注入
        excludes.add("com.longshihan.collect")

        //黑名单

        //该类参与代码的织入
        includes.add("okhttp3.OkHttpClient.\$Builder")
    }

    /**
     * 用来控制是否织入
     */
    fun enabled(enable: Boolean): LConfig {
        this.enabled = enable
        return this
    }


    fun include(vararg filters: String): LConfig {
        this.includes.addAll(filters)
        return this
    }

    fun exclude(vararg filters: String): LConfig {
        this.excludes.addAll(filters)
        return this
    }

    fun excludeJar(vararg filters: String): LConfig {
        this.excludeJars.addAll(filters)
        return this
    }

    fun blacklist(vararg filters: String): LConfig {
        this.blacklist.addAll(filters)
        return this
    }


}