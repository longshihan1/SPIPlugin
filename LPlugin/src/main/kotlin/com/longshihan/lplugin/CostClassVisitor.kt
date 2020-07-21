package com.longshihan.lplugin

import org.objectweb.asm.*
import org.objectweb.asm.commons.AdviceAdapter

/**
 * Created by longhe on 2020-07-17.
 */
class CostClassVisitor(classVisitor: ClassVisitor) : ClassVisitor(Opcodes.ASM5, classVisitor) {
    override fun visitMethod(
        access: Int,
        name: String?,
        desc: String?,
        signature: String?,
        exceptions: Array<String>?
    ): MethodVisitor {
        var mv = cv.visitMethod(access, name, desc, signature, exceptions)
        mv = object : AdviceAdapter(Opcodes.ASM5, mv, access, name, desc) {
            private var inject = false
            override fun visitAnnotation(desc: String, visible: Boolean): AnnotationVisitor {
                inject = true
                return super.visitAnnotation(desc, visible)
            }

            protected override fun onMethodEnter() {
                if (inject) {
                    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                    mv.visitLdcInsn("========start=========");
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                    mv.visitLdcInsn(name);
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
                    mv.visitMethodInsn(INVOKESTATIC, "com/longshihan/lplugin/TimeCache", "setStartTime", "(Ljava/lang/String;J)V", false);

                }
            }

            protected override fun onMethodExit(opcode: Int) {
                if (inject) {
                    //坐等插代码
                    mv.visitLdcInsn(name);
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
                    mv.visitMethodInsn(INVOKESTATIC, "com/longshihan/lplugin/TimeCache", "setEndTime", "(Ljava/lang/String;J)V", false);
                    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                    mv.visitLdcInsn(name);
                    mv.visitMethodInsn(INVOKESTATIC, "com/longshihan/lplugin/TimeCache", "getCostTime", "(Ljava/lang/String;)Ljava/lang/String;", false);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                    mv.visitLdcInsn("========end=========");
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

                }
            }
        }
        return mv
    }
}