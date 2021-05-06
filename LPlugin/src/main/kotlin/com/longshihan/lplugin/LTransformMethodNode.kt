package com.longshihan.lplugin

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.MethodNode

class LTransformMethodNode(
    val className:String?,
    access:Int,
    des:String?,
    signature:String?,
    exceptions: Array<out String>?
): MethodNode() {
    private val isConstructor = false
    override fun visitEnd() {
        super.visitEnd()

    }
    private fun isGetSetMethod(): Boolean {
        var ignoreCount = 0
        val iterator: ListIterator<AbstractInsnNode> = instructions.iterator()
        while (iterator.hasNext()) {
            val insnNode: AbstractInsnNode = iterator.next()
            val opcode: Int = insnNode.getOpcode()
            if (-1 == opcode) {
                continue
            }
            if (opcode != Opcodes.GETFIELD && opcode != Opcodes.GETSTATIC && opcode != Opcodes.H_GETFIELD && opcode != Opcodes.H_GETSTATIC && opcode != Opcodes.RETURN && opcode != Opcodes.ARETURN && opcode != Opcodes.DRETURN && opcode != Opcodes.FRETURN && opcode != Opcodes.LRETURN && opcode != Opcodes.IRETURN && opcode != Opcodes.PUTFIELD && opcode != Opcodes.PUTSTATIC && opcode != Opcodes.H_PUTFIELD && opcode != Opcodes.H_PUTSTATIC && opcode > Opcodes.SALOAD) {
                if (isConstructor && opcode == Opcodes.INVOKESPECIAL) {
                    ignoreCount++
                    if (ignoreCount > 1) {
                        return false
                    }
                    continue
                }
                return false
            }
        }
        return true
    }

    private fun isSingleMethod(): Boolean {
        val iterator = instructions.iterator()
        while (iterator.hasNext()) {
            val insnNode = iterator.next()
            val opcode = insnNode.opcode
            if (-1 == opcode) {
                continue
            } else if (Opcodes.INVOKEVIRTUAL <= opcode && opcode <= Opcodes.INVOKEDYNAMIC) {
                return false
            }
        }
        return true
    }

    private fun isEmptyMethod(): Boolean {
        val iterator = instructions.iterator()
        while (iterator.hasNext()) {
            val insnNode = iterator.next()
            val opcode = insnNode.opcode
            return if (-1 == opcode) {
                continue
            } else {
                false
            }
        }
        return true
    }
}