package me.hfox.morphix.helper.polymorphism;

import com.mongodb.DBObject;
import me.hfox.morphix.exception.MorphixException;

public class DefaultPolymorhpismHelper implements PolymorhpismHelper {

    @Override
    public Class<?> generate(DBObject dbObject) {
        String string = (String) dbObject.get("class_name");
        if (string == null) {
            string = (String) dbObject.get("className");
        }

        return generate(string);
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
    public void store(DBObject dbObject, Class<?> cls) {
        dbObject.put("class_name", cls.getName());
    }

}
