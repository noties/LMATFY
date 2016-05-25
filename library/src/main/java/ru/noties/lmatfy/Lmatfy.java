package ru.noties.lmatfy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dimitry Ivanov on 25.05.2016.
 */
public class Lmatfy {

    private Lmatfy() {}

    public static void please(Object holder, Object target) throws AttachException {

        if (holder == null
                || target == null) {
            throw null;
        }

        final Class<?> holderClass = holder.getClass();

        final List<Field> fields = fieldsAnnotatedWith(Attach.class, holderClass);
        if (fields != null
                && fields.size() > 0) {

            final Class<?> targetClass = target.getClass();
            Class<?> fieldType;

            for (Field field: fields) {
                fieldType = field.getType();
                if (fieldType.isAssignableFrom(targetClass)) {
                    try {
                        field.setAccessible(true);
                        field.set(holder, target);
                    } catch (IllegalAccessException e) {
                        throw AttachException.newInstance(e);
                    }
                } else {
                    throw AttachException.newInstance("`%s`: `%s` must implement/extend `%s`", holderClass.getName(), targetClass.getName(), fieldType.getName());
                }
            }
        } else {
            throw AttachException.newInstance("`%s` has no fields annotated with `@Attach`", holderClass.getName());
        }
    }

    private static <A extends Class<? extends Annotation>> List<Field> fieldsAnnotatedWith(
            A annotationClass,
            Class<?> holderClass
    ) {

        final Field[] fields = holderClass.getDeclaredFields();
        if (fields == null
                || fields.length == 0) {
            return null;
        }

        List<Field> out = null;
        for (Field field: fields) {
            if (field.isAnnotationPresent(annotationClass)) {
                if (out == null) {
                    out = new ArrayList<>();
                }
                out.add(field);
            }
        }

        final Class<?> superClass = holderClass.getSuperclass();
        if (superClass != null) {
            final List<Field> superFields = fieldsAnnotatedWith(annotationClass, superClass);
            if (superFields != null
                    && superFields.size() > 0) {
                if (out == null) {
                    out = superFields;
                } else {
                    out.addAll(superFields);
                }
            }
        }

        return out;
    }
}
