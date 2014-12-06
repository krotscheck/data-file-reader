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
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * This class provides a convenience class by which files may be quickly
 * encoded. It uses the Java ServiceLoader to discover all appropriate
 * implementations of IDataEncoder, and creates one based on your passed
 * mimetype.
 *
 * @author Michael Krotscheck
 */
public final class FileStreamEncoder
        implements IFilteringDataStream {

    /**
     * The output encoder used for this instance.
     */
    private final IDataEncoder outputEncoder;

    /**
     * Creates a new instance of the FileStreamEncoder.
     *
     * @param destination The destination output stream.
     * @param mimeType    The explicit MimeType to use when encoding.
     * @throws java.lang.ClassNotFoundException If the encoder cannot be
     *                                          found.ø
     */
    public FileStreamEncoder(final OutputStream destination,
                             final String mimeType)
            throws ClassNotFoundException {
        try {
            outputEncoder = EncoderCache.getEncoder(mimeType);
            outputEncoder.setOutputStream(destination);
        } catch (IllegalAccessException | InstantiationException ie) {
            throw new ClassNotFoundException(String.format("Cannot create "
                    + "encoder for mimetype %s", mimeType));
        }
    }

    /**
     * Write a row to the underlying encoder.
     *
     * @param row A row of data, mapped as column:row.
     * @throws java.io.IOException Thrown when there's a problem writing to the
     *                             destination.
     */
    public void write(final Map<String, Object> row) throws IOException {
        outputEncoder.write(row);
    }

    /**
     * Closes the encoder stream.
     *
     * @throws java.io.IOException Exception if the wrapped encoder cannot
     *                             close.
     */
    public void close() throws IOException {
        outputEncoder.close();
    }

    /**
     * Add a filter.
     *
     * @param filter The filter to add.
     */
    @Override
    public void addFilter(final IDataFilter filter) {
        outputEncoder.addFilter(filter);
    }

    /**
     * Add multiple filters.
     *
     * @param filters The filters to add.
     */
    @Override
    public void addFilters(final List<IDataFilter> filters) {
        outputEncoder.addFilters(filters);
    }

    /**
     * Check to see if this decoder contains a filter.
     *
     * @param filter The filter to check.
     * @return True if the filter is loaded, otherwise false.
     */
    @Override
    public Boolean containsFilter(final IDataFilter filter) {
        return outputEncoder.containsFilter(filter);
    }

    /**
     * Get all the filters.
     *
     * @return All the filters.
     */
    @Override
    public List<IDataFilter> getFilters() {
        return outputEncoder.getFilters();
    }

    /**
     * Remove the filter from the list.
     *
     * @param filter The filter to remove.
     */
    @Override
    public void removeFilter(final IDataFilter filter) {
        outputEncoder.removeFilter(filter);
    }

    /**
     * Clear the filters.
     */
    @Override
    public void clearFilters() {
        outputEncoder.clearFilters();
    }

    /**
     * Apply the filters.
     *
     * @param row The row to filter.
     * @return The filtered row.
     */
    public Map<String, Object> applyFilters(final Map<String, Object> row) {
        return outputEncoder.applyFilters(row);
    }
}
