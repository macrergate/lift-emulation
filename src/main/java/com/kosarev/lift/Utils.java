package com.kosarev.lift;

import java.util.Arrays;

public class Utils {
    public static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    public static void check(boolean condition) {
        if (!condition) {
            throw new IllegalStateException("wrong state");
        }
    }

    public static void check(boolean condition, Object... context) {
        if (!condition) {
            throw new IllegalStateException("wrong state! context: " + Arrays.toString(context));
        }
    }

}
