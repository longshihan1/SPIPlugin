package com.longshihan.lplugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.build.gradle.internal.pipeline.TransformManager.SCOPE_FULL_PROJECT
import com.android.utils.FileUtils
import com.longshihan.lplugin.config.LConfig
import com.longshihan.lplugin.utils.Config
import com.longshihan.lplugin.utils.loadTransformers
import com.longshihan.spi_api.LTransformListener
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils.forceDelete
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassReader.EXPAND_FRAMES
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * Created by longhe on 2020-07-16.
 */
open class LTransform(val project: Project) : Transform() {
    private var TAG: String = "LTransform"

    override fun getName(): String {
        return TAG
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        super.transform(transformInvocation)
        if (!Config.enable){
            println("时间插桩插件关闭")
        }else{
            println("时间插桩插件开启")
        }
        try {
            println("===== ASM Transform =====")
            println("${transformInvocation?.inputs}")
            println("${transformInvocation?.referencedInputs}")
            println("${transformInvocation?.outputProvider}")
            println("${transformInvocation?.isIncremental}")
            //当前是否是增量编译
            val isIncremental = transformInvocation?.isIncremental
            //消费型输入，可以从中获取jar包和class文件夹路径。需要输出给下一个任务
            val inputs: MutableCollection<TransformInput>? = transformInvocation?.inputs
            //引用型输入，无需输出。
            val referencedInputs: MutableCollection<TransformInput>? =
                transformInvocation?.referencedInputs
            //OutputProvider管理输出路径，如果消费型输入为空，你会发现OutputProvider == null
            val outputProvider = transformInvocation?.outputProvider
            inputs?.stream()?.forEach { input ->
                input.jarInputs.stream().forEach { jarInput ->
                    val dest = outputProvider?.getContentLocation(
                        jarInput.file.absolutePath,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR
                    )
                    //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
                    FileUtils.copyFile(jarInput.file, dest)
                }
                input.directoryInputs.stream().forEach { directoryInput ->
                    val fileCPath=directoryInput.file.absolutePath
                    if (directoryInput.file.isDirectory) {
                        if (directoryInput.file.name != "META-INF") {
                            directoryInput.file.walk().forEach { file ->
                                val name = file.name
                                val dpath=file.absolutePath
                                val currentPath=dpath.replace(fileCPath,"")
                                    .replace("/",".")
                                    .replaceFirst(".","")
                                println("---------$dpath")
                                var isReplce=false
                                for (configStr in LConfig.excludes){
                                    if (currentPath.startsWith(configStr)){
                                        isReplce=true
                                        break
                                    }
                                }
                                if (!isReplce &&name.endsWith(".class") && !name.startsWith("R\$")
                                    && "R.class" != name && "BuildConfig.class" != name) {
                                    try {
                                        println("$currentPath:is changeing ,,,,")
                                        val cr = ClassReader(file.readBytes())
                                        val cw = ClassWriter(cr,ClassWriter.COMPUTE_MAXS)
                                        val cv = TestMethodClassAdapter(currentPath,cw)
                                        cr.accept(cv, EXPAND_FRAMES)
                                        val code = cw.toByteArray()
                                        val fos =
                                            FileOutputStream(file.parentFile.absolutePath + File.separator + name)
                                        fos.write(code)
                                        fos.close()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        }
                        val dest = outputProvider?.getContentLocation(
                            directoryInput.name,
                            directoryInput.contentTypes,
                            directoryInput.scopes,
                            Format.DIRECTORY
                        )
                        println("----directory is location  is  ${dest?.absolutePath}")
                        FileUtils.copyDirectory(directoryInput.file, dest)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
