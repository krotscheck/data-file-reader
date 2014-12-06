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
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class provides a convenience class by which files may be quickly
 * decoded. It uses the Java ServiceLoader to discover all appropriate
 * implementations of IDataDecoder, and creates one based on your passed
 * mimetype.
 *
 * @author Michael Krotscheck
 */
public final class FileStreamDecoder
        implements IFilteringDataStream,
        Iterable<Map<String, Object>>,
        Closeable {

    /**
     * Our input stream reader.
     */
    private IDataDecoder inputDecoder = null;

    /**
     * Creates a new instance of the FileStreamDecoder.
     *
     * @param source   The source input stream.
     * @param mimeType The mimetype.
     * @throws ClassNotFoundException Thrown when a decoder is not found.
     */
    public FileStreamDecoder(final InputStream source, final String mimeType)
            throws ClassNotFoundException {
        this(source, mimeType, null);
    }

    /**
     * Creates a new instance of the FileStreamDecoder.
     *
     * @param source   The source input stream.
     * @param mimeType The mimetype.
     * @param maxRows  The maximum number of rows.
     * @throws ClassNotFoundException Thrown when a decoder is not found.
     */
    public FileStreamDecoder(final InputStream source, final String mimeType,
                             final Long maxRows)
            throws ClassNotFoundException {
        try {
            inputDecoder = DecoderCache.getDecoder(mimeType);
            inputDecoder.setInputStream(source);
            inputDecoder.setMaxRows(maxRows);
        } catch (IllegalAccessException | InstantiationException ie) {
            throw new ClassNotFoundException(String.format("Cannot create "
                    + "decoder for mimetype %s", mimeType));
        }
    }

    /**
     * Returns the iterator from the underlying data stream.
     *
     * @return The iterator.
     */
    @Override
    public Iterator<Map<String, Object>> iterator() {
        inputDecoder.addFilters(getFilters());
        return inputDecoder.iterator();
    }

    /**
     * Closes the file input stream.
     *
     * @throws java.io.IOException IO Exception thrown by the wrapped encoder.
     */
    public void close() throws IOException {
        inputDecoder.close();
    }

    /**
     * Add a filter.
     *
     * @param filter The filter to add.
     */
    @Override
    public void addFilter(final IDataFilter filter) {
        inputDecoder.addFilter(filter);
    }

    /**
     * Add multiple filters.
     *
     * @param filters The filters to add.
     */
    @Override
    public void addFilters(final List<IDataFilter> filters) {
        inputDecoder.addFilters(filters);
    }

    /**
     * Check to see if this decoder contains a filter.
     *
     * @param filter The filter to check.
     * @return True if the filter is loaded, otherwise false.
     */
    @Override
    public Boolean containsFilter(final IDataFilter filter) {
        return inputDecoder.containsFilter(filter);
    }

    /**
     * Get all the filters.
     *
     * @return All the filters.
     */
    @Override
    public List<IDataFilter> getFilters() {
        return inputDecoder.getFilters();
    }

    /**
     * Remove the filter from the list.
     *
     * @param filter The filter to remove.
     */
    @Override
    public void removeFilter(final IDataFilter filter) {
        inputDecoder.removeFilter(filter);
    }

    /**
     * Clear the filters.
     */
    @Override
    public void clearFilters() {
        inputDecoder.clearFilters();
    }

    /**
     * Apply the filters.
     *
     * @param row The row to filter.
     * @return The filtered row.
     */
    public Map<String, Object> applyFilters(final Map<String, Object> row) {
        return inputDecoder.applyFilters(row);
    }

    /**
     * Get the maximum rows.
     *
     * @return The max rows.
     */
    public Long getMaxRows() {
        return inputDecoder.getMaxRows();
    }

    /**
     * Set the maximum rows.
     *
     * @param rows The number of rows, default null.
     */
    public void setMaxRows(final Long rows) {
        inputDecoder.setMaxRows(rows);
    }

}
