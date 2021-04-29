package com.longshihan.lplugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.longshihan.lplugin.utils.CommonUtil
import com.longshihan.lplugin.utils.Config
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassReader.EXPAND_FRAMES
import org.objectweb.asm.ClassWriter
import java.io.*
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

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
        return true
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        super.transform(transformInvocation)
        if (!Config.enable) {
            println("时间插桩插件关闭")
        } else {
            println("时间插桩插件开启")
        }
        try {
            println("===== ASM Transform =====")
            println("${transformInvocation?.inputs}")
            println("${transformInvocation?.referencedInputs}")
            println("${transformInvocation?.outputProvider}")
            println("${transformInvocation?.isIncremental}")
            //当前是否是增量编译
            val isIncremental: Boolean? = transformInvocation?.isIncremental
            //消费型输入，可以从中获取jar包和class文件夹路径。需要输出给下一个任务
            val inputs: MutableCollection<TransformInput>? = transformInvocation?.inputs
            //引用型输入，无需输出。
            val referencedInputs: MutableCollection<TransformInput>? =
                transformInvocation?.referencedInputs
            //OutputProvider管理输出路径，如果消费型输入为空，你会发现OutputProvider == null
            val outputProvider = transformInvocation?.outputProvider
            inputs?.stream()?.forEach { input ->
                input.jarInputs.stream().forEach { jarInput ->
                    var isOriginClass=false;
                    if (Config.enable) {
                        try {
                            if (isIncremental!!) {
                                val destFile = outputProvider?.getContentLocation(
                                    jarInput.file.absolutePath,
                                    jarInput.contentTypes, jarInput.scopes, Format.JAR
                                )
                                when (jarInput.status) {
                                    Status.ADDED -> {//文件添加，需要对文件进行拷贝
                                        println("jar add")
                                        traceJarFiles(jarInput, outputProvider)
                                    }
                                    Status.CHANGED -> {//如果源jar文件状态是已经被修改了，那么目标jar文件要删掉,然后重新拷贝
                                        println("jar change")
                                        if (destFile!!.exists()) {
                                            FileUtils.deletePath(destFile)
                                        }
                                        traceJarFiles(jarInput, outputProvider)
                                    }
                                    Status.NOTCHANGED -> {//文件状态没变化 不需要修改输出文件
                                    }
                                    Status.REMOVED -> {
                                        println("jar remove")
                                        if (destFile!!.exists()) {
                                            FileUtils.deletePath(destFile)
                                        }
                                    }
                                }
                            } else {
                                println("jar is operate")
                                traceJarFiles(jarInput, outputProvider)
                            }
                        }catch (e:Exception){
                            isOriginClass=true
                            println("jar 报错："+e.message)
                        }
                    } else {
                        isOriginClass=true;
                    }
                    if (isOriginClass){
                        val dest = outputProvider?.getContentLocation(
                            jarInput.name,
                            jarInput.contentTypes, jarInput.scopes, Format.JAR
                        )
                        FileUtils.copyFile(jarInput.file, dest)
                    }
                }
                input.directoryInputs.stream().forEach { directoryInput ->
                    var isOriginClass=false;
                    if (Config.enable) {
                        try {
                            transformFiles(directoryInput, outputProvider, isIncremental!!)
                        } catch (e: Exception) {
                            isOriginClass=true
                            println("file 报错:" + e.message)
                        }
                    } else {
                        isOriginClass=true
                    }
                    if (isOriginClass){
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

    fun transformFiles(directoryInput: DirectoryInput, outputProvider: TransformOutputProvider?, incremental: Boolean) {
        val dest = outputProvider?.getContentLocation(directoryInput.name, directoryInput.contentTypes,
            directoryInput.scopes, Format.DIRECTORY)
        val srcDirPath = directoryInput.file.absolutePath
        val destDirPath = dest?.absolutePath
        if (incremental){
            directoryInput.changedFiles.forEach{
                when(it.value){
                    Status.REMOVED->{
                        val destFilePath = it.key.absolutePath.replace(srcDirPath, destDirPath!!)
                        val destFile =  File(destFilePath)
                        println("file remove :"+dest.absolutePath)
                        if (destFile.exists()) {
                            FileUtils.delete(destFile)
                        }
                    }
                    Status.NOTCHANGED->{}
                    Status.ADDED->{
                        println("file ADDED ")
                        traceFiles(directoryInput,outputProvider)
                    }
                    Status.CHANGED->{
                        println("file CHANGED ")
                        traceFiles(directoryInput,outputProvider)
                    }
                }
            }
        }else{
            traceFiles(directoryInput,outputProvider)
        }
    }
    fun traceFiles(directoryInput: DirectoryInput, outputProvider: TransformOutputProvider?) {
        val fileCPath = directoryInput.file.absolutePath

        directoryInput.file.walk().forEach { file ->
            val name = file.name
            val dpath = file.absolutePath
            var currentPath = dpath.replace(fileCPath + File.separator, "")
            currentPath = transformPath(currentPath, "\\")
            if (checkPath(currentPath) && file.isFile) {
                try {
                    println("$currentPath:is changeing ,,,,")
                    val fileInputStream = FileInputStream(file)
                    val code = tranformBtye(fileInputStream, currentPath)
                    val fos = FileOutputStream(
                        file.parentFile.absolutePath + File.separator + name
                    )
                    fos.write(code)
                    fos.close()
                } catch (e: Exception) {
                    e.printStackTrace()
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

    fun traceJarFiles(jarInput: JarInput, outputProvider: TransformOutputProvider?) {
        if (jarInput.file.absolutePath.endsWith(".jar")) {
            var jarName = jarInput.name
            val md5Name = DigestUtils.md5Hex(jarInput.file.absolutePath)
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length - 4)
            }
            val jarFile = JarFile(jarInput.file)
            val enumeration = jarFile.entries()
            val tempFile = File(jarInput.file.parent + File.separator + "classes_temp.jar")
            if (tempFile.exists()) {
                tempFile.delete()
            }
            val jarOutputStream = JarOutputStream(FileOutputStream(tempFile))
            while (enumeration.hasMoreElements()) {
                val jarEntry = enumeration.nextElement()
                var entryName = jarEntry.name
                val zipEntry = ZipEntry(entryName)
                val inputStream = jarFile.getInputStream(jarEntry)
                println(entryName+":"+jarFile.name)
                entryName = transformPath(entryName, "/")
                if (checkPath(entryName)) {//匹配class文件名
                    println("$entryName:os changeing ,,,,")
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(tranformBtye(inputStream, entryName))
                } else {
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(CommonUtil.transformBytes(inputStream))
                }
                jarOutputStream.closeEntry()
            }
            jarOutputStream.close()
            jarFile.close()
            //处理完输出给下一任务作为输入
            val dest = outputProvider?.getContentLocation(
                jarName + md5Name,
                jarInput.contentTypes, jarInput.scopes, Format.JAR
            )
            FileUtils.copyFile(tempFile, dest)
            tempFile.delete()
        }
    }

    fun tranformBtye(inputStream: InputStream, currentPath: String): ByteArray {
        val cr = ClassReader(inputStream)
        val cw = ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
        val cv = TestMethodClassAdapter(currentPath, cw)
        cr.accept(cv, EXPAND_FRAMES)
        return cw.toByteArray()
    }

    fun checkPath(path: String): Boolean {
        if (!path.endsWith(".class")) {
            return false
        }
        for (content in Config.blackStartList) {
            if (path.startsWith(content)) {
                return false
            }
        }
        for (content in Config.blackEndList) {
            if (path.endsWith(content)) {
                return false
            }
        }
        for (content in Config.blackKeyList) {
            if (path.contains(content)) {
                return false
            }
        }
        return true
    }

    fun transformPath(path: String, sequter: String): String {
        val currentPath = path.replace(sequter, ".")
        return currentPath
    }

}
