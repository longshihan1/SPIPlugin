package com.longshihan.lplugin

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

class TraceMethodAdapter(api:Int, mv: MethodVisitor, access:Int, name:String,  desc:String,  className:String,
                          hasWindowFocusMethod:Boolean,  isActivityOrSubClass:Boolean,  isNeedTrace:Boolean) : AdviceAdapter(api,mv,access,name,desc) {
}