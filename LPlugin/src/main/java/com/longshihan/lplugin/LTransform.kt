package com.longshihan.lplugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.build.gradle.internal.pipeline.TransformManager.SCOPE_FULL_PROJECT
import com.android.utils.FileUtils
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassReader.EXPAND_FRAMES
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by longhe on 2020-07-16.
 */

open class LTransform(val project: Project): Transform(){
    private var TAG:String="LTransform"
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

    @Throws(IOException::class,TransformException::class,InterruptedException::class)
    override fun transform(context: Context?, inputs: MutableCollection<TransformInput>?,
        referencedInputs: MutableCollection<TransformInput>?, outputProvider: TransformOutputProvider?,
        isIncremental: Boolean)  {
        print( "//===============asm visit start===============//")
        var startTime = System.currentTimeMillis()
        inputs!!.stream().forEach {input->
            input.directoryInputs.stream().forEach {directoryInput ->
                //坐等遍历class并被ASM操作
                if (directoryInput.file.isDirectory){
                    var filees=directoryInput.file.walk()
                    filees.forEach {file ->
                        val name=file.name
                        if (name.endsWith(".class") && !name.startsWith("R\$") &&
                            "R.class" != name && "BuildConfig.class" != name) {
                            println(name+":is changeing ,,,,")
                            var cr= ClassReader(file.readBytes())
                            var cw= ClassWriter(ClassWriter.COMPUTE_MAXS)
                            var cv=CostClassVisitor(cw)
                            cr.accept(cv,EXPAND_FRAMES)
                            var code=cw.toByteArray()
                            println(file.parentFile.absolutePath+File.separator+name+"is save")
                            var fos=FileOutputStream(file.parentFile.absolutePath+File.separator+name)
                            fos.write(code)
                            fos.close()
                        }
                    }
                }
                val dest=outputProvider?.getContentLocation(directoryInput.name,
                    directoryInput.contentTypes,directoryInput.scopes,
                    Format.DIRECTORY)
                println("----directory is location  is  ${dest?.absolutePath}")
                FileUtils.copyFile(directoryInput.file,dest)
            }
            input.jarInputs.stream().forEach { jarInput ->
                var jarName=jarInput.name
                val md5Name=DigestUtils.md5Hex(jarInput.file.absolutePath)
                if (jarName.endsWith(".jar")){
                    jarName=jarName.substring(0,jarName.length-4)
                }
                val dest=outputProvider?.getContentLocation(jarName+md5Name,
                    jarInput.contentTypes,jarInput.scopes,
                    Format.JAR)
                println("-----jar is location  is  ${dest?.absolutePath}")
                FileUtils.copyFile(jarInput.file,dest)
            }
        }
        val cost=(System.currentTimeMillis()-startTime)/1000
        print("plugin cost $cost secs")
        println("//===============asm visit end===============//")


    }

}
