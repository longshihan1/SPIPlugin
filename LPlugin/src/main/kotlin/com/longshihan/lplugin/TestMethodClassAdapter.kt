package com.longshihan.lplugin

import com.longshihan.lplugin.utils.Config
import com.longshihan.lplugin.utils.UUIDUtils
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.ASM5
import org.objectweb.asm.commons.AdviceAdapter

class TestMethodClassAdapter (val owner:String,var classVisitor: ClassVisitor):ClassVisitor(ASM5,classVisitor),Opcodes{
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor? {
        var mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        mv = object : AdviceAdapter(Opcodes.ASM5, mv, access, name, descriptor) {
            var uuidClassName:String?=null;
            override fun onMethodEnter() {
                for (content in Config.blackMethodList){
                    if (name==content){
                        super.onMethodEnter()
                        return
                    }
                }
                uuidClassName=UUIDUtils.transformUUIDformClass(owner+name)
                println("== onMethodEnter, owner = $owner, name = $name");
                mv.visitLdcInsn(owner)
                mv.visitLdcInsn(name)
                mv.visitLdcInsn(uuidClassName)
                mv.visitMethodInsn(INVOKESTATIC, "com/longshihan/collect/init/Trace", "initFirst",
                    "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",
                    false
                )
            }

            override fun onMethodExit(opcode: Int) {
                for (content in Config.blackMethodList){
                    if (name==content){
                        super.onMethodEnter()
                        return
                    }
                }
                mv.visitLdcInsn(owner)
                mv.visitLdcInsn(name)
                mv.visitLdcInsn(uuidClassName)
                mv.visitMethodInsn(
                    Opcodes.INVOKESTATIC, "com/longshihan/collect/init/Trace", "initLast",
                    "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", false);
            }
        }
        return mv
    }

}