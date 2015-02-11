package me.hfox.morphix.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

public final class AnnotationUtils {

    private AnnotationUtils() {
        // can't be accessed
    }

    public static String getFieldName(Field field) {
        return field.getName();
    }

    public static <T extends Annotation> T getHierarchicalAnnotation(Class<?> cls, Class<T> annoCls) {
        T anno = null;
        do {
            anno = cls.getAnnotation(annoCls);
            cls = cls.getSuperclass();
        } while (anno != null && cls != Object.class);

        return anno;
    }

}