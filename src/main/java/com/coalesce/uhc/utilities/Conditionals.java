package com.coalesce.uhc.utilities;

public class Conditionals {
    public static <T, U, A, B> boolean ofBoth(T t, U u, A a, B b) {
        if (t == a) {
            return u == b;
        }
        return u == a && t == b;
    }
}
