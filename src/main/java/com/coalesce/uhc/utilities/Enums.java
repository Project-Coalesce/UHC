package com.coalesce.uhc.utilities;

import lombok.NonNull;

import java.util.Optional;

public class Enums {
    @NonNull
    public static <T extends Enum<T>> Optional<T> getEnum(Class<T> enumClass, String name) {
        if (enumClass == null || name == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Enum.valueOf(enumClass, name.trim().toUpperCase()));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }
}
