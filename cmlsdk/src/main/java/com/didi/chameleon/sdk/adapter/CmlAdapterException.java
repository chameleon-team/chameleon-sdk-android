package com.didi.chameleon.sdk.adapter;

public class CmlAdapterException extends NoClassDefFoundError {

    public static CmlAdapterException throwAdapterNone(Class adapterClass) {
        String msg = "please set " + adapterClass.getName() + " for cml use";
        return new CmlAdapterException(msg);
    }

    private CmlAdapterException(String msg) {
        super(msg);
    }

}
