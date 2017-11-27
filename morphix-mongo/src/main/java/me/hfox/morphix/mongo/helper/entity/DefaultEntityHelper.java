package me.hfox.morphix.mongo.helper.entity;

import me.hfox.morphix.mongo.Morphix;
import me.hfox.morphix.mongo.MorphixDefaults;
import me.hfox.morphix.mongo.annotation.Id;
import me.hfox.morphix.mongo.annotation.entity.Entity;
import me.hfox.morphix.mongo.annotation.lifecycle.*;
import me.hfox.morphix.mongo.exception.MorphixException;
import me.hfox.morphix.mongo.util.AnnotationUtils;
import org.bson.types.ObjectId;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DefaultEntityHelper implements EntityHelper {

    private Morphix morphix;
    private List<Class<? extends Annotation>> methodAnnotations;
    private List<Class<? extends Annotation>> fieldAnnotations;

    public DefaultEntityHelper(Morphix morphix) {
        this.morphix = morphix;

        this.methodAnnotations = new ArrayList<>();
        methodAnnotations.add(PreCreate.class);
        methodAnnotations.add(PreDelete.class);
        methodAnnotations.add(PreLoad.class);
        methodAnnotations.add(PreSave.class);
        methodAnnotations.add(PreUpdate.class);
        methodAnnotations.add(PreMarshal.class);

        methodAnnotations.add(PostCreate.class);
        methodAnnotations.add(PostDelete.class);
        methodAnnotations.add(PostLoad.class);
        methodAnnotations.add(PostSave.class);
        methodAnnotations.add(PostUpdate.class);
        methodAnnotations.add(PostMarshal.class);


        this.fieldAnnotations = new ArrayList<>();
        fieldAnnotations.add(CreatedAt.class);
        fieldAnnotations.add(UpdatedAt.class);
        fieldAnnotations.add(AccessedAt.class);
    }

    @Override
    public List<Class<? extends Annotation>> getLifecycleMethodAnnotations() {
        return methodAnnotations;
    }

    @Override
    public List<Class<? extends Annotation>> getLifecycleFieldAnnotations() {
        return fieldAnnotations;
    }

    @Override
    public String getCollectionName(Class<?> clazz) {
        Entity entity = AnnotationUtils.getHierarchicalAnnotation(clazz, Entity.class);
        if (entity == null) {
            throw new MorphixException("No Entity annotation could be found for " + clazz.getSimpleName());
        }

        return getCollectionName(clazz, entity);
    }

    @Override
    public String getCollectionName(Class<?> clazz, Entity entity) {
        String name;
        if (entity.value() == null || entity.value().equals(MorphixDefaults.DEFAULT_COLLECTION_NAME)) {
            name = morphix.getNameHelper(entity.nameHelper()).generate(clazz);
        } else {
            name = entity.value();
        }

        return name;
    }

    @Override
    public ObjectId getObjectId(Object object) {
        return getObjectId(object, false);
    }

    @Override
    public ObjectId getObjectId(Object object, boolean error) {
        for (Field field : getFields(object)) {
            if (field.getAnnotation(Id.class) != null) {
                try {
                    return (ObjectId) field.get(object);
                } catch (IllegalAccessException ex) {
                    throw new MorphixException(ex);
                }
            }
        }

        if (error) {
            throw new MorphixException("No @Id found for " + object.getClass().getSimpleName());
        }

        return null;
    }

    @Override
    public void setObjectId(Object object, ObjectId id) {
        for (Field field : getFields(object)) {
            if (field.getAnnotation(Id.class) != null) {
                try {
                    field.set(object, id);
                } catch (IllegalAccessException ex) {
                    throw new MorphixException(ex);
                }
            }
        }
    }

    public List<Field> getFields(Object object) {
        return getFields(object.getClass());
    }

    public List<Field> getFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        do {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                fields.add(field);
            }

            clazz = clazz.getSuperclass();
        } while (cont(clazz));

        return fields;
    }

    public List<Method> getMethods(Object object) {
        return getMethods(object.getClass());
    }

    public List<Method> getMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<>();

        do {
            for (Method method : clazz.getDeclaredMethods()) {
                method.setAccessible(true);
                methods.add(method);
            }

            clazz = clazz.getSuperclass();
        } while (cont(clazz));

        return methods;
    }

    private boolean cont(Class<?> cls) {
        if (cls == Object.class || cls == null) {
            return false;
        }

        Entity entity = cls.getAnnotation(Entity.class);
        return entity == null || entity.inheritParentFields();
    }

}