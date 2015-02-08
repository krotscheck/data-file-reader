/*
 * Copyright (c) 2014 Michael Krotscheck
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.krotscheck.dfr;

import java.io.Closeable;
import java.util.Map;

/**
 * This interface describes a data decoder, which attempts to convert a
 * table-based file into an iterable key/value map.
 *
 * @author Michael Krotscheck
 */
public interface IDataDecoder
        extends IFilteringDataStream, Iterable<Map<String, Object>>, Closeable {

    /**
     * Retrieve the mimetype which this decoder supports.
     *
     * @return The mimetype, as a string.
     */
    String getMimeType();

    /**
     * Retrieve the number of rows which this decoder should stop at.
     *
     * @return The number of rows, null for all rows.
     */
    Long getMaxRows();

    /**
     * Set the number of rows at which this decoder should stop. Set to null to
     * read all rows.
     *
     * @param rows The number of rows, default null.
     */
    void setMaxRows(Long rows);

}
