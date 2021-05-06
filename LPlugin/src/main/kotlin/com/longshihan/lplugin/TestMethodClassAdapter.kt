package com.longshihan.lplugin

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.ASM5


class TestMethodClassAdapter (val owner:String,var classVisitor: ClassVisitor):ClassVisitor(ASM5,classVisitor),Opcodes{
    private var isABSClass = false
    private var className: String? = null
    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        className=name
        if (access and Opcodes.ACC_ABSTRACT > 0 || access and Opcodes.ACC_INTERFACE > 0) {
            isABSClass = true
            println("ABClass--$name")
        }
    }
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor? {

            var mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (isABSClass){
            return mv
        }else {
//            return TraceMethodAdapter(className,access,descriptor,signature,exceptions)
            mv = object : AdviceAdapter(Opcodes.ASM5, mv, access, name, descriptor) {
                var uuidClassName: String? = null;
                override fun onMethodEnter() {
                    for (content in Config.blackMethodList) {
                        if (name == content) {
                            super.onMethodEnter()
                            return
                        }
                    }
                    uuidClassName = UUIDUtils.transformUUIDformClass(owner + name)
                    mv.visitLdcInsn(owner)
                    mv.visitLdcInsn(name)
                    mv.visitLdcInsn(uuidClassName)
                    mv.visitMethodInsn(
                        INVOKESTATIC, "com/longshihan/collect/init/Trace", "initFirst",
                        "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",
                        false
                    )
                }

                override fun onMethodExit(opcode: Int) {
                    for (content in Config.blackMethodList) {
                        if (name == content) {
                            super.onMethodEnter()
                            return
                        }
                    }
                    mv.visitLdcInsn(owner)
                    mv.visitLdcInsn(name)
                    mv.visitLdcInsn(uuidClassName)
                    mv.visitMethodInsn(
                        Opcodes.INVOKESTATIC, "com/longshihan/collect/init/Trace", "initLast",
                        "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", false
                    );
                }


            }
            return mv
        }
    }

}