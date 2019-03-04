package com.didi.chameleon.sdk.utils;

import java.util.HashMap;

public class CmlClassInitManager {

    public static final String INIT_RICH_TEXT = "com.didi.chameleon.weex.richtextcomponent.CmlRichTextInit";

    private static HashMap<String, ICmlClassInit> initSet = new HashMap<>();

    public static ICmlClassInit initClass(String fullClassPath) {
        if (initSet.containsKey(fullClassPath)) {
            return initSet.get(fullClassPath);
        }
        try {
            Class clazz = Class.forName(fullClassPath);
            Object object = clazz.newInstance();
            if (object instanceof ICmlClassInit) {
                ICmlClassInit classInit = (ICmlClassInit) object;
                classInit.init();
                initSet.put(fullClassPath, classInit);
                return classInit;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean hasInit(String fullClassPath) {
        return initSet.containsKey(fullClassPath);
    }

}
