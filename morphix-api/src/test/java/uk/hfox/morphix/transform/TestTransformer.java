package uk.hfox.morphix.transform;

import uk.hfox.morphix.utils.Testing;

import java.util.HashMap;
import java.util.Map;

public class TestTransformer implements Transformer<Object> {

    private final Map<ConvertedType, Converter<Object>> converters;

    public TestTransformer() {
        this.converters = new HashMap<>();
        for (ConvertedType type : ConvertedType.values()) {
            this.converters.put(type, null);
        }
    }

    @Override
    public Converter<Object> getConverter(ConvertedType type) {
        return this.converters.get(type);
    }

    @Override
    public void setConverter(ConvertedType type, Converter<Object> converter) {
        throw Testing.unsupported();
    }

    @Override
    public Object toDB(Object object) {
        throw Testing.unsupported();
    }

    @Override
    public Object toDB(Object object, Filter filter) {
        throw Testing.unsupported();
    }

    @Override
    public <O> O fromGenericDB(Object db, Object entity, Class<O> cls) {
        throw Testing.unsupported();
    }

    @Override
    public <O> O fromGenericDB(Object db, Object entity, Class<O> cls, Filter filter) {
        throw Testing.unsupported();
    }

}
