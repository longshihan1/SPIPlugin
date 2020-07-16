package com.longshihan.lplugin.utils

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project

/**
 * Created by longhe on 2020-07-16.
 */
inline fun <reified T : BaseExtension> Project.getAndroid(): T = extensions.getByName("android") as T