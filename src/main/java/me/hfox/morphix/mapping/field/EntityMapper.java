package me.hfox.morphix.mapping.field;

import com.mongodb.DBObject;
import com.mongodb.DBRef;
import me.hfox.morphix.Morphix;
import me.hfox.morphix.annotation.NotSaved;
import me.hfox.morphix.annotation.Reference;
import me.hfox.morphix.annotation.entity.Entity;
import me.hfox.morphix.annotation.entity.StoreEmpty;
import me.hfox.morphix.annotation.entity.StoreNull;
import me.hfox.morphix.annotation.entity.Polymorph;
import me.hfox.morphix.annotation.lifecycle.Lifecycle;
import me.hfox.morphix.exception.MorphixException;
import me.hfox.morphix.util.AnnotationUtils;
import org.bson.types.ObjectId;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class EntityMapper<T> extends FieldMapper<T> {

    private Object suppliedResult;
    
    private Entity entity;
    private Lifecycle lifecycle;
    private StoreEmpty storeEmpty;
    private StoreNull storeNull;
    private Polymorph polymorph;
    private boolean polymorphEnabled;

    private Reference reference;
    private NotSaved notSaved;

    private Map<Field, FieldMapper> fields;

    public EntityMapper(Class<T> type, Class<?> parent, Field field, Morphix morphix) {
        super(type, parent, field, morphix);
    }

    @SuppressWarnings("unchecked")
    public EntityMapper(T result, Morphix morphix) {
        super((Class<T>) result.getClass(), null, null, morphix);
        this.suppliedResult = result;
    }

    @Override
    protected void discover() {
        super.discover();
        entity = AnnotationUtils.getHierarchicalAnnotation(type, Entity.class);
        lifecycle = AnnotationUtils.getHierarchicalAnnotation(type, Lifecycle.class);
        storeEmpty = AnnotationUtils.getHierarchicalAnnotation(type, StoreEmpty.class);
        storeNull = AnnotationUtils.getHierarchicalAnnotation(type, StoreNull.class);
        polymorph = AnnotationUtils.getHierarchicalAnnotation(type, Polymorph.class);
        polymorphEnabled = polymorph == null || polymorph.value();

        if (field != null) {
            reference = field.getAnnotation(Reference.class);
            notSaved = field.getAnnotation(NotSaved.class);
        }

        fields = new HashMap<>();
        Class<?> cls = type;
        do {
            for (Field field : cls.getDeclaredFields()) {
                FieldMapper mapper = create(parent, field, morphix);
                if (mapper != null) {
                    fields.put(field, mapper);
                }
            }

            cls = cls.getSuperclass();
        } while (cont(cls));
    }

    private boolean cont(Class<?> cls) {
        if (cls == Object.class) {
            return false;
        }

        Entity entity = cls.getAnnotation(Entity.class);
        return entity == null || entity.inheritParentFields();

    }

    @Override
    public Object unmarshal(Object obj) {
        if (obj == null) {
            return null;
        }

        if (reference != null) {
            if (reference.dbRef() && obj instanceof DBRef) {
                DBRef ref = (DBRef) obj;
                return null; // TODO: update to fetch using reference
            } else if (!reference.dbRef() && obj instanceof ObjectId) {
                ObjectId id = (ObjectId) obj;
                return null;  // TODO: update to fetch using id
            }

            return null;
        }

        if (!(obj instanceof DBObject)) {
            return null;
        }

        DBObject object = (DBObject) obj;
        Class<?> cls = polymorphEnabled ? morphix.getPolymorhpismHelper().generate(object) : type;

        Object result;
        if (suppliedResult != null) {
            result = suppliedResult;
        } else {
            try {
                ReflectionFactory factory = ReflectionFactory.getReflectionFactory();
                Constructor empty = Object.class.getDeclaredConstructor();
                Constructor constructor = factory.newConstructorForSerialization(cls, empty);

                result = constructor.newInstance();
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
                throw new MorphixException(ex);
            }
        }
        
        if (lifecycle != null) {
            if (suppliedResult != null) {
                // TODO: call @PreUpdate
            } else {
                // TODO: call @PreLoad
            }
        }

        for (Entry<Field, FieldMapper> entry : fields.entrySet()) {
            Field field = entry.getKey();
            field.setAccessible(true);

            FieldMapper mapper = entry.getValue();

            Object dbResult = object.get(mapper.fieldName);
            if (dbResult == null && storeNull == null) {
                continue;
            }

            Object value = mapper.unmarshal(object.get(mapper.fieldName));
            if (storeEmpty == null) {
                if (value instanceof Collection) {
                    Collection collection = (Collection) value;
                    if (collection.isEmpty()) {
                        continue;
                    }
                } else if (value instanceof Map) {
                    Map map = (Map) value;
                    if (map.isEmpty()) {
                        continue;
                    }
                }
            }
            
            try {
                field.set(result, value);
            } catch (IllegalAccessException ex) {
                throw new MorphixException(ex);
            }
        }

        if (lifecycle != null) {
            if (suppliedResult != null) {
                // TODO: call @PostUpdate
            } else {
                // TODO: call @PostLoad
            }
        }

        return result;
    }

    @Override
    public Object marshal(Object obj) {
        return null;
    }

}
