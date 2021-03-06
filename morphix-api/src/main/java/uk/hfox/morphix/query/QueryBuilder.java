/*-
 * ========================LICENSE_START========================
 * Morphix API
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
package uk.hfox.morphix.query;

import uk.hfox.morphix.connector.MorphixConnector;
import uk.hfox.morphix.query.raw.FindQuery;
import uk.hfox.morphix.query.result.QueryResult;

/**
 * Interface used to build connector-specific queries
 */
public interface QueryBuilder<T> {

    /**
     * Gets the connector used to query the database
     *
     * @return The connector used to query the database
     */
    MorphixConnector getConnector();

    /**
     * Gets the class output expected by this query
     *
     * @return The generic class
     */
    Class<T> getQueryType();

    /**
     * Create a field query for the specified field
     *
     * @param field The field to check
     *
     * @return The FieldQueryBuilder representing the specified field
     */
    FieldQueryBuilder<T> where(String field);

    /**
     * Creates a sort objective for the database you are using.
     * This will allow you to set DB specific mechanisms
     *
     * @return The empty sort objective
     */
    QuerySortBuilder<T, ?> sort();

    /**
     * Delete the resulting object(s) from the database
     */
    void delete();

    /**
     * Delete the resulting object(s) from the database, with the option of deleting just 1
     *
     * @param justOne true if only 1 object should be deleted, false otherwise
     */
    void delete(boolean justOne);

    /**
     * Get an iterable copy of the results given by the query
     *
     * @return An iterable set of results
     */
    QueryResult<T> result();

    /**
     * Gets a DB response of this query
     *
     * @return The db response
     */
    FindQuery find();

}
