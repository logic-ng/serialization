// SPDX-License-Identifier: Apache-2.0 and MIT
// Copyright 2023-20xx BooleWorks GmbH

package org.logicng.serialization;

import java.lang.reflect.Field;

public class ReflectionHelper {

    @SuppressWarnings("unchecked")
    static <T> T getField(final Object object, final String name) {
        try {
            final Field f = object.getClass().getDeclaredField(name);
            f.setAccessible(true);
            return (T) f.get(object);
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e); // OK here, has to be a programming mistake
        }
    }

    @SuppressWarnings("unchecked")
    static <T> T getSuperField(final Object object, final String name) {
        try {
            final Field f = object.getClass().getSuperclass().getDeclaredField(name);
            f.setAccessible(true);
            return (T) f.get(object);
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e); // OK here, has to be a programming mistake
        }
    }

    static void setField(final Object object, final String name, final Object value) {
        try {
            final Field f = object.getClass().getDeclaredField(name);
            f.setAccessible(true);
            f.set(object, value);
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e); // OK here, has to be a programming mistake
        }
    }

    static void setSuperField(final Object object, final String name, final Object value) {
        try {
            final Field f = object.getClass().getSuperclass().getDeclaredField(name);
            f.setAccessible(true);
            f.set(object, value);
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e); // OK here, has to be a programming mistake
        }
    }
}
