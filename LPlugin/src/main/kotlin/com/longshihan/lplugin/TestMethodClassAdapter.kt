package com.longshihan.lplugin

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.ASM5

class TestMethodClassAdapter (var classVisitor: ClassVisitor):ClassVisitor(ASM5,classVisitor),Opcodes{
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor? {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        return return if(mv == null) {
            null
        }else{
            TestMethodVisitor(mv)
        };
    }

}