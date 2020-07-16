package com.longshihan.lplugin

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.build.gradle.internal.pipeline.TransformManager.SCOPE_FULL_PROJECT
import org.gradle.api.Project

/**
 * Created by longhe on 2020-07-16.
 */

open class LTransform(val project: Project): Transform(){
    var TAG:String="LTransform"
    var transformers= mutableSetOf<QualifiedContent.Scope>()
    override fun getName(): String {

        return TAG
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
       return TransformManager.CONTENT_CLASS
    }

    override fun isIncremental(): Boolean {
        return true
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> = when {
        transformers.isEmpty() -> SCOPE_FULL_PROJECT
        else -> super.getReferencedScopes()
    }

}
