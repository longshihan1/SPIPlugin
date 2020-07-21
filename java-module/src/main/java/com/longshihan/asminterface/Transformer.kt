package com.longshihan.asminterface

interface Transformer{

    /**
     * Returns the transformed bytecode
     *
     * @param context
     *         The transforming context
     * @param bytecode
     *         The bytecode to be transformed
     * @return the transformed bytecode
     */
    fun transform(bytecode: ByteArray): ByteArray

}