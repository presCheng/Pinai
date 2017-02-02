package com.jl.utils;

import android.util.Log;

public class ZLog {
    public static void log(String str) {
        Log.i("devlog", str);
    }

    public static void log(int str) {
        Log.i("devlog", str + "");
    }

    public static void log(Class<?> cla, String str) {
        Log.i("devlog", cla.getName() + ":" + str);
    }
}
