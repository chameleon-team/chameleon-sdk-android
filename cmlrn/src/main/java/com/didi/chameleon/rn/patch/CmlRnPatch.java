package com.didi.chameleon.rn.patch;

public class CmlRnPatch {

    static {
        System.loadLibrary("CmlRn");
    }

    public static void test() {
        byte[] a = new byte[10];
        byte[] b = new byte[1];

        byte[] c = bsdiffPatch(a, b);
        System.err.println(String.valueOf(c));
    }

    private static native byte[] bsdiffPatch(byte[] origin, byte[] patch);

}
