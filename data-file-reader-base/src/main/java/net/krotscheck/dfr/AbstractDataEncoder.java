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

import java.io.IOException;
import java.util.Map;

/**
 * An abstract implementation of the data encoder, to consolidate some common
 * functionality and provide a simple extension point.
 *
 * @author Michael Krotscheck
 */
public abstract class AbstractDataEncoder
        extends AbstractFilteredDataStream
        implements IDataEncoder {

    /**
     * Write a row to the file.
     *
     * @param row A row of data.
     * @throws java.io.IOException Thrown when there are problems writing to the
     *                             destination.
     */
    @Override
    public final void write(final Map<String, Object> row) throws IOException {
        Map<String, Object> filteredRow = applyFilters(row);

        writeToOutput(filteredRow);
    }

    /**
     * Protected write-to-stream method. Implement this.
     *
     * @param row A row of data.
     * @throws java.io.IOException Thrown when there are problems writing to the
     *                             destination.
     */
    protected abstract void writeToOutput(final Map<String, Object> row)
            throws IOException;
}
