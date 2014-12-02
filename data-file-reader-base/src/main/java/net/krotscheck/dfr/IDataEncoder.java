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
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * This interface describes a data encoder, which accepts rows of data and
 * writes them to a specific file format.
 *
 * @author Michael Krotscheck
 */
public interface IDataEncoder extends IFilteringDataStream, Closeable {

    /**
     * Retrieve the mimetype which this encoder supports.
     *
     * @return The mimetype, as a string.
     */
    String getMimeType();

    /**
     * Retrieve the OutputStream for this writer's destination.
     *
     * @return The output stream.
     */
    OutputStream getOutputStream();

    /**
     * Set the destination to where the data should be written.
     *
     * @param destination The destination.
     */
    void setOutputStream(OutputStream destination);

    /**
     * Writes a row to the underlying data format.
     *
     * @param row A row of data.
     * @throws java.io.IOException Thrown when there's a problem writing to the
     *                             destination stream.
     */
    void write(Map<String, Object> row) throws IOException;

}
