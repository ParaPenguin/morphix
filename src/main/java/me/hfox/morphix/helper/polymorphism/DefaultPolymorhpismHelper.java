package me.hfox.morphix.helper.polymorphism;

import com.mongodb.DBObject;
import me.hfox.morphix.annotation.entity.Polymorph;
import me.hfox.morphix.exception.MorphixException;
import me.hfox.morphix.util.AnnotationUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static me.hfox.morphix.helper.polymorphism.PolymorphSelector.defaultAnnotatedField;
import static me.hfox.morphix.helper.polymorphism.PolymorphSelector.defaultField;

public class DefaultPolymorhpismHelper implements PolymorhpismHelper {

    private DefaultPolymorphSelector defaultSelector;
    private Map<Polymorph, PolymorphSelector> selectors;

    public DefaultPolymorhpismHelper() {
        defaultSelector = new DefaultPolymorphSelector();
        selectors = new HashMap<>();
    }

    @Override
    public void register(Class<?> type, PolymorphSelector<?> selector) {
        register(type, selector, true);
    }

    @Override
    public void register(Class<?> type, PolymorphSelector<?> selector, boolean highest) {
        Entry<Class<?>, Polymorph> entry;
        if (highest) {
            entry = AnnotationUtils.getHighestClassAnnotation(type, Polymorph.class);
        } else {
            entry = AnnotationUtils.getClassAnnotation(type, Polymorph.class);
        }

        if (entry == null) {
            throw new IllegalArgumentException(type.getName() + " does not have a Polymorph annotation in it's hierarchy");
        }

        selectors.put(entry.getValue(), selector);
    }

    @Override
    public Class<?> generate(DBObject document) {
        String annotatedName = (String) document.get(defaultAnnotatedField);
        if (annotatedName == null) {
            annotatedName = (String) document.get("annotatedClassName");
        }

        PolymorphSelector selector;
        if (annotatedName == null) {
            selector = defaultSelector;
        } else {
            Class<?> annotated = generate(annotatedName);
            Polymorph polymorph = annotated.getAnnotation(Polymorph.class);
            if (polymorph == null || selectors.get(polymorph) == null) {
                selector = defaultSelector;
            } else {
                selector = selectors.get(polymorph);
            }
        }

        return selector.select(document);
    }

    @Override
    public Class<?> generate(String string) {
        if (string == null) {
            return null;
        }

        try {
            return Class.forName(string);
        } catch (NullPointerException | ClassNotFoundException ex) {
            throw new MorphixException(ex);
        }
    }

    @Override
    public void store(DBObject document, Class<?> type) {
        document.put(defaultField, type.getName());

        Entry<Class<?>, Polymorph> entry = AnnotationUtils.getHighestClassAnnotation(type, Polymorph.class);
        if (entry == null) {
            return;
        }

        Class<?> cls = entry.getKey();
        Polymorph polymorph = entry.getValue();
        if (polymorph == null) {
            return;
        }

        if (polymorph.storeAnnotatedClass()) {
            document.put(defaultAnnotatedField, cls.getName());
        }
    }

}
