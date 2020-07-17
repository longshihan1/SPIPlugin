package com.longshihan.lplugin

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ASM5
import org.objectweb.asm.commons.AdviceAdapter


/**
 * Created by longhe on 2020-07-17.
 */
public class TRYClassVisitor(classVisitor: ClassVisitor):ClassVisitor(ASM5, classVisitor){
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val methodVisitor:MethodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        return object : AdviceAdapter(ASM5,methodVisitor,access,name,descriptor){


            override fun onMethodEnter() {
                super.onMethodEnter()
            }


            override fun visitMaxs(maxStack: Int, maxLocals: Int) {
                super.visitMaxs(maxStack, maxLocals)
            }

            override fun onMethodExit(opcode: Int) {
                super.onMethodExit(opcode)
            }

            override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
                return super.visitAnnotation(descriptor, visible)
            }
        }


    }
}