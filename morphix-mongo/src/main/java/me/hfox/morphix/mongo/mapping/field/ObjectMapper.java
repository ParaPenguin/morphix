/*-
 * ========================LICENSE_START========================
 * Morphix Mongo
 * %%
 * Copyright (C) 2017 - 2018 Harry Fox
 * %%
 * This file is part of Morphix, licensed under the MIT License (MIT).
 * 
 * Copyright 2018 Harry Fox <https://hfox.uk/>
 * Copyright 2018 contributors
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * ========================LICENSE_END========================
 */
package me.hfox.morphix.mongo.mapping.field;

import me.hfox.morphix.mongo.Morphix;
import me.hfox.morphix.mongo.MorphixDefaults;
import me.hfox.morphix.mongo.mapping.MappingData;

import java.lang.reflect.Field;

public class ObjectMapper<T> extends FieldMapper<T> {

    public ObjectMapper(Class<T> type, Class<?> parent, Field field, Morphix morphix) {
        super(type, parent, field, morphix);
    }

    @Override
    public T unmarshal(Object obj) {
        return unmarshal(obj, MorphixDefaults.DEFAULT_LIFECYCLE);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T unmarshal(Object obj, boolean lifecycle) {
        return (T) convert(obj);
    }

    @Override
    public T marshal(MappingData mappingData, Object obj) {
        return marshal(mappingData, obj, MorphixDefaults.DEFAULT_LIFECYCLE);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T marshal(MappingData mappingData, Object obj, boolean lifecycle) {
        return (T) convert(obj);
    }

    public Object convert(Object obj) {
        if (obj instanceof Number) {
            Number num = (Number) obj;
            if (Integer.class.equals(type) || int.class.equals(type)) {
                return num.intValue();
            } else if (Double.class.equals(type) || double.class.equals(type)) {
                return num.doubleValue();
            } else if (Float.class.equals(type) || float.class.equals(type)) {
                return num.floatValue();
            } else if (Byte.class.equals(type) || byte.class.equals(type)) {
                return num.byteValue();
            } else if (Short.class.equals(type) || short.class.equals(type)) {
                return num.shortValue();
            } else if (Long.class.equals(type) || long.class.equals(type)) {
                return num.longValue();
            }
        }

        return obj;
    }

}
