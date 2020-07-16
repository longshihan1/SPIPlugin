package com.longshihan.lplugin

import com.android.build.gradle.AppExtension
import com.longshihan.lplugin.utils.getAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by longhe on 2020-07-16.
 * plugin - start
 */
class LPlugin :Plugin<Project>{
    override fun apply(project: Project) {
        when{
            project.plugins.hasPlugin("com.android.application")->project.getAndroid<AppExtension>().let { android->
                android.registerTransform(LTransform(project))
            }



        }

    }

}