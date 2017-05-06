package com.coalesce.uhc.utilities;

import lombok.SneakyThrows;

public class Reflectives {
    private Reflectives() {
    }

    @SneakyThrows
    public static <T> T makeInstance(Class<? extends T> clazz) {
        return clazz.getConstructor().newInstance();
    }
}
