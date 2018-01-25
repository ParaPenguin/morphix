/*-
 * ========================LICENSE_START========================
 * Morphix PostgreSQL
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
package me.hfox.morphix.postgre;

import me.hfox.morphix.connector.MorphixConnector;
import me.hfox.morphix.query.Query;

/**
 * Created by Harry on 28/11/2017.
 *
 * PostgreSQL implementation of the Morphix connector
 */
public class MorphixPostgreConnector implements MorphixConnector {

    @Override
    public void connect() {
        // TODO: connect to database
    }

    @Override
    public void disconnect() {
        // TODO: disconnect from database
    }

    @Override
    public <T> Query<T> createQuery(Class<T> cls) {
        // TODO: create query
        return null;
    }

    @Override
    public <T> Query<T> createQuery(Class<T> cls, String collection) {
        // TODO: create query
        return null;
    }

}
