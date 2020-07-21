package com.longshihan.lplugin

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*

class TestMethodVisitor(methodVisitor: MethodVisitor):MethodVisitor(ASM5,methodVisitor){
    override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, descriptor: String?, isInterface: Boolean) {
        println("== TestMethodVisitor, owner = $owner, name = $name");
        //方法执行之前打印
        mv.visitLdcInsn(" before method exec");
        mv.visitLdcInsn(" [ASM 测试] method in $owner ,name=$name");
        mv.visitMethodInsn(INVOKESTATIC,
            "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(POP);
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        //方法执行之后打印
        mv.visitLdcInsn(" after method exec");
        mv.visitLdcInsn(" method in $owner ,name=$name");
        mv.visitMethodInsn(INVOKESTATIC,
            "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(POP);

    }
}