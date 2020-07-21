package com.longshihan.spiplugin;

import com.google.auto.service.AutoService;
import com.longshihan.spi_api.LTransformListener;

import org.jetbrains.annotations.NotNull;

/**
 * @author longshihan
 * @time 2020/7/18
 */

@AutoService(LTransformListener.class)
public class Test implements LTransformListener {
    @NotNull
    @Override
    public byte[] transform(@NotNull byte[] bytecode) {
        System.out.println("测试进来；哎了:"+bytecode.length);
        return bytecode;
    }
}
