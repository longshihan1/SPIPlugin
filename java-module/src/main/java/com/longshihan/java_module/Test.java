package com.longshihan.java_module;

import com.google.auto.service.AutoService;
import com.longshihan.asminterface.Transformer;

import org.jetbrains.annotations.NotNull;

/**
 * @author longshihan
 * @time 2020/7/18
 */

@AutoService(Transformer.class)
class Test implements Transformer {
    @NotNull
    @Override
    public byte[] transform(@NotNull byte[] bytecode) {
        System.out.println("测试进来；哎了:"+bytecode.length);
        return bytecode;
    }
}
