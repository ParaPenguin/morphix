/*-
 * ========================LICENSE_START========================
 * Morphix MongoDB
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
package uk.hfox.morphix.mongo.query.raw;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import uk.hfox.morphix.query.raw.OutputQuery;

public abstract class MongoOutputQuery<T> implements OutputQuery {

    protected final MongoCollection<Document> collection;

    protected boolean queried = false;
    protected T output;

    protected MongoOutputQuery(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    public boolean hasQueried() {
        return queried;
    }

    /**
     * Gets the Object result from this query.
     * The result can be null, best to check hasQueried first
     *
     * @return The output from this query
     */
    public T getOutput() {
        return output;
    }

    @Override
    public void performQuery() {
        if (this.queried) {
            return;
        }

        runQuery();
        this.queried = true;
    }

    /**
     * Runs the query defined by this class
     */
    protected abstract void runQuery();

}
