package com.example.demo.annotation;

import java.lang.reflect.Field;
import java.util.UUID;

public class UuidGenerator {

    public static void process(Object obj) {
        try {
            for (Field field : obj.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(AutoGeneratedUuid.class)) {
                    field.setAccessible(true);
                    if (field.get(obj) == null) {
                        field.set(obj, UUID.randomUUID());
                    }
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

